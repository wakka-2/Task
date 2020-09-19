package com.task.app.util

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import com.zeugmasolutions.localehelper.LocaleHelperApplicationDelegate
import java.util.*

open class InitApplication : Application() {
    private val localeAppDelegate = LocaleHelperApplicationDelegate()

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(localeAppDelegate.attachBaseContext(base))
    }
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        localeAppDelegate.onConfigurationChanged(this)
    }

}