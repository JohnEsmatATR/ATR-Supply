package com.akhnaton.atrapp.ui.nav.profile.order.history

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.akhnaton.atrapp.R
import com.akhnaton.atrapp.data.model.orderHistory.OrderHistoryModel
import com.akhnaton.atrapp.databinding.LayoutOrderBinding
import com.akhnaton.atrapp.ui.nav.home.categorys.AllCategoryAdapter

class OrderHistoryAdapter : RecyclerView.Adapter<OrderHistoryAdapter.ViewHolder>() {

    private lateinit var listener: OnProductClickListener
    private var mList = mutableListOf<OrderHistoryModel>()

    fun setData(
        order: List<OrderHistoryModel>,
        listener: OnProductClickListener
    ) {
        mList = order.toMutableList()
        this.listener = listener
        notifyDataSetChanged()
    }

    inner class ViewHolder(
        private val binding: LayoutOrderBinding,
        private val listener: OnProductClickListener
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: OrderHistoryModel) {
            binding.data = item

            val colorResId = if (item.orderStatusCode == 0) R.color.orange else R.color.snack_green
            val color = binding.root.context.resources.getColor(colorResId, null)
            binding.orderStatus.setTextColor(color)

            binding.viewOrderBtn.setOnClickListener { listener.onProductClick(item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = LayoutOrderBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding, listener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(mList[position])
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    interface OnProductClickListener {
        fun onProductClick(data: OrderHistoryModel)
    }
}