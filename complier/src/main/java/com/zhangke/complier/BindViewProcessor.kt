package com.zhangke.complier

import com.squareup.javapoet.JavaFile
import com.squareup.javapoet.MethodSpec
import com.squareup.javapoet.TypeName
import com.squareup.javapoet.TypeSpec
import com.zhangke.annotations.BindView
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.SourceVersion
import javax.lang.model.element.*


/**
 * Created by ZhangKe on 2020/3/17.
 */
class BindViewProcessor : AbstractProcessor() {

    override fun init(processingEnv: ProcessingEnvironment?) {
        super.init(processingEnv)
    }

    override fun getSupportedSourceVersion(): SourceVersion {
        return super.getSupportedSourceVersion()
    }

    override fun getSupportedAnnotationTypes(): Set<String> {
        //返回该处理器可以处理的注解集合
        return setOf(BindView::class.java.canonicalName)
    }

    override fun process(annotations: MutableSet<out TypeElement>, roundEnv: RoundEnvironment): Boolean {
        parseBindView(roundEnv)
        return true
    }

    private fun parseBindView(roundEnv: RoundEnvironment) {
        val bindViewElementSet = roundEnv.getElementsAnnotatedWith(BindView::class.java)
        buildBindClass(bindViewElementSet)
    }

    private fun buildBindClass(eleSet: Set<Element>) {
        val groupedElement = groupingElementWithType(eleSet)
        val keySet = groupedElement.keys
        for (classItem in keySet) {
            val typeBuilder = makeTypeSpecBuilder(classItem)
            val constructorBuilder = makeConstructor(classItem)
            buildConstructorCode(constructorBuilder, groupedElement[classItem])
            typeBuilder.addMethod(constructorBuilder.build())
            val file = JavaFile.builder(getPackageName(classItem), typeBuilder.build())
                    .build()
            file.writeTo(this.processingEnv.filer)
        }
    }

    private fun groupingElementWithType(eleSet: Set<Element>): Map<TypeElement, ArrayList<Element>> {
        val groupedElement = HashMap<TypeElement, ArrayList<Element>>()
        for (item in eleSet) {
            checkAnnotationLegal(item)
            val enclosingElement = item.enclosingElement as TypeElement
            if (groupedElement.keys.contains(enclosingElement)) {
                groupedElement[enclosingElement]!!.add(item)
            } else {
                val list = ArrayList<Element>()
                list += item
                groupedElement[enclosingElement] = list
            }
        }
        return groupedElement
    }

    private fun checkAnnotationLegal(ele: Element) {
        if (ele.kind != ElementKind.FIELD) {
            throw RuntimeException("@BindView must in filed! $ele kind is ${ele.kind}")
        }
        val modifier = ele.modifiers
        if (modifier.contains(Modifier.FINAL)) {
            throw RuntimeException("@BindView filed can not be final!")
        }
        if (modifier.contains(Modifier.PRIVATE)) {
            throw RuntimeException("@BindView filed can not be private")
        }
    }

    private fun makeTypeSpecBuilder(typeEle: TypeElement): TypeSpec.Builder {
        return TypeSpec.classBuilder("${typeEle.simpleName}_ViewBinding")
                .addModifiers(Modifier.PUBLIC)
    }

    private fun makeConstructor(typeElement: TypeElement): MethodSpec.Builder {
        val typeMirror = typeElement.asType()
        return MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(TypeName.get(typeMirror), "target")
    }

    private fun getPackageName(typeElement: Element): String {
        var ele = typeElement
        while (ele.kind != ElementKind.PACKAGE) {
            ele = ele.enclosingElement
        }
        return (ele as PackageElement).qualifiedName.toString()
    }

    private fun buildConstructorCode(bindMethodBuilder: MethodSpec.Builder, elements: ArrayList<Element>?) {
        elements?.let {
            for (itemView in elements) {
                bindMethodBuilder.addStatement("target.${itemView} = " +
                        "target.findViewById(${itemView.getAnnotation(BindView::class.java).value})")
            }
        }
    }
}