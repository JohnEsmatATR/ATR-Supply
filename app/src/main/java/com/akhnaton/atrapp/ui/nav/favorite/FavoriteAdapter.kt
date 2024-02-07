package com.akhnaton.atrapp.ui.nav.favorite

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load

class FavoriteAdapter(
//    private val onClick: (product: ProductModel, position: Int) -> Unit,
//    private val onAddToProductClick: (product: ProductModel, position: Int) -> Unit,
//    private val onFavoriteClick: (product: ProductModel, position: Int) -> Unit,
)
//    : RecyclerView.Adapter<FavoriteAdapter.ViewHolder>() {
//
//    private var productsList = ArrayList<ProductModel>()
//
//    fun setData(product: List<ProductModel>) {
//        productsList = product as ArrayList
//        notifyDataSetChanged()
//    }
//
//    inner class ViewHolder(private val binding: LayoutFavoriteBinding) :
//        RecyclerView.ViewHolder(binding.root) {
//
//        fun bind(item: ProductModel, position: Int) {
//
//            binding.product = item
//
//            binding.imItem.load("${Common.ImgUrl}${item.image}") {
//                crossfade(true)
//                placeholder(R.drawable.ic_logo)
//            }
//
//            if (item.quantity > 0) {
//                binding.btnAddToCart.visibility = View.VISIBLE
//                binding.txtStock.visibility = View.GONE
//            } else {
//                binding.btnAddToCart.visibility = View.GONE
//                binding.txtStock.visibility = View.VISIBLE
//            }
//
//            binding.btnAddToCart.setOnClickListener {
//                if (adapterPosition != RecyclerView.NO_POSITION) {
//                    onAddToProductClick(item, position)
//                }
//            }
//
//            binding.imFavorite.setOnClickListener {
//                if (adapterPosition != RecyclerView.NO_POSITION) {
//                    onFavoriteClick(item, position)
//
//                }
//            }
//
//            itemView.setOnClickListener {
//                if (adapterPosition != RecyclerView.NO_POSITION) {
//                    onClick(item, position)
//                }
//            }
//
//        }
//
//
//        private fun validateDecreaseQuantity(qty: Int): Boolean {
//            return (qty > 1)
//        }
//
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        val binding = LayoutFavoriteBinding.inflate(
//            LayoutInflater.from(parent.context),
//            parent,
//            false
//        )
//        binding.txtOldPrice.paintFlags =
//            binding.txtOldPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
//
//        return ViewHolder(binding)
//    }
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        productsList?.get(position)?.let { holder.bind(it, position) }
//    }
//
//    override fun getItemCount(): Int {
//        return productsList.size
//    }
//
//
//}