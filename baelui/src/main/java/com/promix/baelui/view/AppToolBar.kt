package com.promix.baelui.view

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.TintTypedArray
import androidx.appcompat.widget.Toolbar
import com.promix.baelui.R

@SuppressLint("RestrictedApi")
class AppToolBar : Toolbar {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        ini(attrs, 0)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        ini(attrs, defStyleAttr)
    }

    private fun ini(attrs: AttributeSet?, defStyleAttr: Int) {
        val a = TintTypedArray.obtainStyledAttributes(
            context, attrs,
            R.styleable.AppToolBar, defStyleAttr, 0
        )
        try {
            if (a.hasValue(R.styleable.AppToolBar_menu)) {
                this.inflateMenu(a.getResourceId(R.styleable.AppToolBar_menu, 0))
            }
        } finally {
            a.recycle()
        }
    }

    override fun inflateMenu(resId: Int) {
        menu.clear()
        super.inflateMenu(resId)
    }
}