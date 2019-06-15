package com.promix.baelui.callback

import android.view.View

interface LongClickHandler<T> {
    fun onLongClick(itemView: View, viewModel: T): Boolean
}
