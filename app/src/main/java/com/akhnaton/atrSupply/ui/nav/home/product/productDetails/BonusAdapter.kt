package com.akhnaton.atrSupply.ui.nav.home.product.productDetails

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.akhnaton.atrSupply.data.model.BonusData
import com.akhnaton.atrSupply.databinding.BonusItemArBinding
import com.akhnaton.atrSupply.databinding.BonusItemBinding

class BonusAdapter(
    private val language: String,
    private var items: List<BonusData> = emptyList()
) : RecyclerView.Adapter<BonusAdapter.BonusViewHolder>() {

    inner class BonusViewHolder(private val binding: Any) : RecyclerView.ViewHolder(
        if (binding is BonusItemBinding) binding.root else (binding as BonusItemArBinding).root
    ) {
        fun bind(item: BonusData) {
            when (binding) {
                is BonusItemBinding -> {
                    binding.bonusData = item
                    binding.executePendingBindings()
                }
                is BonusItemArBinding -> {
                    binding.bonusData = item
                    binding.executePendingBindings()
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BonusViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (language == "ar") {
            val binding = BonusItemArBinding.inflate(inflater, parent, false)
            BonusViewHolder(binding)
        } else {
            val binding = BonusItemBinding.inflate(inflater, parent, false)
            BonusViewHolder(binding)
        }
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
