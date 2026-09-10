package com.akhnaton.atrapp.ui.nav.home.product

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.akhnaton.atrapp.R
import com.akhnaton.atrapp.data.model.CategoriesModel
import com.google.android.material.checkbox.MaterialCheckBox

class FilterProductAdapter(
    private val categories: MutableList<CategoriesModel>,
    private val onSelected: (Int, Boolean) -> Unit
) : RecyclerView.Adapter<FilterProductAdapter.ProductViewHolder>() {

    private var selectedPosition: Int = -1 //malak

    inner class ProductViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {

        val container: View =
            itemView.findViewById(R.id.productContainer)

        val name: TextView =
            itemView.findViewById(R.id.tvProduct)

        val checkbox: MaterialCheckBox =
            itemView.findViewById(R.id.checkBox)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProductViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(
                R.layout.item_filter_product,
                parent,
                false
            )

        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: ProductViewHolder,
        position: Int
    ) {
        val product = categories[position]
        holder.name.text = product.TITLE

        val isSelected = position == selectedPosition //malak

        holder.checkbox.setOnCheckedChangeListener(null)
        holder.checkbox.isChecked = isSelected //malak
        updateSelectedState(holder, isSelected) //malak

        val clickListener = View.OnClickListener { //malak
            val currentPosition = holder.bindingAdapterPosition // malak
            if (currentPosition == RecyclerView.NO_POSITION || currentPosition >= categories.size) {
                return@OnClickListener
            } //malak

            val previousSelected = selectedPosition // malak

            if (selectedPosition == currentPosition) { // malak
                selectedPosition = RecyclerView.NO_POSITION
                notifyItemChanged(currentPosition)
                onSelected(-1, false)
            } else {
                selectedPosition = currentPosition
                if (previousSelected != RecyclerView.NO_POSITION) {
                    notifyItemChanged(previousSelected)
                }
                notifyItemChanged(selectedPosition) //malak
                onSelected(selectedPosition, true)
            }
        }

        holder.itemView.setOnClickListener(clickListener)
        holder.checkbox.setOnClickListener(clickListener)
    }

    private fun updateSelectedState(
        holder: ProductViewHolder,
        selected: Boolean
    ) {

        if (selected) {

            holder.container.background =
                ContextCompat.getDrawable(
                    holder.itemView.context,
                    R.drawable.bg_filter_item_selected
                )

            holder.name.setTextColor(
                Color.rgb(180, 95, 10)
            )

        } else {

            holder.container.background =
                ContextCompat.getDrawable(
                    holder.itemView.context,
                    R.drawable.bg_filter_item
                )

            holder.name.setTextColor(
                Color.rgb(50, 50, 50)
            )
        }
    }

    override fun getItemCount(): Int = categories.size

    fun setSelectedPosition(position: Int){
        this.selectedPosition = position
        notifyDataSetChanged()
    }
}