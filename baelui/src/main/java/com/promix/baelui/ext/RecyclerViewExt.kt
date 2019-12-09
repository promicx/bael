package com.promix.baelui.ext

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.promix.baelui.bind.adapter.BindingRecyclerViewAdapter
import com.promix.baelui.bind.base.VmBase
import com.promix.baelui.bind.binder.core.CompositeItemBinder
import com.promix.baelui.bind.binder.core.ConditionalDataBinder
import com.promix.baelui.bind.binder.core.ItemBinder
import com.promix.baelui.callback.ClickHandler
import com.promix.baelui.callback.LongClickHandler

private const val KEY_ITEMS = -123
private const val KEY_CLICK_HANDLER = -124
private const val KEY_LONG_CLICK_HANDLER = -125

@Suppress("UNCHECKED_CAST")
@BindingAdapter("bindViewBinder")
fun <T : VmBase<*>> RecyclerView.bindView(
    vararg itemBinders: ConditionalDataBinder<T>
) {
    val items = getTag(KEY_ITEMS)
    val ad: BindingRecyclerViewAdapter<T>
    ad = if (items == null) {
        BindingRecyclerViewAdapter(CompositeItemBinder(*itemBinders), emptyList())
    } else {
        BindingRecyclerViewAdapter(CompositeItemBinder(*itemBinders), items as Collection<T>)
    }

    var handler = getTag(KEY_CLICK_HANDLER)
    if (handler != null) {
        ad.setClickHandler(handler as ClickHandler<T>)
    }

    handler = getTag(KEY_LONG_CLICK_HANDLER)
    if (handler != null)
        ad.setLongClickHandler(handler as LongClickHandler<T>)
    adapter = ad
}

@Suppress("UNCHECKED_CAST")
@BindingAdapter("bindViewBinder")
fun <T : VmBase<*>> RecyclerView.bindView(
    itemBinder: ItemBinder<T>
) {
    val items = getTag(KEY_ITEMS)
    val ad: BindingRecyclerViewAdapter<T>
    ad = if (items == null) {
        BindingRecyclerViewAdapter(itemBinder, emptyList())
    } else {
        BindingRecyclerViewAdapter(itemBinder, items as Collection<T>)
    }

    var handler = getTag(KEY_CLICK_HANDLER)
    if (handler != null) {
        ad.setClickHandler(handler as ClickHandler<T>)
    }

    handler = getTag(KEY_LONG_CLICK_HANDLER)
    if (handler != null)
        ad.setLongClickHandler(handler as LongClickHandler<T>)
    adapter = ad
}

@Suppress("UNCHECKED_CAST")
@BindingAdapter("bindItems")
fun <T : VmBase<*>> RecyclerView.bindItems(
    items: Collection<T>?
) {
    if (adapter == null || adapter !is BindingRecyclerViewAdapter<*>) {
        setTag(KEY_ITEMS, items)
    } else {
        (adapter as BindingRecyclerViewAdapter<T>).setItems(items)
    }
}

@Suppress("UNCHECKED_CAST")
@BindingAdapter("bindHandler")
fun <T : VmBase<*>> RecyclerView.bindHandler(
    clickHandler: ClickHandler<T>
) {
    if (adapter == null || adapter !is BindingRecyclerViewAdapter<*>) {
        setTag(KEY_CLICK_HANDLER, clickHandler)
    } else {
        (adapter as BindingRecyclerViewAdapter<T>).setClickHandler(clickHandler)
    }
}

@Suppress("UNCHECKED_CAST")
@BindingAdapter("bindHandler")
fun <T : VmBase<*>> RecyclerView.bindHandler(
    longClickHandler: LongClickHandler<T>
) {
    if (adapter == null || adapter !is BindingRecyclerViewAdapter<*>) {
        setTag(KEY_LONG_CLICK_HANDLER, longClickHandler)
    } else {
        (adapter as BindingRecyclerViewAdapter<T>).setLongClickHandler(longClickHandler)
    }
}