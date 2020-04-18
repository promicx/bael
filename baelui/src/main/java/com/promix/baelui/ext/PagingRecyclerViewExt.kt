package com.promix.baelui.ext

import androidx.databinding.BindingAdapter
import androidx.paging.PagedList
import androidx.recyclerview.widget.RecyclerView
import com.promix.baelui.bind.adapter.BindingPagingRecyclerViewAdapter
import com.promix.baelui.bind.adapter.BindingRecyclerViewAdapter
import com.promix.baelui.bind.base.VmBase
import com.promix.baelui.bind.binder.core.CompositeItemBinder
import com.promix.baelui.bind.binder.core.ConditionalDataBinder
import com.promix.baelui.bind.binder.core.ItemBinder
import com.promix.baelui.callback.ClickHandler
import com.promix.baelui.callback.LongClickHandler
import com.promix.baelui.helper.IBindPredicate

private const val KEY_FILTER = -121
private const val KEY_SORT = -122
private const val KEY_ITEMS = -123
private const val KEY_CLICK_HANDLER = -124
private const val KEY_LONG_CLICK_HANDLER = -125

@Suppress("UNCHECKED_CAST")
@BindingAdapter("bindPangingViewBinder")
fun <T : VmBase<*>> RecyclerView.bindPagingView(
    vararg itemBinders: ConditionalDataBinder<T>
) {
    val items = getTag(KEY_ITEMS)
    val ad: BindingPagingRecyclerViewAdapter<T>
    ad = if (items == null || items !is PagedList<*>) {
        BindingPagingRecyclerViewAdapter(CompositeItemBinder(*itemBinders))
    } else {
        BindingPagingRecyclerViewAdapter(CompositeItemBinder(*itemBinders)).apply { submitList(items as PagedList<T>?) }
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
@BindingAdapter("bindPangingViewBinder")
fun <T : VmBase<*>> RecyclerView.bindPagingView(
    itemBinder: ItemBinder<T>
) {
    val items = getTag(KEY_ITEMS)
    val ad: BindingPagingRecyclerViewAdapter<T>
    ad = if (items == null || items !is PagedList<*>) {
        BindingPagingRecyclerViewAdapter(itemBinder)
    } else {
        BindingPagingRecyclerViewAdapter(itemBinder).apply { submitList(items as PagedList<T>?) }
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
@BindingAdapter("bindSubmitItems","bindCommitCallback",requireAll = false)
fun <T : VmBase<*>> RecyclerView.bindSubmitItems(
    items: PagedList<T>?,
    commitCallback: (() -> Unit)?
) {
    if (adapter == null || adapter !is BindingPagingRecyclerViewAdapter<*>) {
        setTag(KEY_ITEMS, items)
    } else {
        (adapter as BindingPagingRecyclerViewAdapter<T>).submitList(items, commitCallback)
    }
}