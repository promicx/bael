package com.promix.baelui.bind.binding

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView
import com.promix.baelui.bind.adapter.BindingRecyclerViewAdapter
import com.promix.baelui.bind.binder.ItemBinder
import com.promix.baelui.callback.ClickHandler
import com.promix.baelui.callback.LongClickHandler

object RecyclerViewBindings {
    private const val KEY_ITEMS = -123
    private const val KEY_CLICK_HANDLER = -124
    private const val KEY_LONG_CLICK_HANDLER = -125

    @Suppress("UNCHECKED_CAST")
    @BindingAdapter("items")
    fun <T> setItems(recyclerView: RecyclerView, items: Collection<T>) {
        val adapter = recyclerView.adapter as BindingRecyclerViewAdapter<T>?
        if (adapter != null) {
            adapter.setItems(items)
        } else {
            recyclerView.setTag(KEY_ITEMS, items)
        }
    }

    @Suppress("UNCHECKED_CAST")
    @BindingAdapter("clickHandler")
    fun <T> setHandler(recyclerView: RecyclerView, handler: ClickHandler<T>) {
        val adapter = recyclerView.adapter as BindingRecyclerViewAdapter<T>?
        if (adapter != null) {
            adapter.setClickHandler(handler)
        } else {
            recyclerView.setTag(KEY_CLICK_HANDLER, handler)
        }
    }

    @Suppress("UNCHECKED_CAST")
    @BindingAdapter("longClickHandler")
    fun <T> setHandler(recyclerView: RecyclerView, handler: LongClickHandler<T>) {
        val adapter = recyclerView.adapter as BindingRecyclerViewAdapter<T>?
        if (adapter != null) {
            adapter.setLongClickHandler(handler)
        } else {
            recyclerView.setTag(KEY_LONG_CLICK_HANDLER, handler)
        }
    }

    @BindingAdapter(value = ["hasFixedSize", "nestedScroll"], requireAll = true)
    fun <T> setOption(recyclerView: RecyclerView, hasFixed: Boolean, nested: Boolean) {
        recyclerView.setHasFixedSize(hasFixed)
        recyclerView.isNestedScrollingEnabled = nested
    }

    @Suppress("UNCHECKED_CAST")
    @BindingAdapter("itemViewBinder")
    fun <T> setItemViewBinder(recyclerView: RecyclerView, itemViewMapper: ItemBinder<T>) {
        try {
            val itemTag = recyclerView.getTag(KEY_ITEMS)
            val items = if (itemTag != null)
                itemTag as Collection<T>
            else
                emptyList<T>()
            val clickHandler = recyclerView.getTag(KEY_CLICK_HANDLER)
            val adapter = BindingRecyclerViewAdapter(itemViewMapper, items)
            if (clickHandler != null) {
                adapter.setClickHandler(clickHandler as ClickHandler<T>)
            }

            recyclerView.adapter = adapter
        } catch (e: ClassCastException) {
            e.printStackTrace()
        }
    }

    @BindingAdapter("itemAnimDuration")
    fun setAnimItem(recyclerView: RecyclerView, duration: Int) {
        val itemAnimator = DefaultItemAnimator()
        itemAnimator.addDuration = duration.toLong()
        itemAnimator.removeDuration = duration.toLong()
        recyclerView.itemAnimator = itemAnimator
    }
}
