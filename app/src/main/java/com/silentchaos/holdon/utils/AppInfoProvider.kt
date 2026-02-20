package com.silentchaos.holdon.utils

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build

object AppInfoProvider {

    data class AppVersion(
        val versionName: String,
        val versionCode: Long
    ) {
        fun formatted(): String {
            return "v$versionName ($versionCode)"
        }
    }

    fun getAppVersion(context: Context): AppVersion {
        return try {

            val packageInfo = context.packageManager.getPackageInfo(
                context.packageName,
                0
            )

            val versionName = packageInfo.versionName ?: "1.0"

            val versionCode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                packageInfo.longVersionCode
            } else {
                @Suppress("DEPRECATION")
                packageInfo.versionCode.toLong()
            }

            AppVersion(
                versionName = versionName,
                versionCode = versionCode
            )

        } catch (e: PackageManager.NameNotFoundException) {

            AppVersion(
                versionName = "1.0",
                versionCode = 1
            )
        }
    }
}
