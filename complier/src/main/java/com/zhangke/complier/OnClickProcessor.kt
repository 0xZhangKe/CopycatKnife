package com.zhangke.complier

import com.zhangke.annotations.OnClick
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.element.TypeElement

/**
 * Created by ZhangKe on 2020/3/17.
 */
class OnClickProcessor : AbstractProcessor(){

    override fun init(processingEnv: ProcessingEnvironment?) {
        super.init(processingEnv)
    }

    override fun getSupportedAnnotationTypes(): Set<String> {
        return setOf(OnClick::class.java.canonicalName)
    }

    override fun process(annotations: MutableSet<out TypeElement>?, roundEnv: RoundEnvironment?): Boolean {

        return true
    }
}