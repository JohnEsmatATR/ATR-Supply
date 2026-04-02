package com.akhnaton.atrapp.ui.nav.home

import android.graphics.Paint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.akhnaton.atrapp.R
import com.akhnaton.atrapp.data.model.ProductModel
import com.akhnaton.atrapp.databinding.LayoutProductBinding
import com.bumptech.glide.Glide

class ProductAdapter(
    private val onClick: (product: ProductModel, position: Int, sharedView: View, transitionName: String) -> Unit,
    private val onFavoriteClick: (product: ProductModel, position: Int, isFavorite: Boolean) -> Unit,
    private val onAddToCartClick: (product: ProductModel) -> Unit = {}
) : RecyclerView.Adapter<ProductAdapter.ViewHolder>() {

    private var productsList = ArrayList<ProductModel>()
    private lateinit var flag: String
    var isInHome: Boolean = false

    // في ProductAdapter
    fun setData(newList: List<ProductModel>, isAppend: Boolean, flag: String) {
        this.flag = flag
        if (!isAppend) {
            productsList.clear()
        }
        productsList.addAll(newList)
        notifyDataSetChanged()
    }


    fun addData(newList: List<ProductModel>) {
        val startPosition = productsList.size
        productsList.addAll(newList)
        notifyItemRangeInserted(startPosition, newList.size)
    }

    fun updateList(newList: List<ProductModel>) {
        this.productsList.clear()
        this.productsList.addAll(newList)
        notifyDataSetChanged()
    }


    inner class ViewHolder(private val binding: LayoutProductBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private var isFavorite = false
        private var hasBones = false

        fun bind(item: ProductModel, position: Int) {
            isFavorite = item.IS_LIKED
            hasBones = item.HAS_BONUS
            val offer = item.PRICE_DISCOUNT_PERCENTAGE
            if (offer == "0%") {
                binding.imDiscount.visibility = View.GONE
                binding.txtStock.visibility = View.GONE
            } else {
                binding.imDiscount.visibility = View.VISIBLE
                binding.txtStock.visibility = View.VISIBLE
            }
            if (item.PRICE_AFTER_DISCOUNT == 0.0) {
                binding.txtPrice.visibility = View.GONE
            } else {
                binding.txtPrice.visibility = View.VISIBLE
            }

            Log.d("WHAT", item.PRICE_WITH_TAX.toString())
            Log.d("WHAT", item.PRICE_AFTER_DISCOUNT.toString())

            if (item.PRICE_WITH_TAX == item.PRICE_AFTER_DISCOUNT) {
                binding.txtOldPrice.visibility = View.GONE
            }
            if (hasBones) {
                binding.imageView.visibility = View.VISIBLE
            } else {
                binding.imageView.visibility = View.GONE
            }

            changeFavoriteButton()

            binding.productModel = item
//
            Glide.with(binding.root.context).load(item.IMAGE_URL).into(binding.imItem)

            binding.imFavorite.setOnClickListener {
                isFavorite = !isFavorite
                changeFavoriteButton()
                onFavoriteClick(item, position, isFavorite)
            }

            binding.btnAddToCart.setOnClickListener {
                onAddToCartClick(item)
            }

            itemView.setOnClickListener {
                val sharedView = binding.txtItemName
                val transitionName =
                    ViewCompat.getTransitionName(sharedView) ?: "itemImageTransition"
                onClick(item, position, sharedView, transitionName)
            }

        }

        private fun changeFavoriteButton() {
            val iconRes = if (isFavorite) R.drawable.ic_favorite_fill else R.drawable.ic_favorite
            binding.imFavorite.load(iconRes) {
                crossfade(true)
                placeholder(R.drawable.ic_logo)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            LayoutProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        binding.txtOldPrice.paintFlags =
            binding.txtOldPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
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
        productsList.get(position).let { holder.bind(it, position) }
    }

    override fun getItemCount(): Int {
        return productsList.size
    }

    fun clear() {
        productsList.clear()
        notifyDataSetChanged()
    }

}