package com.promix.baelui.bind.base

import androidx.databinding.BaseObservable

abstract class VmBase<T>(var model: T) : BaseObservable() {
    abstract fun getUnique(): String

    override fun equals(other: Any?): Boolean {
        return other is VmBase<*> && this.hashCode() == other.hashCode()
    }

    override fun hashCode(): Int {
        var result = model.hashCode()
        result = 31 * result + getUnique().hashCode()
        return result
    }
}
