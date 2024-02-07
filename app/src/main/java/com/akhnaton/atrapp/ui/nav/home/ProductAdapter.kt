package com.akhnaton.atrapp.ui.nav.home

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.akhnaton.atrapp.R
import com.akhnaton.atrapp.data.model.ProductModel
import com.akhnaton.atrapp.databinding.LayoutProductBinding
import com.akhnaton.atrapp.shared.Common

class ProductAdapter(
    private val onClick: (product: ProductModel, position: Int) -> Unit,
    private val onFavoriteClick: (product: ProductModel, position: Int, isFavorite: Boolean) -> Unit
) :
    RecyclerView.Adapter<ProductAdapter.ViewHolder>() {

    private var productsList = ArrayList<ProductModel>()
    private lateinit var flag: String
    private var isInHome: Boolean = false

    fun setData(product: List<ProductModel>, isInHome: Boolean, flag: String) {
        productsList = product as ArrayList
        this.flag = flag
        this.isInHome = isInHome
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: LayoutProductBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var isFavorite = false
        fun bind(item: ProductModel, position: Int) {
            isFavorite = item.in_favourite

            changeFavoriteButton()

            binding.productModel = item

            binding.imItem.load("${Common.ImgUrl}${item.image}") {
                crossfade(true)
                placeholder(R.drawable.ic_logo)
            }

            if (item.old_discount == 0.0) {
                binding.txtPercentage.visibility = View.GONE
                binding.txtOldPrice.visibility = View.GONE
            } else {
                binding.txtPercentage.visibility = View.VISIBLE
                binding.txtOldPrice.visibility = View.VISIBLE

            }

            itemView.setOnClickListener {
                onClick(item, position)
            }
            binding.imFavorite.setOnClickListener {
                isFavorite = !isFavorite
                changeFavoriteButton()
                onFavoriteClick(item, position, isFavorite)
            }

            if (item.is_best_sale == 0) {
                binding.imDiscount.visibility = View.GONE
            } else {
                binding.imDiscount.visibility = View.VISIBLE
            }

        }

        private fun changeFavoriteButton(){
            val imFavorite =
                if (isFavorite) { R.drawable.ic_favorite_fill }
                else { R.drawable.ic_favorite }

            binding.imFavorite.load(imFavorite) {
                crossfade(true)
                placeholder(R.drawable.ic_logo)
            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = LayoutProductBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        binding.txtOldPrice.paintFlags = binding.txtOldPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        if (isInHome) {
            binding.root.layoutParams = ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
            )
        } else {
            binding.root.layoutParams = ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_PARENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
            )
        }
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        productsList?.get(position)?.let { holder.bind(it, position) }
    }

    override fun getItemCount(): Int {
        return productsList.size
    }

}