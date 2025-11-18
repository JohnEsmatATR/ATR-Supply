package com.akhnaton.atrSupply.ui.nav.home.categories

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.akhnaton.atrSupply.data.model.CategoriesModel
import com.akhnaton.atrSupply.databinding.LayoutAllCategoryBinding
import com.bumptech.glide.Glide

class AllCategoryAdapter() : RecyclerView.Adapter<AllCategoryAdapter.AllCategoryViewHolder>() {

    private lateinit var listener: OnCategoryClickListener
    private var mList = mutableListOf<CategoriesModel>()


    fun setCategoriesList(category: List<CategoriesModel>, listener: OnCategoryClickListener) {
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
        fun bind(data: CategoriesModel) {
            binding.categoryModel = data
            Glide.with(binding.root.context)
                .load(data.IMAGE_URL)
                .into(binding.imItem)
            binding.cardItem.setOnClickListener {
                listener.onCategoryClick(data)
                binding.executePendingBindings()
            }
        }
    }
    interface OnCategoryClickListener {
        fun onCategoryClick(category: CategoriesModel)
    }
}