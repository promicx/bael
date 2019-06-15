package com.promix.baelui.bind.binder

import com.promix.baelui.bind.base.BvBase
import com.promix.baelui.bind.binder.core.CompositeItemBinder
import com.promix.baelui.bind.binder.core.ConditionalDataBinder
import kotlin.reflect.KClass

class CompositeItemBuilder private constructor(binders: List<ConditionalDataBinder<BvBase<*>>>) : CompositeItemBinder<BvBase<*>>(*binders.toTypedArray()) {
    internal class Builder() {
        private val itemsBinder = mutableListOf<ConditionalDataBinder<BvBase<*>>>()
        fun addItemBinder(clazz: KClass<*>, variable: Int, layoutId: Int): Builder {
            itemsBinder.add(
                createBinder(
                    clazz,
                    variable,
                    layoutId
                )
            )
            return this
        }

        fun build(): CompositeItemBinder<BvBase<*>> {
            return CompositeItemBinder(*itemsBinder.toTypedArray())
        }
    }

    companion object {
        private fun <T : BvBase<*>> createBinder(
            clazz: KClass<*>,
            variable: Int,
            layoutId: Int
        ): ConditionalDataBinder<T> {
            return object : ConditionalDataBinder<T>(variable, layoutId) {
                override fun canHandle(model: T): Boolean {
                    return clazz.java.simpleName == model.javaClass.simpleName
                }
            }
        }
    }
}