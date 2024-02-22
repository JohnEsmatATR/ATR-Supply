package com.akhnaton.atrapp.ui.nav.cart

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.akhnaton.atrapp.R
import com.akhnaton.atrapp.data.model.ProductModel
import com.akhnaton.atrapp.databinding.LayoutCartBinding
import com.akhnaton.atrapp.shared.Common

class CartAdapter(
    private val onClick: (product: ProductModel, position: Int) -> Unit,
    private val onPlusClick: (product: ProductModel, position: Int, quantity: Int) -> Unit,
    private val onMinusClick: (product: ProductModel, position: Int, quantity: Int) -> Unit,
) : RecyclerView.Adapter<CartAdapter.ViewHolder>() {

    private var productsList = ArrayList<ProductModel>()
    private var isVisible: Boolean = true
    private var type: String = ""

    fun setData(cart: List<ProductModel>, isVisible: Boolean, type: String) {
        productsList = cart as ArrayList
        this.isVisible = isVisible
        this.type = type
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: LayoutCartBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private var quantity = 1

        fun bind(item: ProductModel, position: Int) {

            binding.cart = item
            binding.quantity = item.quantity

            if (isVisible) {
                binding.btnPlus.visibility = View.VISIBLE
                binding.btnMinus.visibility = View.VISIBLE

            } else {
                binding.btnPlus.visibility = View.GONE
                binding.btnMinus.visibility = View.GONE
            }


            binding.imItem.load("${Common.ImgUrl}${item.image}") {
                crossfade(true)
                placeholder(R.drawable.ic_logo)
            }


            binding.btnPlus.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    quantity = binding.txtQuantity.text.toString().toInt()
                    if (item.flag != 5 || (item.flag == 5)) {
                        quantity++
                        binding.quantity = quantity
                        onPlusClick(item, position, quantity)
                    }
                }
            }

            binding.btnMinus.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    quantity = binding.txtQuantity.text.toString().toInt()
                    if (validateDecreaseQuantity(quantity)) {
                        quantity--
                        binding.quantity = quantity
                        onMinusClick(item, position, quantity)
                    }
                }
            }
            itemView.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    onClick(item, position)
                }
            }

        }


        private fun validateDecreaseQuantity(qty: Int): Boolean {
            return (qty > 1)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = LayoutCartBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        productsList?.get(position)?.let { holder.bind(it, position) }
    }

    override fun getItemCount(): Int {
        return productsList.size
    }


}