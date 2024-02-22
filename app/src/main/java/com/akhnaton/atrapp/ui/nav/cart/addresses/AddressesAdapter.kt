package com.akhnaton.atrapp.ui.nav.cart.addresses

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.akhnaton.atrapp.R
import com.akhnaton.atrapp.data.model.AddressModel
import com.akhnaton.atrapp.databinding.LayoutAddressBinding

class AddressesAdapter(private val onClick: (address: AddressModel, position: Int) -> Unit) :
    RecyclerView.Adapter<AddressesAdapter.ViewHolder>() {

    private var addressesList = ArrayList<AddressModel>()

    fun setData(cart: List<AddressModel>) {
        addressesList = cart as ArrayList
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: LayoutAddressBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: AddressModel, position: Int) {

            binding.address = item

            setChecked(item.prime, binding)

            itemView.setOnClickListener {
                onClick(item, position)
            }

        }

    }

    fun setChecked(prime: Int, binding: LayoutAddressBinding) {
        if (prime == 1) {
            binding.imAddressChecked.visibility = View.GONE
        } else {
            binding.imAddressChecked.visibility = View.VISIBLE
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