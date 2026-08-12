package com.calcvault.app

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager

data class AppInfo(val label: String, val packageName: String)

object InstalledApps {
    fun refresh(target: MutableList<AppInfo>, context: Context) {
        target.clear()
        val pm = context.packageManager
        val flags = PackageManager.GET_META_DATA
        val main = PackageManager.GET_META_DATA or PackageManager.MATCH_ALL
        @Suppress("DEPRECATION")
        val apps = pm.getInstalledApplications(flags)
        for (info: ApplicationInfo in apps) {
            if ((info.flags and ApplicationInfo.FLAG_SYSTEM) != 0 &&
                (info.flags and ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) == 0 &&
                info.packageName != context.packageName) continue
            val label = pm.getApplicationLabel(info).toString()
            target.add(AppInfo(label, info.packageName))
        }
        target.sortBy { it.label.lowercase() }
    }
}
