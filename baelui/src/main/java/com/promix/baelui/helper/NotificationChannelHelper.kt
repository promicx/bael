package com.promix.baelui.helper

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build

class NotificationChannelHelper private constructor(internal val builder: Builder) {
    private val manager: NotificationManager

    init {
        manager =
            builder.context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    fun notify(notifyId: Int, notification: Notification) {
        manager.notify(notifyId, notification)
    }

    fun cancel(notifyId: Int) {
        manager.cancel(notifyId)
    }

    fun cancelAll() {
        manager.cancelAll()
    }

    private fun install() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return
        builder.channels.forEach {
            manager.createNotificationChannel(it)
        }
    }

    private fun delete() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return
        builder.channels.forEach {
            manager.deleteNotificationChannel(it.id)
        }
    }

    internal class Builder(internal val context: Context) {
        internal val channels = mutableListOf<NotificationChannel>()

        fun addChannel(channel: NotificationChannel) {
            channels.add(channel)
        }

        fun build(): NotificationChannelHelper {
            return NotificationChannelHelper(this)
        }
    }

    companion object {
        var INSTANCE: NotificationChannelHelper? = null
        fun create(context: Context, channels: Set<NotificationChannel>?) {
            if (INSTANCE != null || Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return
            val helper = Builder(context)
            channels?.forEach {
                helper.addChannel(it)
            }
            INSTANCE = helper.build()
            INSTANCE?.install()
        }

        fun destroy() {
            INSTANCE?.delete()
        }

        fun cancelAll() {
            INSTANCE?.cancelAll()
        }
    }
}

val notifyManager: NotificationChannelHelper?
    get() = NotificationChannelHelper.INSTANCE