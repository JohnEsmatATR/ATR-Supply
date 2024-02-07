package com.akhnaton.atrapp.ui.nav

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.core.view.get
import androidx.fragment.app.Fragment
import com.akhnaton.atrapp.R
import com.akhnaton.atrapp.databinding.ActivityHomeBinding
import com.akhnaton.atrapp.shared.BaseActivity
import com.akhnaton.atrapp.ui.nav.cart.CartFragment
import com.akhnaton.atrapp.ui.nav.favorite.FavoriteFragment
import com.akhnaton.atrapp.ui.nav.home.HomeFragment
import com.akhnaton.atrapp.ui.nav.profile.ProfileFragment
import com.google.android.material.navigation.NavigationView

class HomeActivity : BaseActivity() {
    lateinit var binding: ActivityHomeBinding
    lateinit var actionBarDrawerToggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
        onClick()

    }

    private fun init() {
        initNavBottom()
        setupDrawer()
    }


    private fun onClick() {
//        binding.imNotification.setOnClickListener {
//            startActivity(Intent(this@HomeActivity, NotificationActivity::class.java))
//        }
//        binding.imMenu.setOnClickListener {
//            binding.drawerLayout.openDrawer(GravityCompat.START, true)
//        }
//        binding.navigationView.setNavigationItemSelectedListener(this)
//
//        try {
//            val header = binding.navigationView.getHeaderView(0)
//            val txtName = header.findViewById(R.id.txt_name) as TextView
//            txtName.text = SharedPreferenceHelper.userObj!!.full_name
//        }catch (e:Exception){}

    }

    private fun setupDrawer() {

//        actionBarDrawerToggle = ActionBarDrawerToggle(this, binding.drawerLayout, R.string.nav_open, R.string.nav_close)
//        binding.drawerLayout.addDrawerListener(actionBarDrawerToggle)
//
//        actionBarDrawerToggle.syncState()
//
//        // to make the Navigation drawer icon always appear on the action bar
//        supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }


    private fun initNavBottom() {

        setItemSelected(R.id.home)
        binding.bottomNavigationView.menu[2].isEnabled = false


        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    setItemSelected(R.id.home)
                }

                R.id.favorite -> {
                    setItemSelected(R.id.favorite)
                }

                R.id.cart -> {
                    setItemSelected(R.id.cart)
                }

                R.id.profile -> {
                    setItemSelected(R.id.profile)
                }

                else -> {
                    setItemSelected(R.id.home)
                }
            }
            true
        }
    }

    private fun setCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment, fragment)
            commit()
        }

    private fun setItemSelected(item: Int) {
        val homeFragment = HomeFragment()
        val cartFragment = CartFragment()
        val favoriteFragment = FavoriteFragment()
        val profileFragment = ProfileFragment()

        when (item) {
            R.id.home -> {
                setCurrentFragment(homeFragment)
                binding.bottomNavigationView.menu[0].icon = getDrawable(R.drawable.ic_home_fill)
                binding.bottomNavigationView.menu[1].icon = getDrawable(R.drawable.ic_favorite)
                binding.bottomNavigationView.menu[3].icon = getDrawable(R.drawable.ic_cart)
                binding.bottomNavigationView.menu[4].icon = getDrawable(R.drawable.ic_profile)
            }

            R.id.cart -> {
                setCurrentFragment(cartFragment)
                binding.bottomNavigationView.menu[0].icon = getDrawable(R.drawable.ic_home)
                binding.bottomNavigationView.menu[1].icon = getDrawable(R.drawable.ic_favorite)
                binding.bottomNavigationView.menu[3].icon = getDrawable(R.drawable.ic_cart_fill)
                binding.bottomNavigationView.menu[4].icon = getDrawable(R.drawable.ic_profile)

            }

            R.id.favorite -> {
                setCurrentFragment(favoriteFragment)
                binding.bottomNavigationView.menu[0].icon = getDrawable(R.drawable.ic_home)
                binding.bottomNavigationView.menu[1].icon = getDrawable(R.drawable.ic_favorite_fill)
                binding.bottomNavigationView.menu[3].icon = getDrawable(R.drawable.ic_cart)
                binding.bottomNavigationView.menu[4].icon = getDrawable(R.drawable.ic_profile)

            }

            R.id.profile -> {
                setCurrentFragment(profileFragment)
                binding.bottomNavigationView.menu[0].icon = getDrawable(R.drawable.ic_home)
                binding.bottomNavigationView.menu[1].icon = getDrawable(R.drawable.ic_favorite)
                binding.bottomNavigationView.menu[3].icon = getDrawable(R.drawable.ic_cart)
                binding.bottomNavigationView.menu[4].icon = getDrawable(R.drawable.ic_profile_fill)


            }

            else -> {
                setCurrentFragment(homeFragment)
                binding.bottomNavigationView.menu[0].icon = getDrawable(R.drawable.ic_home_fill)
                binding.bottomNavigationView.menu[1].icon = getDrawable(R.drawable.ic_favorite)
                binding.bottomNavigationView.menu[3].icon = getDrawable(R.drawable.ic_cart)
                binding.bottomNavigationView.menu[4].icon = getDrawable(R.drawable.ic_profile)

            }
        }
    }
}