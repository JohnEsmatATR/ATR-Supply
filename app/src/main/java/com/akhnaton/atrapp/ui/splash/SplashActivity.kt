package com.akhnaton.atrapp.ui.splash

import android.content.Intent
import android.content.pm.PackageInfo
import android.os.Bundle
import android.util.Log
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.akhnaton.atrapp.R
import com.akhnaton.atrapp.databinding.ActivitySplashBinding
import com.akhnaton.atrapp.shared.BaseActivity
import com.akhnaton.atrapp.shared.SharedPreferenceHelper
import com.akhnaton.atrapp.ui.auth.onBoarding.OnBoardingActivity
import com.akhnaton.atrapp.ui.auth.onBoarding.WelcomeActivity
import com.akhnaton.atrapp.ui.nav.HomeActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : BaseActivity() {
    lateinit var binding: ActivitySplashBinding
    private var logoAnim: Animation? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    private fun init() {
        SharedPreferenceHelper.init(this@SplashActivity)

        val pInfo: PackageInfo =
            baseContext.packageManager.getPackageInfo(baseContext.packageName, 0)

        SharedPreferenceHelper.version = pInfo.versionName

        logoAnim = AnimationUtils.loadAnimation(baseContext, R.anim.logo_anim)
        binding.imLogo.animation = logoAnim

        CoroutineScope(Dispatchers.Main).launch {
            delay(3000)
            Log.d("dvjnkdvndvdv", "init: ${SharedPreferenceHelper.userObj}")
            if (SharedPreferenceHelper.isLogged!!) {
                val intent = Intent(baseContext, HomeActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                val intent = Intent(baseContext, OnBoardingActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

}