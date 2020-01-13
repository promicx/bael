package com.promix.baelui.bind.base

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableList
import java.util.*

abstract class VmBases<T : VmBase<*>> : BaseObservable() {
    @Bindable
    var items: ObservableList<T> = ObservableArrayList()
    private val tempItems: ObservableList<T>

    init {
        tempItems = ObservableArrayList()
    }

    open fun addItem(item: T) {
        val index = items.indexOf(item)
        if (index > -1) {
            items[index] = item
        } else {
            items.add(item)
        }
    }

    open fun setItems(items: Collection<T>) {
        for (item in items) {
            addItem(item)
        }
    }

    @Throws(Exception::class)
    open fun getItem(pos: Int): T {
        check(items.size != 0) { "Item size is 0." }
        return items[pos]
    }

    @Throws(Exception::class)
    open fun remove(item: T) {
        check(items.size != 0) { "Item size is 0." }
        items.remove(item)
    }

    open fun clearAll() {
        items.clear()
    }

    protected open fun sortBy(comparator: Comparator<T>) {
        items.sortWith(comparator)
    }

    interface Prediction<T> {
        fun test(t: T): Boolean
    }
}
