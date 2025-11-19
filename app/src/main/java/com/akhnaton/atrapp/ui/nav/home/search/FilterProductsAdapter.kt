package com.akhnaton.atrapp.ui.nav.home.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.akhnaton.atrapp.R
import com.akhnaton.atrapp.data.model.CategoryModel

class FilterProductsAdapter(
    private val onClick: (categoryId: Int, categoryTitle: String) -> Unit,
    private var categories: List<CategoryModel>
) : RecyclerView.Adapter<FilterProductsAdapter.ViewHolderProducts>() {

    private var selectedPosition = -1

    inner class ViewHolderProducts(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val filterTitle: TextView = itemView.findViewById(R.id.filter_all)

        init {
            itemView.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    selectedPosition = position
                    notifyDataSetChanged()
                    val item = categories[position]
                    onClick(item.ID, item.TITLE)
                }
            }
        }
    }
    fun updateCategories(newCategories: List<CategoryModel>) {
        categories = newCategories
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderProducts {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_filter, parent, false)
        return ViewHolderProducts(view)
    }

    override fun onBindViewHolder(holder: ViewHolderProducts, position: Int) {
        val category = categories[position]
        holder.filterTitle.text = category.TITLE

        val context = holder.itemView.context
        val colorRes = if (position == selectedPosition) R.color.orange else R.color.black
        holder.filterTitle.setTextColor(ContextCompat.getColor(context, colorRes))
    }

    override fun getItemCount(): Int = categories.size
}
