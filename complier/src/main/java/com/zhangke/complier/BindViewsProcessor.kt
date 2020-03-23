package com.zhangke.complier

import com.squareup.javapoet.JavaFile
import com.squareup.javapoet.MethodSpec
import com.squareup.javapoet.TypeName
import com.squareup.javapoet.TypeSpec
import com.zhangke.annotations.BindView
import java.lang.StringBuilder
import java.lang.reflect.Type
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.element.*


/**
 * Created by ZhangKe on 2020/3/17.
 */
class BindViewsProcessor : AbstractProcessor() {

    override fun init(processingEnv: ProcessingEnvironment?) {
        super.init(processingEnv)
    }

    override fun getSupportedAnnotationTypes(): Set<String> {
        //返回该处理器可以处理的注解集合
        return setOf(BindView::class.java.canonicalName)
    }

    override fun process(annotations: MutableSet<out TypeElement>, roundEnv: RoundEnvironment): Boolean {
        //获取所有使用了该注解的 element，可能是类、方法、属性等等
        val bindViewElementSet = roundEnv.getElementsAnnotatedWith(BindView::class.java)
        for (element in bindViewElementSet) {
            checkAnnotationLegal(element)
            val variableElement = element as VariableElement
            println("$element----------->enclosingElementas:${element.enclosingElement}, " +
                    "Type:${element.asType()}, kind:${element.kind}, element type:${element is TypeElement}")

        }
        buildBindClass(bindViewElementSet)
        return true
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

    private fun buildBindClass(eleSet: Set<Element>){
        val groupedElement = HashMap<String, ArrayList<Element>>()
        for(item in eleSet){
            val fullName = item.enclosingElement.toString()
            if(groupedElement.keys.contains(fullName)){
                groupedElement[fullName]!!.add(item)
            }else{
                val list = ArrayList<Element>()
                list += item
                groupedElement[fullName] = list
            }
        }
        val keySet = groupedElement.keys
        for(classItem in keySet){
            val method = MethodSpec.methodBuilder("bind")
                    .addModifiers(Modifier.PUBLIC)
                    .addParameter(String::class.java, "activity")
                    .returns(TypeName.VOID)
                    .build()
            val pkgAndNameArray = classItem.split(".")
            val className = pkgAndNameArray[pkgAndNameArray.size - 1]
            val type = TypeSpec.classBuilder("${className}_Binding")
                    .addModifiers(Modifier.PUBLIC)
                    .addMethod(method)
                    .build()
            val packageName = StringBuilder().apply {
                if(pkgAndNameArray.isEmpty()){
                    append("")
                }else if(pkgAndNameArray.size == 1){
                    append(pkgAndNameArray[0])
                }else{
                    for(index in pkgAndNameArray.indices){
                        if(index < pkgAndNameArray.size - 1){
                            append(pkgAndNameArray[index])
                            if(index != pkgAndNameArray.size - 2){
                                append(".")
                            }
                        }
                    }
                }
            }.toString()
            val file = JavaFile.builder(packageName, type).build()
            file.writeTo(this.processingEnv.filer)
        }
    }
}