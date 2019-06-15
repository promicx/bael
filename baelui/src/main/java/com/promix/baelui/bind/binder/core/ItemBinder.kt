package com.promix.baelui.bind.binder.core

interface ItemBinder<T> {
    fun getLayoutRes(model: T): Int

    fun getBindingVariable(model: T): Int
}
