package com.akhnaton.atrapp.ui.nav.cart.checkout

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.akhnaton.atrapp.data.model.CartProduct
import com.akhnaton.atrapp.data.model.CartResponse
import com.akhnaton.atrapp.databinding.ItemCartParentBinding
import com.akhnaton.atrapp.shared.SharedPreferenceHelper
import com.akhnaton.atrapp.ui.nav.cart.CartAdapter

class CartParentAdapter(
    private val onClick: (CartProduct, Int) -> Unit,
    private val onPlusClick: (CartProduct, Int, Int) -> Unit,
    private val onMinusClick: (CartProduct, Int, Int) -> Unit,
    private val onDeleteClick: (CartProduct, Int) -> Unit
) : RecyclerView.Adapter<CartParentAdapter.ParentViewHolder>() {

    private var cartList = mutableListOf<CartResponse>()

    fun setData(data: List<CartResponse>) {
        cartList.clear()
        cartList.addAll(data)
        notifyDataSetChanged()
    }

    inner class ParentViewHolder(private val binding: ItemCartParentBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: CartResponse) {
            binding.cartResponse = item
            val lan = SharedPreferenceHelper.language ?: "ar"




            val childAdapter = CartAdapter(
                lan,
                onClick,
                onPlusClick,
                onMinusClick,
                onDeleteClick,

            )
            binding.recyclerProducts.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = childAdapter
            }
            childAdapter.setData(item.items.products, true, item.orderType)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ParentViewHolder {
        val binding = ItemCartParentBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ParentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ParentViewHolder, position: Int) {
        holder.bind(cartList[position])
    }

    override fun getItemCount(): Int = cartList.size
}
