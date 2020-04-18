package com.promix.baelui.bind.adapter

import android.os.Handler
import android.os.HandlerThread
import android.os.Process
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableList
import androidx.databinding.ViewDataBinding
import androidx.paging.PagedList
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.promix.baelui.bind.base.VmBase
import com.promix.baelui.bind.binder.core.ItemBinder
import com.promix.baelui.callback.ClickHandler
import com.promix.baelui.callback.LongClickHandler
import com.promix.baelui.helper.IBindPredicate
import java.lang.ref.WeakReference
import java.util.*

class BindingPagingRecyclerViewAdapter<T : VmBase<*>>(
    private val itemBinder: ItemBinder<T>
) : PagedListAdapter<T, BindingPagingRecyclerViewAdapter.ViewHolder>(compareWith()),
    View.OnClickListener,
    View.OnLongClickListener {

    private var items: PagedList<T>? = null
    private var backupItems: PagedList<T>? = null

    private var filterResults: PagedList<T>? = null
    private var inflater: LayoutInflater? = null
    private var clickHandler: ClickHandler<T>? = null
    private var longClickHandler: LongClickHandler<T>? = null

    private var reqHandler: Handler? = null
    private var resHandler = Handler()

    private val filterRunnable = Runnable {
        filterResults?.let {
            synchronized(it) {
                resHandler.post {
                    submitList(it)
                    filterResults = null
                }

                reqHandler?.postDelayed(finnishRunnable, 3000)
            }
        }
    }

    private val finnishRunnable = Runnable {
        reqHandler?.looper?.quit()
        reqHandler = null
    }

    override fun getItemId(position: Int): Long {
        val item = items?.get(position) ?: return super.getItemId(position)
        return item.getUnique()
    }

    fun filterBy(predicate: IBindPredicate<T>) {
        if (backupItems == null)
            backupItems = items

        backupItems?.apply {
            synchronized(this) {
                if (reqHandler == null) {
                    val thread = HandlerThread(THREAD_NAME, Process.THREAD_PRIORITY_BACKGROUND)
                    thread.start()
                    reqHandler = Handler(thread.looper)
                }
                filterResults = this.filter { predicate.condition(it) } as? PagedList<T>

                reqHandler?.removeCallbacks(filterRunnable)
                reqHandler?.removeCallbacks(finnishRunnable)
                reqHandler?.postDelayed(filterRunnable, 300)
            }
        }
    }

    fun resetFilter() {
        if (backupItems == null) return
        submitList(backupItems)
    }

    fun sortBy(comparator: Comparator<T>? = null) {
        if (comparator == null) {
            return
        }

        items?.apply {
            synchronized(this) {
                if (reqHandler == null) {
                    val thread = HandlerThread(THREAD_NAME, Process.THREAD_PRIORITY_BACKGROUND)
                    thread.start()
                    reqHandler = Handler(thread.looper)
                }

                reqHandler?.post {
                    resHandler.post {
                        items?.sortWith(comparator)
                        reqHandler?.postDelayed(finnishRunnable, 3000)
                    }
                }
                reqHandler?.removeCallbacks(finnishRunnable)
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, layoutId: Int): ViewHolder {
        if (inflater == null) {
            inflater = LayoutInflater.from(viewGroup.context)
        }

        val binding =
            DataBindingUtil.inflate<ViewDataBinding>(inflater!!, layoutId, viewGroup, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val item = items?.get(position) ?: return

        viewHolder.binding.setVariable(itemBinder.getBindingVariable(item), item)
        viewHolder.binding.root.setTag(ITEM_MODEL, item)
        if (clickHandler != null)
            viewHolder.binding.root.setOnClickListener(this)
        if (longClickHandler != null)
            viewHolder.binding.root.setOnLongClickListener(this)
        viewHolder.binding.executePendingBindings()
    }

    override fun getItemViewType(position: Int): Int {
        val item = items?.get(position) ?: return RecyclerView.INVALID_TYPE
        return itemBinder.getLayoutRes(item)
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

    class ViewHolder internal constructor(internal val binding: ViewDataBinding) :
        RecyclerView.ViewHolder(binding.root)

    companion object {
        private const val ITEM_MODEL = -124
        private const val THREAD_NAME = "FilterThread"

        private fun <T : VmBase<*>> compareWith(): DiffUtil.ItemCallback<T> {
            return object :
                DiffUtil.ItemCallback<T>() {
                override fun areItemsTheSame(
                    oldConcert: T,
                    newConcert: T
                ) = oldConcert.getUnique() == newConcert.getUnique()

                override fun areContentsTheSame(
                    oldConcert: T,
                    newConcert: T
                ) = oldConcert == newConcert
            }
        }
    }
}
