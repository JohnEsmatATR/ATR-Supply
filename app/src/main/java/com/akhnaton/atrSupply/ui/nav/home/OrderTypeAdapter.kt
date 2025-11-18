package com.akhnaton.atrSupply.ui.nav.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.akhnaton.atrSupply.data.model.CategoriesModel
import com.akhnaton.atrSupply.data.model.OrderTypeModel
import com.akhnaton.atrSupply.databinding.LayoutOrderTypeItemBinding
import com.akhnaton.atrSupply.shared.SharedPreferenceHelper

class OrderTypeAdapter(
    private val onCategoryClick: (category: CategoriesModel, orderType: String, position: Int) -> Unit
) : RecyclerView.Adapter<OrderTypeAdapter.ViewHolder>() {

    private var order_type = ArrayList<OrderTypeModel>()

    fun setData(data: List<OrderTypeModel>) {
        order_type = ArrayList(data)
        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: LayoutOrderTypeItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private lateinit var currentOrderType: OrderTypeModel

        private val categoryAdapter = CategoryAdapter { category, position ->

            onCategoryClick(category, currentOrderType.order_type_index, position)

        }

        init {
            val isArabic = SharedPreferenceHelper.language == "ar"

            binding.recyclerCategories.apply {
                layoutManager = LinearLayoutManager(
                    context,
                    LinearLayoutManager.HORIZONTAL,
                    isArabic
                )

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
        holder.bind(order_type[position])
    }

    override fun getItemCount(): Int = order_type.size
}
