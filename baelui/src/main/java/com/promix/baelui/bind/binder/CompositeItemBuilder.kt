package com.promix.baelui.bind.binder

import com.promix.baelui.bind.base.BvBase
import kotlin.reflect.KClass

class CompositeItemBuilder : CompositeItemBinder<BvBase<Any>>() {
    internal class Builder {
        private val itemsBinder = mutableListOf<ConditionalDataBinder<BvBase<Any>>>()
        fun addItemBinder(clazz: KClass<BvBase<Any>>, variable: Int, layoutId: Int) {
            itemsBinder.add(
                createBinder(
                    clazz,
                    variable,
                    layoutId
                )
            )
        }

        fun build(): CompositeItemBinder<BvBase<Any>> {
            return CompositeItemBinder(*itemsBinder.toTypedArray())
        }
    }

    companion object {
        fun <T : Any> createBinder(clazz: KClass<T>, variable: Int, layoutId: Int): ConditionalDataBinder<T> {
            return object : ConditionalDataBinder<T>(variable, layoutId) {
                override fun canHandle(model: T): Boolean {
                    return clazz.java.simpleName == model.javaClass.simpleName
                }
            }
        }
    }
}