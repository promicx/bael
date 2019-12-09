package com.promix.baelui.helper

import android.Manifest.permission.*
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.BaseMultiplePermissionsListener
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import java.util.*

class PermissionHelper private constructor(private val act: Activity) {

    fun allowAllPermission(onResult: (Boolean) -> Unit) {
        Dexter.withActivity(act)
            .withPermissions(
                READ_EXTERNAL_STORAGE,
                WRITE_EXTERNAL_STORAGE,
                RECORD_AUDIO,
                CAMERA
            )
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    onResult(report.areAllPermissionsGranted())
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: List<PermissionRequest>,
                    token: PermissionToken
                ) {
                    token.continuePermissionRequest()
                }
            }).check()
    }

    fun askLocation(onResult: (Boolean) -> Unit) {
        Dexter.withActivity(act)
            .withPermissions(setOf(ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION))
            .withListener(object : BaseMultiplePermissionsListener() {
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    super.onPermissionsChecked(report)
                    onResult(report?.areAllPermissionsGranted() == true)
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: MutableList<PermissionRequest>?,
                    token: PermissionToken?
                ) {
                    super.onPermissionRationaleShouldBeShown(permissions, token)
                    token?.cancelPermissionRequest()
                    onResult(false)
                }
            }).check()
    }

    fun askCall(onResult: (Boolean) -> Unit) {
        Dexter.withActivity(act)
            .withPermissions(setOf(CAMERA, RECORD_AUDIO))
            .withListener(object : BaseMultiplePermissionsListener() {
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    super.onPermissionsChecked(report)
                    onResult(report?.areAllPermissionsGranted() == true)
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: MutableList<PermissionRequest>?,
                    token: PermissionToken?
                ) {
                    super.onPermissionRationaleShouldBeShown(permissions, token)
                    token?.cancelPermissionRequest()
                    onResult(false)
                }
            }).check()
    }

    fun askStorage(vararg perms: String, onResult: (Boolean) -> Unit) {
        val permissions = setOf(WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE)
        permissions.plus(perms)
        Dexter.withActivity(act)
            .withPermissions(permissions)
            .withListener(object : BaseMultiplePermissionsListener() {
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    super.onPermissionsChecked(report)
                    onResult(report?.areAllPermissionsGranted() == true)
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: MutableList<PermissionRequest>?,
                    token: PermissionToken?
                ) {
                    super.onPermissionRationaleShouldBeShown(permissions, token)
                    token?.cancelPermissionRequest()
                    onResult(false)
                }
            }).check()
    }

    companion object {
        const val ASK_PERM_OVERLAY = 0x100
        private val sInstance = IdentityHashMap<Activity, PermissionHelper>()

        fun askOverlay(act: Activity, onResult: (Boolean) -> Unit) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                when {
                    Settings.canDrawOverlays(act) -> onResult(true)
                    else -> {
                        onResult(false)
                        val intent = Intent(
                            Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                            Uri.parse("package:" + act.packageName)
                        )
                        act.startActivityForResult(intent, ASK_PERM_OVERLAY)
                    }
                }
            } else {
                onResult(true)
            }
        }

        fun install(act: Activity) {
            synchronized(sInstance) {
                if (!sInstance.containsKey(act)) {
                    sInstance[act] =
                        PermissionHelper(act)
                }
            }
        }

        fun getInstance(act: Activity): PermissionHelper {
            if (sInstance.isEmpty()) {
                throw NullPointerException("Please install AppHelper in your application before use it.")
            }
            return sInstance[act]!!
        }

        fun getInstance(): PermissionHelper {
            if (sInstance.isEmpty()) {
                throw NullPointerException("Please install AppHelper in your application before use it.")
            }
            return sInstance.values.first()
        }
    }
}

val pHelper: PermissionHelper
    get() = PermissionHelper.getInstance()