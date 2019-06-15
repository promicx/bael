package com.promix.baelui.bind.binder

abstract class ConditionalDataBinder<T>(bindingVariable: Int, layoutId: Int) :
    ItemBinderBase<T>(bindingVariable, layoutId) {

    abstract fun canHandle(model: T): Boolean

    fun canHandle(layoutId: Int): Boolean {
        return this.layoutId == layoutId
    }
}
