package com.akhnaton.atrSupply.ui.nav.cart.addresses

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.akhnaton.atrSupply.data.model.AddressModel
import com.akhnaton.atrSupply.databinding.LayoutAddressBinding

class AddressesAdapter(private val onClick: (address: AddressModel, position: Int) -> Unit) :
    RecyclerView.Adapter<AddressesAdapter.ViewHolder>() {

    private var addressesList = ArrayList<AddressModel>()

    fun setData(newList: List<AddressModel>) {
        addressesList.clear()
        Log.d("TAG", "setData: ${newList}")
        addressesList.addAll(newList)
        notifyDataSetChanged()
    }


    inner class ViewHolder(private val binding: LayoutAddressBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: AddressModel, position: Int) {
            binding.address = item
            binding.executePendingBindings()
            itemView.setOnClickListener {
                onClick(item, position)
            }
            Log.d("DEBUG", "Binding TITLE: ${item.TITLE}")
            if (item.prime == 1) {
                binding.card.setStrokeColor(Color.parseColor("#EE7907"))
            }



        }

    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = LayoutAddressBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        addressesList?.get(position)?.let { holder.bind(it, position) }
    }

    override fun getItemCount(): Int {
        return addressesList.size
    }

}