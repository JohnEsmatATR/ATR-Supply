package com.akhnaton.atrapp.ui.nav.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.akhnaton.atrapp.data.model.CategoriesModel
import com.akhnaton.atrapp.databinding.LayoutCategoryBinding
import com.bumptech.glide.Glide

class CategoryAdapter(
    private val onClick: (category: CategoriesModel, position: Int) -> Unit
) : RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    private var categoriesList = ArrayList<CategoriesModel>()

    fun setData(categories: List<CategoriesModel>) {
        categoriesList = ArrayList(categories)
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: LayoutCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: CategoriesModel, position: Int) {
            binding.categoryModel = item

            Glide.with(binding.root.context)
                .load(item.IMAGE_URL)
                .into(binding.imItem)

            binding.root.setOnClickListener {
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
        holder.bind(categoriesList[position], position)
    }

    override fun getItemCount(): Int = categoriesList.size
}
