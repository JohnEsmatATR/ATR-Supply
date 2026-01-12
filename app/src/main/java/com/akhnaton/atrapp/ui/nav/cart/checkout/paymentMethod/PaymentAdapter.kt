package com.akhnaton.atrapp.ui.nav.cart.checkout.paymentMethod

import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.akhnaton.atrapp.R
import com.akhnaton.atrapp.data.model.PaymentModel
import com.akhnaton.atrapp.databinding.ItemPaymentBinding

class PaymentAdapter (private val onClick: (paymentModel: PaymentModel, position: Int,) -> Unit) :
    RecyclerView.Adapter<PaymentAdapter.ViewHolder>() {

    private var apartmentsList = ArrayList<PaymentModel>()
    private var selectedPosition = -1

    fun setData(categories: List<PaymentModel>) {
        apartmentsList = categories as java.util.ArrayList
        selectedPosition = -1 // Reset selection when data changes
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: ItemPaymentBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: PaymentModel, position: Int) {

            binding.paymentType = item

            // Update visual state based on selection
            val isSelected = position == selectedPosition
            updateSelectionState(isSelected)

            itemView.setOnClickListener {
                val previousSelected = selectedPosition
                selectedPosition = position
                
                // Notify previous and current item to update their UI
                if (previousSelected != -1 && previousSelected != position) {
                    notifyItemChanged(previousSelected)
                }
                notifyItemChanged(position)
                
                onClick(item, position)
            }

        }

        private fun updateSelectionState(isSelected: Boolean) {
            val context = binding.root.context
            val dpToPx = { dp: Float ->
                TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    dp,
                    context.resources.displayMetrics
                ).toInt()
            }
            
            if (isSelected) {
                // Selected state: thicker stroke (3dp), light gray background
                binding.cardItem.strokeWidth = dpToPx(3f)
                binding.cardItem.setCardBackgroundColor(
                    ContextCompat.getColor(context, R.color.lightGray)
                )
            } else {
                // Unselected state: normal stroke (1dp), white background
                binding.cardItem.strokeWidth = dpToPx(1f)
                binding.cardItem.setCardBackgroundColor(
                    ContextCompat.getColor(context, R.color.white)
                )
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

