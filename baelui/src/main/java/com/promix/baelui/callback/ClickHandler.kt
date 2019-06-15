package com.promix.baelui.callback

import android.view.View

interface ClickHandler<T> {
    fun onClick(itemView: View, viewModel: T)
}
