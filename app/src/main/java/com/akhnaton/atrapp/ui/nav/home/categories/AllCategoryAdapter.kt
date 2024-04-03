package com.akhnaton.atrapp.ui.nav.home.categories

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.akhnaton.atrapp.data.model.CategoryModel
import com.akhnaton.atrapp.databinding.LayoutAllCategoryBinding

class AllCategoryAdapter() : RecyclerView.Adapter<AllCategoryAdapter.AllCategoryViewHolder>() {

    private lateinit var listener: OnCategoryClickListener
    private var mList = mutableListOf<CategoryModel>()


    fun setCategoriesList(category: List<CategoryModel>, listener: OnCategoryClickListener) {
        this.mList = category.toMutableList()
        this.listener = listener
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllCategoryViewHolder {
        val binding = LayoutAllCategoryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return AllCategoryViewHolder(binding, listener)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(holder: AllCategoryViewHolder, position: Int) {
        holder.bind(mList[position])
    }

    class AllCategoryViewHolder(
        val binding: LayoutAllCategoryBinding,
        private val listener: OnCategoryClickListener
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: CategoryModel) {
            binding.categoryModel = data
            binding.cardItem.setOnClickListener {
                listener.onCategoryClick(data)
                binding.executePendingBindings()
            }
        }
    }
    interface OnCategoryClickListener {
        fun onCategoryClick(category: CategoryModel)
    }
}