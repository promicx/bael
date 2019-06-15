package com.promix.baelui.bind.base

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableList
import java.util.*

abstract class BvBases<T : BvBase<Any>> : BaseObservable() {
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
        if (items.size == 0)
            throw IllegalStateException("Item size is 0.")
        return items[pos]
    }

    @Throws(Exception::class)
    open fun remove(item: T) {
        if (items.size == 0)
            throw IllegalStateException("Item size is 0.")
        items.remove(item)
    }

    open fun clearAll() {
        items.clear()
    }

    protected open fun filterBy(predicate: Prediction<T>) {
        if (tempItems.size == 0)
            tempItems.addAll(items)
        val filtered = items.filter {
            predicate.test(it)
        }
        items.clear()
        items.addAll(filtered)
    }

    protected open fun sortBy(comparator: Comparator<T>) {
        items.sortWith(comparator)
    }

    interface Prediction<T> {
        fun test(t: T): Boolean
    }
}