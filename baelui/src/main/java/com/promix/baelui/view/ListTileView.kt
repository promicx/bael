package com.promix.baelui.view

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.widget.TextViewCompat
import com.google.android.material.card.MaterialCardView
import com.promix.baelui.R
import kotlinx.android.synthetic.main.layout_list_tile.view.*

class ListTileView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : MaterialCardView(context, attrs, defStyleAttr) {
    private val rootContent = View.inflate(context, R.layout.layout_list_tile, null)

    init {
        this.addView(rootContent)
        this.cardElevation = 0f
        this.radius = 0f
        this.setCardBackgroundColor(Color.TRANSPARENT)

        val a = context.obtainStyledAttributes(attrs, R.styleable.ListTileView, defStyleAttr, 0)

        if (a.hasValue(R.styleable.ListTileView_lstIconStart))
            setIconStart(a.getResourceId(R.styleable.ListTileView_lstIconStart, -1))
        if (a.hasValue(R.styleable.ListTileView_lstIconEnd))
            setIconEnd(a.getResourceId(R.styleable.ListTileView_lstIconEnd, -1))
        if (a.hasValue(R.styleable.ListTileView_lstTitle)) {
            val s = a.getResourceId(R.styleable.ListTileView_lstTitle, -1)
            if (s != -1) {
                setTitle(s)
            } else {
                setTitle(a.getString(R.styleable.ListTileView_lstTitle))
            }
        }

        if (a.hasValue(R.styleable.ListTileView_lstSubTitle)) {
            val s = a.getResourceId(R.styleable.ListTileView_lstSubTitle, -1)
            if (s != -1) {
                setSubTitle(s)
            } else {
                setSubTitle(a.getString(R.styleable.ListTileView_lstSubTitle))
            }
        }

        if (a.hasValue(R.styleable.ListTileView_lstTitleAppearance)) {
            val ap = a.getResourceId(R.styleable.ListTileView_lstTitleAppearance, -1)
            if (ap != -1) {
                TextViewCompat.setTextAppearance(rootContent.lstTitle, ap)
            }
        }

        if (a.hasValue(R.styleable.ListTileView_lstSubTitleAppearance)) {
            val ap = a.getResourceId(R.styleable.ListTileView_lstSubTitleAppearance, -1)
            if (ap != -1) {
                TextViewCompat.setTextAppearance(rootContent.lstSubTitle, ap)
            }
        }

        if (a.hasValue(R.styleable.ListTileView_lstTitleColor)) {
            setTitleColor(a.getColor(R.styleable.ListTileView_lstTitleColor, Color.LTGRAY))
        }

        if (a.hasValue(R.styleable.ListTileView_lstSubTitleColor)) {
            setSubTitleColor(a.getColor(R.styleable.ListTileView_lstSubTitleColor, Color.LTGRAY))
        }

        a.recycle()
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        check(childCount <= 1) { "Cannot have a child" }
    }

    fun setTitle(text: String?) {
        rootContent?.lstTitle?.text = text
    }

    fun setTitle(@StringRes textRes: Int) {
        if (textRes == -1) return
        rootContent?.lstTitle?.setText(textRes)
    }

    fun setTitleColor(color: Int) {
        rootContent?.lstTitle?.setTextColor(color)
    }

    fun setSubTitle(text: String?) {
        if (text.isNullOrEmpty()) {
            rootContent?.lstSubTitle?.visibility = View.GONE
        } else {
            rootContent?.lstSubTitle?.visibility = View.VISIBLE
        }
        rootContent?.lstSubTitle?.text = text
    }

    fun setSubTitle(@StringRes textRes: Int) {
        if (textRes != -1) {
            rootContent?.lstSubTitle?.setText(textRes)
            rootContent?.lstSubTitle?.visibility = View.VISIBLE
        } else {
            rootContent?.lstSubTitle?.visibility = View.GONE
        }
    }

    fun setSubTitleColor(color: Int) {
        rootContent?.lstSubTitle?.setTextColor(color)
    }

    fun setIconStart(@DrawableRes iconRes: Int) {
        if (iconRes != -1) {
            rootContent?.lstIconStart?.visibility = View.VISIBLE
            rootContent?.lstIconStart?.setImageResource(iconRes)
        } else {
            rootContent?.lstIconStart?.visibility = View.GONE
        }
    }

    fun setIconEnd(@DrawableRes iconRes: Int) {
        if (iconRes != -1) {
            rootContent?.lstIconEnd?.setImageResource(iconRes)
            rootContent?.lstIconEnd?.visibility = View.VISIBLE
        } else {
            rootContent?.lstIconEnd?.visibility = View.VISIBLE
        }
    }
}