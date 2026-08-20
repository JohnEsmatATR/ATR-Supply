package com.akhnaton.atrapp.ui.nav.home.product

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.akhnaton.atrapp.R
import com.akhnaton.atrapp.data.model.OrderTypeModel

class FilterCategoryAdapter(
    private val categories: MutableList<OrderTypeModel>,
    private val onCategoryClick: (Int) -> Unit
) : RecyclerView.Adapter<FilterCategoryAdapter.CategoryViewHolder>() {

    private var selectedPosition = 0
    inner class CategoryViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {

        val container: View =
            itemView.findViewById(R.id.categoryContainer)

        val name: TextView =
            itemView.findViewById(R.id.tvCategory)

        val count: TextView =
            itemView.findViewById(R.id.tvCount)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CategoryViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(
                R.layout.item_filter_category,
                parent,
                false
            )

        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: CategoryViewHolder,
        position: Int
    ) {

        val item = categories[position]
        val selected = position == selectedPosition

        holder.name.text = item.order_type

        if (item.selectedCount > 0) {
            holder.count.visibility = View.VISIBLE
            holder.count.text = item.selectedCount.toString()
        } else {
            holder.count.visibility = View.GONE
        }

        if (selected) {

            holder.container.setBackgroundColor(
                Color.rgb(232, 117, 10)
            )

            holder.name.setTextColor(Color.WHITE)

            holder.count.setTextColor(
                Color.rgb(232, 117, 10)
            )

        } else {

            holder.container.setBackgroundColor(
                Color.TRANSPARENT
            )

            holder.name.setTextColor(
                Color.rgb(80, 80, 80)
            )
        }

        holder.itemView.setOnClickListener {

            val oldPosition = selectedPosition
            selectedPosition = position

            notifyItemChanged(oldPosition)
            notifyItemChanged(selectedPosition)

            onCategoryClick(position)
        }
    }

    override fun getItemCount(): Int = categories.size

    fun setSelectedPosition(position: Int) {

        val oldPosition = selectedPosition

        selectedPosition = position

        notifyItemChanged(oldPosition)
        notifyItemChanged(selectedPosition)
    }
}