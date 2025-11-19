package com.akhnaton.atrapp.ui.nav.profile.order.details.returnOrder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.akhnaton.atrapp.data.model.orderHistory.OrderDetailsModel
import com.akhnaton.atrapp.databinding.LayoutOrderReturnItemBinding

class OrderReturnAdapter : RecyclerView.Adapter<OrderReturnAdapter.ViewHolder>() {

    private var mList = mutableListOf<OrderDetailsModel>()

    fun setData(item: List<OrderDetailsModel>) {
        mList = item.toMutableList()
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: LayoutOrderReturnItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: OrderDetailsModel) {
            binding.data = item
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = LayoutOrderReturnItemBinding.inflate(
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