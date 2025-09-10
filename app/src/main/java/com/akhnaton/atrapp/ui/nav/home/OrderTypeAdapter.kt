package com.akhnaton.atrapp.ui.nav.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.akhnaton.atrapp.data.model.CategoriesModel
import com.akhnaton.atrapp.data.model.OrderTypeModel
import com.akhnaton.atrapp.databinding.LayoutOrderTypeItemBinding

class OrderTypeAdapter(
    private val onCategoryClick: (category: CategoriesModel, orderType: String, position: Int) -> Unit
) : RecyclerView.Adapter<OrderTypeAdapter.ViewHolder>() {

    private var orderTypes = ArrayList<OrderTypeModel>()

    fun setData(data: List<OrderTypeModel>) {
        orderTypes = ArrayList(data)
        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: LayoutOrderTypeItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private lateinit var currentOrderType: OrderTypeModel

        private val categoryAdapter = CategoryAdapter { category, position ->

            onCategoryClick(category, currentOrderType.order_type ?: "", position)
        }

        init {
            binding.recyclerCategories.apply {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                adapter = categoryAdapter
                setHasFixedSize(true)
            }
        }

        fun bind(item: OrderTypeModel) {
            currentOrderType = item
            binding.txtOrderType.text = item.order_type
            categoryAdapter.setData(item.categories)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = LayoutOrderTypeItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(orderTypes[position])
    }

    override fun getItemCount(): Int = orderTypes.size
}
