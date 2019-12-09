package com.promix.baelui.bind.binder

import com.promix.baelui.bind.base.VmBase
import com.promix.baelui.bind.binder.core.CompositeItemBinder
import com.promix.baelui.bind.binder.core.ConditionalDataBinder
import kotlin.reflect.KClass

class ComposeItemBuilder {
    class Builder {
        private val binders = mutableListOf<ConditionalDataBinder<VmBase<*>>>()
        fun addItemBinder(clazz: KClass<*>, variable: Int, layout: Int): Builder {
            binders.add(createBinder(clazz, variable, layout))
            return this
        }

        fun build(): CompositeItemBinder<VmBase<*>> {
            return CompositeItemBinder(*binders.toTypedArray())
        }
    }

    companion object {
        private fun <T : VmBase<*>> createBinder(
            clazz: KClass<*>,
            variable: Int,
            layout: Int
        ): ConditionalDataBinder<T> {
            return object : ConditionalDataBinder<T>(variable, layout) {
                override fun canHandle(model: T): Boolean {
                    return clazz.java.simpleName == model.javaClass.simpleName
                }
            }
        }
    }
}