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

class OrderDetailsAdapter : RecyclerView.Adapter<OrderDetailsAdapter.ViewHolder>() {

    private var mList = mutableListOf<OrderDetailsModel>()

    fun setData(item: List<OrderDetailsModel>) {
        mList = item.toMutableList()
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: LayoutOrderItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: OrderDetailsModel) {
            val lang = SharedPreferenceHelper.language ?: "en"
            if (lang == "ar"){
                binding.priceLayout.visibility = View.GONE
                binding.textView2.visibility = View.GONE
                binding.textView4.visibility = View.VISIBLE
                binding.pricerAr.visibility = View.VISIBLE
                binding.priceTxtar.visibility= View.VISIBLE
                binding.quantity.visibility = View.GONE
            }else{
                binding.priceLayout.visibility = View.VISIBLE
                binding.textView2.visibility = View.VISIBLE
                binding.textView4.visibility = View.GONE
                binding.pricerAr.visibility = View.GONE
                binding.quantityar.visibility = View.GONE
                binding.priceTxtar.visibility= View.GONE
                binding.quantity.visibility = View.VISIBLE
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