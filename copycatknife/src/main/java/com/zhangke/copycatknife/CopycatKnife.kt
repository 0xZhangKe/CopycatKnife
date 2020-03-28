package com.zhangke.copycatknife

import android.app.Activity
import java.lang.reflect.Constructor

/**
 * Created by ZhangKe on 2020/3/28.
 */
class CopycatKnife {

    companion object {

        @JvmStatic
        fun bind(activity: Activity) {
            val targetClass = activity::class.java
            val constructor = findBindingConstructorForClass(targetClass)
            constructor?.newInstance(activity)
        }

        private fun findBindingConstructorForClass(cls: Class<*>?): Constructor<*>? {
            if (cls == null) return null
            var bindingConstructor: Constructor<*>? = null
            val clsName = cls.name
            try {
                val bindingClass = cls.classLoader!!.loadClass(clsName + "_ViewBinding")
                bindingConstructor = bindingClass.getConstructor(cls)
            } catch (e: ClassNotFoundException) {
                bindingConstructor = findBindingConstructorForClass(cls.superclass)
            } catch (e: NoSuchMethodException) {
                throw RuntimeException("Unable to find binding constructor for $clsName", e)
            }
            return bindingConstructor
        }
    }

}