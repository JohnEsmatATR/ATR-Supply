package com.akhnaton.atrapp.ui.nav.cart

import android.graphics.Paint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load

class CartAdapter(
//    private val onClick: (product: ProductModel, position: Int) -> Unit,
//    private val onPlusClick: (product: ProductModel, position: Int, quantity: Int) -> Unit,
//    private val onMinusClick: (product: ProductModel, position: Int, quantity: Int) -> Unit,
//    private val onDeleteClick: (product: ProductModel, position: Int) -> Unit
)
//    : RecyclerView.Adapter<CartAdapter.ViewHolder>() {

//    private var productsList = ArrayList<ProductModel>()
//    private var isVisible: Boolean = true
//    private var type: String = ""
//
//    fun setData(cart: List<ProductModel>, isVisible: Boolean, type: String) {
//        productsList = cart as ArrayList
//        this.isVisible = isVisible
//        this.type = type
//        notifyDataSetChanged()
//    }
//
//    inner class ViewHolder(private val binding: LayoutCartBinding) :
//        RecyclerView.ViewHolder(binding.root) {
//        private var quantity = 1
//
//        fun bind(item: ProductModel, position: Int) {
//
//            binding.cart = item
//            binding.quantity = item.quantity
//
//            if (isVisible) {
//                binding.btnPlus.visibility = View.VISIBLE
//                binding.btnMinus.visibility = View.VISIBLE
//                binding.imDelete.visibility = View.VISIBLE
//                binding.txtQuantityText.visibility = View.GONE
//                if (item.stock_code == 0) {
//                    binding.layoutQuantity.visibility = View.INVISIBLE
//                    binding.txtOutOfStock.visibility = View.VISIBLE
//                } else {
//                    binding.layoutQuantity.visibility = View.VISIBLE
//                    binding.txtOutOfStock.visibility = View.INVISIBLE
//                }
//
//                if (item.excluder_flag != "N") {
//                    binding.layoutVipPrice.visibility = View.GONE
//                } else {
//                    binding.layoutVipPrice.visibility = View.VISIBLE
//                }
//            } else {
//                binding.btnPlus.visibility = View.GONE
//                binding.btnMinus.visibility = View.GONE
//                binding.imDelete.visibility = View.GONE
//                binding.txtQuantityText.visibility = View.VISIBLE
//                binding.layoutVipPrice.visibility = View.GONE
//                binding.txtPercentage.text = item.discount_rate.toString() + "% off)"
//            }
//
//            if (type != "orderDetails") {
//                if (item.old_discount == 0.0) {
//                    binding.txtPercentage.visibility = View.GONE
//                    binding.txtOldPrice.visibility = View.GONE
//                } else {
//                    binding.txtPercentage.visibility = View.VISIBLE
//                    binding.txtOldPrice.visibility = View.VISIBLE
//
//                }
//            } else {
//                item.old_discount = item.discount_rate
//                item.old_price = item.price_before_discount
//            }
//
//            binding.imItem.load("${Common.ImgUrl}${item.image}") {
//                crossfade(true)
//                placeholder(R.drawable.ic_logo)
//            }
//
//
//            binding.btnPlus.setOnClickListener {
//                if (adapterPosition != RecyclerView.NO_POSITION) {
//                    quantity = binding.txtQuantity.text.toString().toInt()
//                    if (item.flag != 5 || (item.flag == 5)) {
//                        quantity++
//                        binding.quantity = quantity
//                        onPlusClick(item, position, quantity)
//                    }
//                }
//            }
//
//            binding.btnMinus.setOnClickListener {
//                if (adapterPosition != RecyclerView.NO_POSITION) {
//                    quantity = binding.txtQuantity.text.toString().toInt()
//                    if (validateDecreaseQuantity(quantity)) {
//                        quantity--
//                        binding.quantity = quantity
//                        onMinusClick(item, position, quantity)
//                    }
//                }
//            }
//            binding.imDelete.setOnClickListener {
//                if (adapterPosition != RecyclerView.NO_POSITION) {
//                    onDeleteClick(item, position)
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
//        val binding = LayoutCartBinding.inflate(
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