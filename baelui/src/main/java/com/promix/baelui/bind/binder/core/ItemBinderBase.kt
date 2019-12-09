package com.promix.baelui.bind.binder.core

open class ItemBinderBase<T>(private val bindingVariable: Int, protected val layoutId: Int) :
    ItemBinder<T> {

    override fun getLayoutRes(model: T): Int {
        return layoutId
    }

    override fun getBindingVariable(model: T): Int {
        return bindingVariable
    }
}
