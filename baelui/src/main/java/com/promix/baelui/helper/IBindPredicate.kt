package com.promix.baelui.helper

interface IBindPredicate<T> {
    fun condition(item: T): Boolean
}