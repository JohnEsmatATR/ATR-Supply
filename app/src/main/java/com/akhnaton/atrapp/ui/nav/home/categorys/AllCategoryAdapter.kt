package com.akhnaton.atrapp.ui.nav.home.categorys

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.akhnaton.atrapp.data.model.CategoryModel
import com.akhnaton.atrapp.databinding.LayoutAllCategoryBinding

class AllCategoryAdapter() : RecyclerView.Adapter<AllCategoryAdapter.AllCategoryViewHolder>() {

    private var mList = mutableListOf<CategoryModel>()

    fun setCategoriesList(category: List<CategoryModel>) {
        this.mList = category.toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllCategoryViewHolder {
        val binding = LayoutAllCategoryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return AllCategoryViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(holder: AllCategoryViewHolder, position: Int) {
        holder.bind(mList[position])
    }

    class AllCategoryViewHolder(val binding: LayoutAllCategoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: CategoryModel) {
            binding.categoryModel = data
            binding.executePendingBindings()
        }
    }
}