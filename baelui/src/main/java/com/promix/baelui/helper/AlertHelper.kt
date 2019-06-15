package com.promix.baelui.helper

import android.content.Context
import android.content.DialogInterface
import android.content.res.Configuration
import android.util.TypedValue
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import androidx.annotation.ArrayRes
import androidx.annotation.FloatRange
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.promix.baelui.R

class AlertHelper private constructor(
    private val builder: AlertDialog.Builder,
    private val option: AlertOption
) {
    internal class Builder(private val context: Context) {
        private val builder = AlertDialog.Builder(context)
        private val alertOption = AlertOption()

        fun setTitle(title: String): Builder {
            builder.setTitle(title)
            return this
        }

        fun setItems(items: Array<String>, listener: DialogInterface.OnClickListener?): Builder {
            builder.setItems(items, listener)
            return this
        }

        fun setSingleChoiceItems(
            items: Array<String>,
            selectedIndex: Int,
            listener: DialogInterface.OnClickListener?
        ): Builder {
            builder.setSingleChoiceItems(items, selectedIndex, listener)
            return this
        }

        fun setSingleChoiceItems(
            @ArrayRes itemsId: Int,
            selectedIndex: Int,
            listener: DialogInterface.OnClickListener?
        ): Builder {
            builder.setSingleChoiceItems(itemsId, selectedIndex, listener)
            return this
        }

        fun setMultipleChoiceItems(
            items: Array<String>,
            checkedItems: BooleanArray,
            listener: DialogInterface.OnMultiChoiceClickListener?
        ): Builder {
            builder.setMultiChoiceItems(items, checkedItems, listener)
            return this
        }

        fun setView(@LayoutRes layoutId: Int): Builder {
            builder.setView(layoutId)
            return this
        }

        fun setView(v: View): Builder {
            builder.setView(v)
            return this
        }

        fun setMessage(message: String): Builder {
            builder.setMessage(message)
            return this
        }

        fun setPositiveButton(label: String, listener: DialogInterface.OnClickListener? = null): Builder {
            builder.setPositiveButton(label, listener)
            return this
        }

        fun setNegativeButton(label: String, listener: DialogInterface.OnClickListener? = null): Builder {
            builder.setNegativeButton(label, listener)
            return this
        }

        fun setTopPanelColor(color: Int): Builder {
            alertOption.topPanelColor = color
            return this
        }

        fun setTopPanelColorRes(colorRes: Int): Builder {
            alertOption.topPanelColor = ContextCompat.getColor(context, colorRes)
            return this
        }

        fun setTitleColor(color: Int): Builder {
            alertOption.titleColor = color
            return this
        }

        fun setTitleColorRes(colorRes: Int): Builder {
            alertOption.titleColor = ContextCompat.getColor(context, colorRes)
            return this
        }

        fun setPercentWidth(@FloatRange(from = 0.0, to = 1.0) w: Float): Builder {
            alertOption.percentWidth = w
            return this
        }

        fun setPercentHeight(@FloatRange(from = 0.0, to = 1.0) h: Float): Builder {
            alertOption.percentHeight = h
            return this
        }

        fun create(): AlertHelper {
            return AlertHelper(builder, alertOption)
        }

        fun show() {
            AlertHelper(builder, alertOption).show()
        }
    }

    private fun toPx(context: Context, dp: Float): Int {
        val res = context.resources
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, res.displayMetrics).toInt()
    }

    private fun toWidthPercent(context: Context, @FloatRange(from = 0.0, to = 1.0) p: Float): Int {
        return (context.resources.displayMetrics.widthPixels * p).toInt()
    }

    private fun toHeightPercent(context: Context, @FloatRange(from = 0.0, to = 1.0) p: Float): Int {
        return (context.resources.displayMetrics.heightPixels * p).toInt()
    }

    private fun isLandscape(context: Context): Boolean {
        return context.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    }

    fun show() {
        val context = builder.context

        val alert = builder.create()
        alert.show()

        if (option.titleColor != 0)
            alert.findViewById<TextView>(R.id.alertTitle)?.setTextColor(option.titleColor)
        if (option.topPanelColor != 0)
            alert.findViewById<View>(R.id.topPanel)?.setBackgroundColor(option.topPanelColor)

        val lp = WindowManager.LayoutParams()
        lp.copyFrom(alert.window?.attributes)
        if (option.percentWidth > 0.0)
            lp.width = toWidthPercent(context, option.percentWidth)
        else {
            if (!isLandscape(context))
                lp.width = toPx(context, 340f)
        }

        if (option.percentHeight > 0.0)
            lp.height = toHeightPercent(context, option.percentHeight)
        alert.window?.attributes = lp
    }

    internal class AlertOption {
        @FloatRange(from = 0.0, to = 1.0)
        var percentWidth = 0.0f
        @FloatRange(from = 0.0, to = 1.0)
        var percentHeight = 0.0f

        var titleColor = 0
        var topPanelColor = 0
    }
}