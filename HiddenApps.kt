package com.calcvault.app

import android.content.Context

object HiddenApps {
    private const val PREF = "hidden_apps"
    private const val KEY = "pkgs"

    fun getHidden(ctx: Context): List<String> {
        val p = ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE)
        val s = p.getString(KEY, "") ?: ""
        if (s.isEmpty()) return emptyList()
        return s.split(",").filter { it.isNotBlank() }
    }

    fun save(ctx: Context, list: List<String>) {
        ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE).edit()
            .putString(KEY, list.joinToString(","))
            .apply()
    }
}
