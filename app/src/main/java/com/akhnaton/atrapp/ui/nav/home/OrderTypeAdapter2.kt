package com.akhnaton.atrapp.ui.nav.home

import android.util.LayoutDirection
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.akhnaton.atrapp.R
import com.akhnaton.atrapp.data.model.CategoriesModel
import com.akhnaton.atrapp.data.model.OrderTypeModel
import com.akhnaton.atrapp.databinding.ItemCategoryBinding
import com.akhnaton.atrapp.databinding.LayoutOrderTypeItemBinding
import com.akhnaton.atrapp.shared.SharedPreferenceHelper
import com.bumptech.glide.Glide

class OrderTypeAdapter2(
    private val onCategoryClick: (category: CategoriesModel, orderType: String, position: Int) -> Unit
) : RecyclerView.Adapter<OrderTypeAdapter2.ViewHolder>() {

    private var order_type = ArrayList<OrderTypeModel>()

    fun setData(data: List<OrderTypeModel>) {
        order_type = ArrayList(data)
        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: ItemCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private lateinit var currentOrderType: OrderTypeModel

//        private val categoryAdapter = CategoryAdapter { category, position ->
//
//            onCategoryClick(category, currentOrderType.order_type_index, position)
//
//        }

        init {
//            val isArabic = SharedPreferenceHelper.language == "ar"
//
//            binding.recyclerCategories.apply {
//                layoutManager = LinearLayoutManager(
//                    context,
//                    LinearLayoutManager.HORIZONTAL,
//                    isArabic
//                )
//
//                adapter = categoryAdapter
//                setHasFixedSize(true)
//            }
        }


        fun bind(item: OrderTypeModel, position: Int) {
            currentOrderType = item
            Glide.with(itemView.context)
                .load(item.IMAGE_URL)
                .fitCenter()
                .placeholder(R.drawable.ic_logo)
                .error(R.drawable.ic_logo)
                .fallback(R.drawable.ic_logo)
                .into(binding.ivCategory)

            binding.tvCategoryName.text = item.order_type
            binding.root.setOnClickListener {
                onCategoryClick(item.categories[0], item.order_type_index, 0)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCategoryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(order_type[position], position)
    }

    override fun getItemCount(): Int = order_type.size
}
