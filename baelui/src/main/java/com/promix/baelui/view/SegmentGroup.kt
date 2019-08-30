package com.promix.baelui.view

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Outline
import android.util.AttributeSet
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewOutlineProvider
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.promix.baelui.R

class SegmentGroup @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
    private var mListener: OnSegmentSelectedListener? = null
    private var mSelectedIndex = 0

    private var mBGDefaultColor = Color.TRANSPARENT
    private var mBGSelectedColor = Color.LTGRAY

    private var mFGDefaultColor = Color.GRAY
    private var mFGSelectedColor = Color.GRAY

    private var mCornerRadius = 0f
    private var mLine = 4f

    private val mViewClickListener = OnClickListener { v ->
        mSelectedIndex = indexOfChild(v)
        mListener?.onSegmentSelected(v, mSelectedIndex)
        invalidateView()
    }

    init {
        val a = context.obtainStyledAttributes(attrs, R.styleable.SegmentGroup)

        setCornerRadius(a.getDimension(R.styleable.SegmentGroup_slCornerRadius, 0f))
        setLine(a.getDimension(R.styleable.SegmentGroup_slDivider, 1f))

        setBGDefaultColor(a.getColor(R.styleable.SegmentGroup_slBGDefaultColor, Color.TRANSPARENT))
        setBGSelectedColor(a.getColor(R.styleable.SegmentGroup_slBGSelectedColor, Color.LTGRAY))

        setFGDefaultColor(a.getColor(R.styleable.SegmentGroup_slFGDefaultColor, Color.GRAY))
        setFGSelectedColor(a.getColor(R.styleable.SegmentGroup_slFGSelectedColor, Color.GRAY))

        a.recycle()
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        for (i in 0 until childCount) {
            getChildAt(i).setOnClickListener(mViewClickListener)
        }
        invalidateView()
    }

    private fun invalidateView() {
        for (i in 0 until childCount) {
            val fgColor = if (mSelectedIndex == i) mFGSelectedColor else mFGDefaultColor
            val bgColor = if (mSelectedIndex == i) mBGSelectedColor else mBGDefaultColor
            val child = getChildAt(i)
            when (child) {
                is TextView -> {
                    child.setBackgroundColor(bgColor)
                    child.setTextColor(fgColor)
                }
                is ImageView -> {
                    child.setBackgroundColor(bgColor)
                    child.imageTintList = ColorStateList.valueOf(fgColor)
                }
                else -> throw IllegalStateException("Unsupported type of child's view, only TextView and ImageView")
            }

            if (i > 0) {
                val lp = child.layoutParams as MarginLayoutParams
                lp.marginStart = mLine.toInt()
                child.layoutParams = lp
            }
        }
    }

    fun setBGDefaultColor(color: Int) {
        mBGDefaultColor = color
        invalidateView()
    }

    fun setFGDefaultColor(color: Int) {
        mFGDefaultColor = color
        invalidateView()
    }

    fun setBGSelectedColor(color: Int) {
        mBGSelectedColor = color
        invalidateView()
    }

    fun setFGSelectedColor(color: Int) {
        mFGSelectedColor = color
        invalidateView()
    }

    fun setCornerRadius(radius: Float) {
        mCornerRadius = radius
        clipRound(this, radius)
        invalidateView()
    }

    fun setLine(line: Float) {
        mLine = line
        setPadding(mLine.toInt(), mLine.toInt(), mLine.toInt(), mLine.toInt())
        invalidateView()
    }

    private fun clipRound(view: View?, radius: Float) {
        view?.outlineProvider = object : ViewOutlineProvider() {
            override fun getOutline(view: View?, outline: Outline?) {
                outline?.setRoundRect(0, 0, view?.width ?: 0, view?.height ?: 0, radius)
                view?.clipToOutline = true
            }
        }
    }

    fun setOnSegmentSelectedListener(listener: OnSegmentSelectedListener) {
        if (mListener == null)
            listener.onSegmentSelected(getChildAt(mSelectedIndex), mSelectedIndex)
        mListener = listener
    }

    interface OnSegmentSelectedListener {
        fun onSegmentSelected(v: View?, selectedIndex: Int)
    }
}