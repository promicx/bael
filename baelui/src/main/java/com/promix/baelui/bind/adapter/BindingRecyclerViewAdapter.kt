package com.promix.baelui.bind.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableList
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.promix.baelui.bind.binder.core.ItemBinder
import com.promix.baelui.callback.ClickHandler
import com.promix.baelui.callback.LongClickHandler
import java.lang.ref.WeakReference

open class BindingRecyclerViewAdapter<T>(private val itemBinder: ItemBinder<T>, items: Collection<T>?) :
    RecyclerView.Adapter<BindingRecyclerViewAdapter.ViewHolder>(), View.OnClickListener, View.OnLongClickListener {
    private val onListChangedCallback: WeakReferenceOnListChangedCallback<T>
    protected var items: ObservableList<T>? = null
    private var inflater: LayoutInflater? = null
    private var clickHandler: ClickHandler<T>? = null
    private var longClickHandler: LongClickHandler<T>? = null

    init {
        this.onListChangedCallback = WeakReferenceOnListChangedCallback(this)
        setItems(items)
    }

    fun setItems(items: Collection<T>?) {
        if (this.items === items) {
            return
        }

        if (this.items != null) {
            notifyItemRangeRemoved(0, this.items!!.size)
            this.items?.removeOnListChangedCallback(onListChangedCallback)
        }

        when {
            items is ObservableList<*> -> {
                this.items = items as ObservableList<T>?
                this.items?.addOnListChangedCallback(onListChangedCallback)
                notifyItemRangeInserted(0, this.items!!.size)
            }
            items != null -> {
                this.items = ObservableArrayList()
                this.items?.addOnListChangedCallback(onListChangedCallback)
                this.items?.addAll(items)
            }
            else -> this.items = null
        }
        notifyDataSetChanged()
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        items?.removeOnListChangedCallback(onListChangedCallback)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, layoutId: Int): ViewHolder {
        if (inflater == null) {
            inflater = LayoutInflater.from(viewGroup.context)
        }

        val binding = DataBindingUtil.inflate<ViewDataBinding>(inflater!!, layoutId, viewGroup, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val item = items!![position]

        viewHolder.binding.setVariable(itemBinder.getBindingVariable(item), item)
        viewHolder.binding.root.setTag(ITEM_MODEL, item)
        if (clickHandler != null)
            viewHolder.binding.root.setOnClickListener(this)
        if (longClickHandler != null)
            viewHolder.binding.root.setOnLongClickListener(this)
        viewHolder.binding.executePendingBindings()
    }

    override fun getItemViewType(position: Int): Int {
        return itemBinder.getLayoutRes(items!![position])
    }

    override fun getItemCount(): Int {
        return items?.size ?: 0
    }

    @Suppress("UNCHECKED_CAST")
    override fun onClick(v: View) {
        if (clickHandler != null) {
            val item = v.getTag(ITEM_MODEL) as T
            clickHandler?.onClick(v, item)
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun onLongClick(v: View): Boolean {
        if (longClickHandler != null) {
            val item = v.getTag(ITEM_MODEL) as T
            return longClickHandler!!.onLongClick(v, item)
        }
        return false
    }

    fun setClickHandler(clickHandler: ClickHandler<T>) {
        this.clickHandler = clickHandler
    }

    fun setLongClickHandler(clickHandler: LongClickHandler<T>) {
        this.longClickHandler = clickHandler
    }

    class ViewHolder internal constructor(internal val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root)

    private class WeakReferenceOnListChangedCallback<T>(bindingRecyclerViewAdapter: BindingRecyclerViewAdapter<T>) :
        ObservableList.OnListChangedCallback<ObservableList<T>>() {

        private val adapterReference: WeakReference<BindingRecyclerViewAdapter<T>> = WeakReference(bindingRecyclerViewAdapter)

        override fun onChanged(sender: ObservableList<T>) {
            try {
                val adapter = adapterReference.get()
                adapter?.notifyDataSetChanged()
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }

        override fun onItemRangeChanged(sender: ObservableList<T>, positionStart: Int, itemCount: Int) {
            try {
                val adapter = adapterReference.get()
                adapter?.notifyItemRangeChanged(positionStart, itemCount)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        override fun onItemRangeInserted(sender: ObservableList<T>, positionStart: Int, itemCount: Int) {
            val adapter = adapterReference.get()
            adapter?.notifyItemRangeInserted(positionStart, itemCount)
        }

        override fun onItemRangeMoved(sender: ObservableList<T>, fromPosition: Int, toPosition: Int, itemCount: Int) {
            val adapter = adapterReference.get()
            adapter?.notifyItemMoved(fromPosition, toPosition)
        }

        override fun onItemRangeRemoved(sender: ObservableList<T>, positionStart: Int, itemCount: Int) {
            val adapter = adapterReference.get()
            adapter?.notifyItemRangeRemoved(positionStart, itemCount)
        }
    }

    companion object {
        private const val ITEM_MODEL = -124
    }
}
