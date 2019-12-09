package com.prasac.core.ext

import android.app.Dialog
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver

fun Dialog.subscribeDialogToLifecycle(
    lifecycle: Lifecycle,
    dismissHandler: (() -> Unit)? = null
) {
    val lifecycleObserver = LifecycleEventObserver { _, event ->
        if (event == Lifecycle.Event.ON_PAUSE && isShowing) {
            dismiss()
            dismissHandler?.invoke()
        }
    }
    lifecycle.addObserver(lifecycleObserver)
    setOnDismissListener {
        lifecycle.removeObserver(lifecycleObserver)
    }
}