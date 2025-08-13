package com.akhnaton.atrapp.shared

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.res.Configuration
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.akhnaton.atrapp.R
import java.util.Locale


open class BaseActivity : AppCompatActivity() {
    var dp = 0f

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        supportRequestWindowFeature(Window.FEATURE_ACTION_BAR)
        super.onCreate(savedInstanceState, persistentState)
        dp = resources.displayMetrics.density

        setAppLocale(SharedPreferenceHelper.language ?: "en")


    }

    override fun onResume() {
        super.onResume()
    }

    fun showToastSnack(word: String?, flag: Boolean) {
        try {
            val layout = LayoutInflater.from(this).inflate(R.layout.snack_bar_layout, null, false)
            layout.setBackgroundColor(
                if (flag)  ContextCompat.getColor(this@BaseActivity, R.color.snack_red) else this.resources.getColor(
                    R.color.snack_green
                )
            )
            val image = layout.findViewById<ImageView>(R.id.image)
            image.setImageResource(if (flag) R.drawable.ic_error else R.drawable.ic_success)
            val text = layout.findViewById<TextView>(R.id.text)
            text.text = word
            text.setTextColor(ContextCompat.getColor(this@BaseActivity, R.color.white))
            val parentLayout = findViewById<View>(android.R.id.content)
            val snackbar = Snackbar.make(parentLayout, "", BaseTransientBottomBar.LENGTH_SHORT)
            (snackbar.view as ViewGroup).removeAllViews()
            (snackbar.view as ViewGroup).addView(layout)
            val params = snackbar.view.layoutParams as FrameLayout.LayoutParams
            params.gravity = Gravity.TOP
            snackbar.view.setBackgroundColor(
                if (flag) ContextCompat.getColor(this@BaseActivity, R.color.snack_red)
                else ContextCompat.getColor(this@BaseActivity, R.color.snack_green)
            )
            snackbar.setBackgroundTint(
                if (flag) ContextCompat.getColor(this@BaseActivity, R.color.snack_red)
                else  ContextCompat.getColor(this@BaseActivity, R.color.snack_green)
            )

            snackbar.view.layoutParams = params
            snackbar.view.setPadding(
                (16 * dp).toInt(),
                (10 * dp).toInt(),
                (16 * dp).toInt(),
                (10 * dp).toInt()
            )
            snackbar.show()
        } catch (e: Exception) {

        }
    }

    fun showToast(view: View, str: String) {
        val snack: Snackbar = Snackbar.make(view, str, Snackbar.LENGTH_LONG)
        val view = snack.view
        val params = view.layoutParams as FrameLayout.LayoutParams
        params.gravity = Gravity.TOP
        view.layoutParams = params
        view.setBackgroundResource(R.color.snack_green)
        snack.show()
    }

    fun showDialog(title: String, message: String, isCancelable: Boolean): AlertDialog.Builder {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this@BaseActivity)

        builder.setTitle(title)
        builder.setMessage(message)
        builder.setCancelable(isCancelable)
        return builder
    }


    fun validateIncreaseQuantity(qty: Int, max: Int): Boolean {
        return (qty < max)
    }

    fun validateDecreaseQuantity(qty: Int): Boolean {
        return (qty > 1)
    }

//    fun onTokenExpired(message: String) {
//        showDialog("Warning!", message, true)
//            .setPositiveButton("Ok") { dialog: DialogInterface, i: Int ->
//                SharedPreferenceHelper.let { sharedPref ->
//                    SharedPreferenceHelper.isLogged = false
//                    SharedPreferenceHelper.isWelcomeShowed = false
//                    SharedPreferenceHelper.userObj = null
//                    SharedPreferenceHelper.membership = 0
//                    SharedPreferenceHelper.userToken = ""
//                }
//                startActivity(
//                    Intent(
//                        baseContext,
//                        AuthActivity::class.java
//                    )
//                )
//                finish()
//                dialog.dismiss()
//            }.create().show()
//
//    }


    fun showProgressDialog(view: View) {
        view.visibility = View.VISIBLE
        window.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
        )
    }

    fun hideProgressDialog(view: View) {
        view.visibility = View.GONE
        window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }

    fun setAppLocale(lang: String) {
        val locale = Locale.forLanguageTag(lang)
        Locale.setDefault(locale)

        val config = Configuration()
        config.setLocale(locale)

        SharedPreferenceHelper.language = lang

        val context = createConfigurationContext(config)
        context.resources
    }


//    fun getVersion(): String {
//        val pInfo: PackageInfo =
//            baseContext.packageManager.getPackageInfo(baseContext.packageName, 0)
//        return pInfo.versionName
//    }

    open fun isNetworkAvailable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork ?: return false
            val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                    activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                    activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
        } else {
            @Suppress("DEPRECATION")
            val networkInfo = connectivityManager.activeNetworkInfo
            @Suppress("DEPRECATION")
            networkInfo != null && networkInfo.isConnected
        }
    }


}