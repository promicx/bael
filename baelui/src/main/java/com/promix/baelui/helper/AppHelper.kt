package com.promix.baelui.helper

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.pm.PackageInfo
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Environment
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextUtils
import android.text.style.ForegroundColorSpan
import android.util.TypedValue
import android.view.inputmethod.InputMethodManager
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import timber.log.Timber
import java.io.File
import java.util.*
import kotlin.math.abs

class AppHelper private constructor(private val app: Application) {
    val appContext: Context
        get() = app.applicationContext

    private val packageInfo: PackageInfo
        get() {
            return app.packageManager.getPackageInfo(app.packageName, 0)
        }

    val versionName: String?
        get() = packageInfo.versionName

    val packageName: String
        get() = packageInfo.packageName


    fun hasInternet(): Boolean {
        val cmd = "ping -c 1 google.com"
        return try {
            Runtime.getRuntime().exec(cmd).waitFor() == 0
        } catch (e: Exception) {
            Timber.e(e)
            false
        }
    }

    fun getSizePx(id: Int): Int {
        return app.resources.getDimensionPixelSize(id)
    }

    fun getSize(id: Int): Float {
        return app.resources.getDimension(id)
    }

    fun toSize(dp: Float): Int {
        val px =
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, app.resources.displayMetrics)
        return px.toInt()
    }

    fun toTextSize(dp: Float): Int {
        val px =
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, dp, app.resources.displayMetrics)
        return px.toInt()
    }

    fun toDP(px: Float): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_PX,
            px,
            app.resources.displayMetrics
        )
    }

    fun getResColor(colorId: Int): Int {
        return ContextCompat.getColor(app, colorId)
    }

    fun getString(@StringRes resId: Int): String {
        return app.getString(resId)
    }

    fun getResColor(colorName: String?): Int {
        if (colorName.isNullOrEmpty()) return Color.TRANSPARENT
        return ContextCompat.getColor(
            app,
            app.resources.getIdentifier(colorName, "color", app.packageName)
        )
    }

    fun getViewId(idString: String): Int {
        return app.resources.getIdentifier(idString, "id", app.packageName)
    }

    fun makeDirInPicture(albumName: String): File {
        val file = File(
            app.getExternalFilesDir(
                Environment.DIRECTORY_PICTURES
            ), albumName
        )
        if (!file.mkdirs()) {
            Timber.e("Directory not created")
        }
        return file
    }

    companion object {
        private var INSTANCE: AppHelper? = null
        fun install(app: Application) {
            INSTANCE = AppHelper(app)
        }

        fun getInstance(): AppHelper {
            checkNotNull(INSTANCE) {
                "Need to install in application."
            }
            return INSTANCE!!
        }

        fun hideSoftKeyboard(activity: Activity) {
            val inputMethodManager =
                activity.getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager

            if (activity.currentFocus != null && activity.currentFocus!!.windowToken != null)
                inputMethodManager.hideSoftInputFromWindow(activity.currentFocus!!.windowToken, 0)
        }

        fun getSpanTextColor(s: String, c: Int): SpannableStringBuilder {
            val builder = SpannableStringBuilder()
            builder.append(s)
            builder.setSpan(ForegroundColorSpan(c), 0, s.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            return builder
        }

        fun stringToColor(uniqueId: String?): Int {
            if (uniqueId.isNullOrEmpty()) return Color.DKGRAY

            val red = uniqueId.hashCode() % 80
            val green = uniqueId.hashCode() % 150
            val blue = uniqueId.hashCode() % 100

            return Color.argb(230, abs(red), abs(green), abs(blue))
        }

        fun generateAvatar(
            pName: String,
            bgColor: Int,
            textColor: Int,
            textSize: Int,
            size: Int
        ): Bitmap {
            var name = pName
            if (TextUtils.isEmpty(name))
                name = "?"
            name = name.toUpperCase(Locale.US)
            val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            val paint2 = Paint()
            paint2.color = bgColor

            val rect = Rect(0, 0, bitmap.width, bitmap.height)
            val rectF = RectF(rect)
            canvas.drawOval(rectF, paint2)

            val paint = Paint()
            paint.color = textColor
            paint.textSize = textSize.toFloat()
            paint.isFakeBoldText = true
            paint.textAlign = Paint.Align.CENTER
            paint.textScaleX = 1f
            canvas.drawText(
                name, (canvas.width / 2).toFloat(),
                canvas.height / 2 - (paint.descent() + paint.ascent()) / 2, paint
            )

            return bitmap
        }

        fun drawableToBitmap(drawable: Drawable): Bitmap? {
            if (drawable is BitmapDrawable) {
                return drawable.bitmap
            }
            var width = drawable.intrinsicWidth
            width = if (width > 0) width else 1
            var height = drawable.intrinsicHeight
            height = if (height > 0) height else 1
            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            drawable.setBounds(0, 0, canvas.width, canvas.height)
            drawable.draw(canvas)
            return bitmap
        }
    }
}

val appHelper = AppHelper.getInstance()