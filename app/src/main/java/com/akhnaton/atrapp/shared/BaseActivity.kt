package com.akhnaton.atrapp.shared

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import com.akhnaton.atrapp.R
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import java.util.Locale


open class BaseActivity : AppCompatActivity() {
    var dp = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dp = resources.displayMetrics.density
        
        // Enable edge-to-edge display
        WindowCompat.setDecorFitsSystemWindows(window, false)
        
        // Set up window insets controller
        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
        windowInsetsController.isAppearanceLightStatusBars = true
    }

    override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)
        setupSafeAreaInsets()
    }

    override fun setContentView(view: View?) {
        super.setContentView(view)
        setupSafeAreaInsets()
    }

    override fun setContentView(view: View?, params: ViewGroup.LayoutParams?) {
        super.setContentView(view, params)
        setupSafeAreaInsets()
    }

    /**
     * Automatically sets up window insets for common views (app_bar, bottom navigation, etc.)
     * This ensures all screens respect the safe area
     */
    private fun setupSafeAreaInsets() {
        val rootView = findViewById<View>(android.R.id.content)
        if (rootView != null) {
            // Post to ensure view hierarchy is ready and views are measured
            rootView.post {
                // Find and setup app_bar
                val appBar = rootView.findViewById<View>(R.id.app_bar)
                if (appBar != null) {
                    setupWindowInsetsTop(appBar)
                }

                // Find and setup bottom navigation
                val bottomNav = rootView.findViewById<View>(R.id.bottomNavigationView)
                if (bottomNav != null) {
                    setupWindowInsetsBottom(bottomNav)
                }

                // Find and setup RecyclerViews to respect bottom navigation bar
                setupRecyclerViewInsets(rootView)
                
                // Find and setup NestedScrollViews to respect bottom navigation bar
                setupScrollViewInsets(rootView)
                
                // Force apply window insets to ensure they're processed
                ViewCompat.requestApplyInsets(rootView)
            }
        }
    }

    /**
     * Recursively finds all RecyclerViews and applies bottom insets
     */
    private fun setupRecyclerViewInsets(rootView: View) {
        val recyclerViews = mutableListOf<androidx.recyclerview.widget.RecyclerView>()
        findViewsByType(rootView, androidx.recyclerview.widget.RecyclerView::class.java, recyclerViews)
        
        recyclerViews.forEach { recyclerView ->
            // Store original padding values before setting listener
            val originalPaddingLeft = recyclerView.paddingLeft
            val originalPaddingTop = recyclerView.paddingTop
            val originalPaddingRight = recyclerView.paddingRight
            val originalPaddingBottom = recyclerView.paddingBottom
            
            // Ensure padding is respected
            recyclerView.clipToPadding = false
            
            ViewCompat.setOnApplyWindowInsetsListener(recyclerView) { view, insets ->
                val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                // Use original padding values, or current if original was 0
                val paddingLeft = originalPaddingLeft.takeIf { it > 0 } ?: view.paddingLeft
                val paddingTop = originalPaddingTop.takeIf { it > 0 } ?: view.paddingTop
                val paddingRight = originalPaddingRight.takeIf { it > 0 } ?: view.paddingRight
                
                view.setPadding(
                    paddingLeft,
                    paddingTop,
                    paddingRight,
                    systemBars.bottom
                )
                insets
            }
        }
    }

    /**
     * Recursively finds all NestedScrollViews and ScrollViews and applies bottom insets
     */
    private fun setupScrollViewInsets(rootView: View) {
        val nestedScrollViews = mutableListOf<androidx.core.widget.NestedScrollView>()
        val scrollViews = mutableListOf<android.widget.ScrollView>()
        findViewsByType(rootView, androidx.core.widget.NestedScrollView::class.java, nestedScrollViews)
        findViewsByType(rootView, android.widget.ScrollView::class.java, scrollViews)
        
        nestedScrollViews.forEach { scrollView ->
            ViewCompat.setOnApplyWindowInsetsListener(scrollView) { view, insets ->
                val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                view.setPadding(
                    view.paddingLeft,
                    view.paddingTop,
                    view.paddingRight,
                    systemBars.bottom
                )
                insets
            }
        }
        
        scrollViews.forEach { scrollView ->
            ViewCompat.setOnApplyWindowInsetsListener(scrollView) { view, insets ->
                val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                view.setPadding(
                    view.paddingLeft,
                    view.paddingTop,
                    view.paddingRight,
                    systemBars.bottom
                )
                insets
            }
        }
    }

    /**
     * Recursively finds all views of a specific type
     */
    private fun <T : View> findViewsByType(view: View, type: Class<T>, result: MutableList<T>) {
        if (type.isInstance(view)) {
            result.add(type.cast(view)!!)
        }
        if (view is ViewGroup) {
            for (i in 0 until view.childCount) {
                findViewsByType(view.getChildAt(i), type, result)
            }
        }
    }


    override fun onResume() {
        super.onResume()
    }

    fun showToastSnack(word: String?, flag: Boolean) {
        try {
            val layout = LayoutInflater.from(this).inflate(R.layout.snack_bar_layout,
                null, false)
            layout.setBackgroundColor(
                if (flag)  ContextCompat.getColor(this@BaseActivity, R.color.snack_red)
                else ContextCompat.getColor(this@BaseActivity,
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

    /**
     * Helper function to apply window insets to a view (typically app_bar)
     * This ensures the view respects the status bar and safe area at the top
     */
    protected fun setupWindowInsetsTop(view: View) {
        ViewCompat.setOnApplyWindowInsetsListener(view) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(
                v.paddingLeft,
                systemBars.top,
                v.paddingRight,
                v.paddingBottom
            )
            insets
        }
    }

    /**
     * Helper function to apply window insets to a view at the bottom
     * This ensures the view respects the navigation bar and safe area at the bottom
     */
    protected fun setupWindowInsetsBottom(view: View) {
        ViewCompat.setOnApplyWindowInsetsListener(view) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val layoutParams = v.layoutParams
            if (layoutParams is ViewGroup.MarginLayoutParams) {
                layoutParams.bottomMargin = systemBars.bottom
                v.layoutParams = layoutParams
            }
            insets
        }
    }

    /**
     * Helper function to apply window insets to both top and bottom of a view
     */
    protected fun setupWindowInsets(view: View) {
        ViewCompat.setOnApplyWindowInsetsListener(view) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(
                v.paddingLeft,
                systemBars.top,
                v.paddingRight,
                systemBars.bottom
            )
            insets
        }
    }

    override fun attachBaseContext(newBase: Context) {
        SharedPreferenceHelper.init(newBase)
        val lang = SharedPreferenceHelper.language ?: "ar"
        val locale = Locale(lang)
        val config = Configuration(newBase.resources.configuration)
        config.setLocale(locale)
        val context = newBase.createConfigurationContext(config)

        android.util.Log.d("DEBUGGGGG", "Current language: $lang")

        super.attachBaseContext(context)
    }


}