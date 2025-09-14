package com.akhnaton.atrapp.ui.nav.cart.checkout.paymentMethod

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.akhnaton.atrapp.data.model.PaymentModel
import com.akhnaton.atrapp.databinding.ItemPaymentBinding

class PaymentAdapter (private val onClick: (paymentModel: PaymentModel, position: Int,) -> Unit) :
    RecyclerView.Adapter<PaymentAdapter.ViewHolder>() {

    private var apartmentsList = ArrayList<PaymentModel>()

    fun setData(categories: List<PaymentModel>) {
        apartmentsList = categories as java.util.ArrayList
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: ItemPaymentBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: PaymentModel, position: Int) {

            binding.paymentType = item

            itemView.setOnClickListener {
                onClick(item, position)
            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemPaymentBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        apartmentsList?.get(position)?.let { holder.bind(it, position) }
    }

    override fun getItemCount(): Int {
        return apartmentsList.size
    }
}

