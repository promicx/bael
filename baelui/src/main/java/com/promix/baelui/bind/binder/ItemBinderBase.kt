package com.promix.baelui.bind.binder

open class ItemBinderBase<T>(protected val bindingVariable: Int, protected val layoutId: Int) : ItemBinder<T> {

    override fun getLayoutRes(model: T): Int {
        return layoutId
    }

    override fun getBindingVariable(model: T): Int {
        return bindingVariable
    }
}
