package com.akhnaton.atrapp.shared

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.akhnaton.atrapp.R

class ShimmerAdapterCart (private val itemCount: Int) : RecyclerView.Adapter<ShimmerAdapterCart.ShimmerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShimmerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_cart_shimmer, parent, false)
        return ShimmerViewHolder(view)
    }

    override fun onBindViewHolder(holder: ShimmerViewHolder, position: Int) {

    }

    override fun getItemCount() = itemCount


    class ShimmerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}