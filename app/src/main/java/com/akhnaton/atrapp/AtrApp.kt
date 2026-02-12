package com.akhnaton.atrapp

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import android.util.Log
import com.akhnaton.atrapp.shared.DebugBannerManager
import com.akhnaton.atrapp.shared.LocaleHelper
import com.akhnaton.atrapp.shared.SharedPreferenceHelper

class AtrApp : Application() {

    override fun onCreate() {
        super.onCreate()

        SharedPreferenceHelper.init(this)
        registerActivityLifecycleCallbacks(
            object : ActivityLifecycleCallbacks {
                override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}
                override fun onActivityDestroyed(activity: Activity) {}
                override fun onActivityPaused(activity: Activity) {}
                override fun onActivityResumed(activity: Activity) {}
                override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
                override fun onActivityStarted(activity: Activity) {
                    DebugBannerManager.show(activity)
                }

                override fun onActivityStopped(activity: Activity) {}
            }
        )
    }
}
