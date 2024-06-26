package com.akhnaton.atrapp.ui.nav.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.akhnaton.atrapp.R
import com.akhnaton.atrapp.data.model.CategoryModel
import com.akhnaton.atrapp.databinding.LayoutCategoryBinding
import com.akhnaton.atrapp.shared.Common
import java.util.ArrayList

class CategoryAdapter(private val onClick: (category: CategoryModel, position: Int) -> Unit) :
    RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    private var apartmentsList = ArrayList<CategoryModel>()

    fun setData(categories: List<CategoryModel>) {
        apartmentsList = categories as ArrayList
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: LayoutCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: CategoryModel, position: Int) {

            binding.categoryModel = item

            binding.imItem.load(item.IMAGE_URL) {
                crossfade(true)
                placeholder(R.drawable.ic_logo)
            }


            itemView.setOnClickListener {
                onClick(item, position)
            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = LayoutCategoryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        apartmentsList?.get(position)?.let { holder.bind(it, position) }
    }

    override fun getItemCount(): Int {
        return apartmentsList.size
    }

}