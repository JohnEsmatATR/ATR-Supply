package com.akhnaton.atrapp.ui.nav.profile.order.details

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.akhnaton.atrapp.R
import com.akhnaton.atrapp.data.model.orderHistory.OrderDetailsModel
import com.akhnaton.atrapp.databinding.LayoutOrderItemBinding
import com.akhnaton.atrapp.shared.SharedPreferenceHelper
import com.akhnaton.atrapp.util.formatPrice
import java.util.Locale

class OrderDetailsAdapter : RecyclerView.Adapter<OrderDetailsAdapter.ViewHolder>() {

    private var mList = mutableListOf<OrderDetailsModel>()

    fun setData(item: List<OrderDetailsModel>) {
        mList = item.toMutableList()
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: LayoutOrderItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: OrderDetailsModel) {
            val context = binding.root.context
            val lang = SharedPreferenceHelper.language ?: "ar"

            // Calculate unit price
            val unitPrice = if (item.quantity > 0) {
                formatPrice(item.price / item.quantity, 2, Locale(lang))
            } else {
                formatPrice(0.0)
            }

            val unitPriceText = "$unitPrice ${context.getString(R.string.currency)}"

            if (lang == "ar") {
                binding.priceLayout.visibility = View.GONE
                binding.textView2.visibility = View.GONE
                binding.textView4.visibility = View.VISIBLE
                binding.pricerAr.visibility = View.VISIBLE
                binding.priceTxtar.visibility = View.VISIBLE
                binding.quantity.visibility = View.GONE
                binding.unitPriceLabel.visibility = View.GONE
                binding.unitPrice.visibility = View.GONE
                binding.unitPriceLabelAr.visibility = View.VISIBLE
                binding.unitPriceAr.visibility = View.VISIBLE
                binding.unitPriceAr.text = unitPriceText
            } else {
                binding.priceLayout.visibility = View.VISIBLE
                binding.textView2.visibility = View.VISIBLE
                binding.textView4.visibility = View.GONE
                binding.pricerAr.visibility = View.GONE
                binding.quantityar.visibility = View.GONE
                binding.priceTxtar.visibility = View.GONE
                binding.quantity.visibility = View.VISIBLE
                binding.unitPriceLabel.visibility = View.VISIBLE
                binding.unitPrice.visibility = View.VISIBLE
                binding.unitPriceLabelAr.visibility = View.GONE
                binding.unitPriceAr.visibility = View.GONE
                binding.unitPrice.text = unitPriceText
            }
            binding.data = item
            binding.imgProduct.load(item.img) {
                crossfade(true)
                placeholder(R.drawable.ic_logo)
                error(R.drawable.ic_logo)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = LayoutOrderItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(mList[position])
    }

    override fun getItemCount(): Int {
        return mList.size
    }

}