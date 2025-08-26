package com.akhnaton.atrapp.ui.nav.home.product.productDetails

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.akhnaton.atrapp.data.model.BonusData
import com.akhnaton.atrapp.databinding.BonusItemBinding

class BonusAdapter(
    private var items: List<BonusData> = emptyList()
) : RecyclerView.Adapter<BonusAdapter.BonusViewHolder>() {

    inner class BonusViewHolder(val binding: BonusItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: BonusData) {
            binding.bonusData = item
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BonusViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = BonusItemBinding.inflate(layoutInflater, parent, false)
        return BonusViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BonusViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    fun setData(newList: List<BonusData>) {
        items = newList
        notifyDataSetChanged()
    }

}

