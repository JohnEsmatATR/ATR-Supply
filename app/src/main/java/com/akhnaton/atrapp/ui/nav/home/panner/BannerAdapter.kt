package com.akhnaton.atrapp.ui.nav.home.panner

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.viewpager.widget.PagerAdapter
import com.akhnaton.atrapp.R
import com.bumptech.glide.Glide

class BannerAdapter(private val banners: List<String>) : PagerAdapter() {

    override fun getCount(): Int = banners.size

    override fun isViewFromObject(view: View, obj: Any): Boolean {
        return view == obj
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {

        val view = LayoutInflater.from(container.context)
            .inflate(R.layout.item_banner, container, false)

        val imageView = view.findViewById<ImageView>(R.id.bannerImage)

        Glide.with(container.context)
            .load(banners[position])
            .into(imageView)

        container.addView(view)

        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, obj: Any) {
        container.removeView(obj as View)
    }
}