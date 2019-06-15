package com.promix.baelui.bind.base

import androidx.databinding.BaseObservable

abstract class BvBase<T>(var model: T) : BaseObservable() {
    abstract fun getId(): String

    override fun equals(other: Any?): Boolean {
        return other is BvBase<*> && this.hashCode() == other.hashCode()
    }

    override fun hashCode(): Int {
        var result = model.hashCode()
        result = 31 * result + getId().hashCode()
        return result
    }
}
