package com.akhnaton.atrapp.ui.nav.home.product

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.akhnaton.atrapp.R

class SortProductAdapter(
    private val sortingOptions: List<String>,
    private val onSelected: (Int) -> Unit
) : RecyclerView.Adapter<SortProductAdapter.SortViewHolder>() {

    private var selectedPosition = -1 //malak

    inner class SortViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {

        val title: TextView =
            itemView.findViewById(R.id.tvSortTitle)

        val radioButton: ImageView =
            itemView.findViewById(R.id.imgRadioButton)

        val iconText: TextView =
            itemView.findViewById(R.id.tvIconText)

        val sortIcon: ImageView =
            itemView.findViewById(R.id.imgSortIcon)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SortViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(
                R.layout.item_sort,
                parent,
                false
            )

        return SortViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: SortViewHolder,
        pos: Int
    ) {

        val option = sortingOptions[pos]

        holder.title.text = option
        holder.itemView.isSelected = pos == selectedPosition

        when(pos) {
            0 -> {
                holder.iconText.visibility = View.VISIBLE
                holder.sortIcon.visibility = View.GONE

                holder.iconText.text = "A-Z"
            }

            1 -> {
                holder.iconText.visibility = View.VISIBLE
                holder.sortIcon.visibility = View.GONE

                holder.iconText.text = "Z-A"
            }

            2 -> {
                holder.iconText.visibility = View.GONE
                holder.sortIcon.visibility = View.VISIBLE

                holder.sortIcon.setImageResource(R.drawable.asc)
            }

            3 -> {
                holder.iconText.visibility = View.GONE
                holder.sortIcon.visibility = View.VISIBLE

                holder.sortIcon.setImageResource(R.drawable.des)
            }
        }




        updateSelectedState(
            holder,
            pos == selectedPosition
        )

        holder.itemView.setOnClickListener {

            val oldPosition = selectedPosition

            selectedPosition = pos

            notifyItemChanged(oldPosition)
            notifyItemChanged(selectedPosition)

            onSelected(pos)
        }
    }

    private fun updateSelectedState(
        holder: SortViewHolder,
        selected: Boolean
    ) {

        if (selected) {

            holder.radioButton.setImageResource(
                R.drawable.ic_circle_selected
            )

        } else {

            holder.radioButton.setImageResource(
                R.drawable.ic_circle
            )
        }
    }

    override fun getItemCount(): Int =
        sortingOptions.size
}