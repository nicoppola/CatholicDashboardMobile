package org.coppola.catholic

import android.content.Context

object AndroidPlatformContextProvider {
    private var appContext: Context? = null

    val context: Context?
        get() = appContext

    fun setContext(context: Context) {
        appContext = context
    }
}