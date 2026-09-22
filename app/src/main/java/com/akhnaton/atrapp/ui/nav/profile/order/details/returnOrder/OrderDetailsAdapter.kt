package com.akhnaton.atrapp.ui.nav.profile.order.details

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.akhnaton.atrapp.R
import com.akhnaton.atrapp.data.model.orderHistory.Item
import com.akhnaton.atrapp.databinding.LayoutOrderItemBinding
import java.util.Locale

class OrderDetailsAdapter :
    RecyclerView.Adapter<OrderDetailsAdapter.ViewHolder>() {

    private var mList = mutableListOf<Item>()

    fun setData(items: List<Item>) {
        mList = items.toMutableList()
        notifyDataSetChanged()
    }

    inner class ViewHolder(
        private val binding: LayoutOrderItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Item) {

            // val context = binding.root.context
            // val lang = SharedPreferenceHelper.language ?: "ar"


            // val unitPrice = if (item.quantity > 0) {
            //     formatPrice(item.price / item.quantity, 2, Locale(lang))
            // } else {
            //     formatPrice(0.0)
            // }

            // val unitPriceText = "$unitPrice ${context.getString(R.string.currency)}"

            /*
            if (lang == "ar") {
                binding.priceLayout.visibility = View.GONE
                binding.textView2.visibility = View.GONE
                binding.quantity.visibility = View.GONE

                binding.textView4.visibility = View.VISIBLE
                binding.quantityar.visibility = View.VISIBLE
                binding.pricerAr.visibility = View.VISIBLE
                binding.priceTxtar.visibility = View.VISIBLE

                // binding.unitPriceAr.text = unitPriceText

            } else {
                binding.priceLayout.visibility = View.VISIBLE
                binding.textView2.visibility = View.VISIBLE
                binding.quantity.visibility = View.VISIBLE

                binding.textView4.visibility = View.GONE
                binding.quantityar.visibility = View.GONE
                binding.pricerAr.visibility = View.GONE
                binding.priceTxtar.visibility = View.GONE

                // binding.unitPrice.text = unitPriceText
            }
            */


            binding.data = item

            val context = binding.root.context

            val currency = context.getString(R.string.LE_format)
            val taxLabel = context.getString(R.string.tax)
            val priceWithTaxLabel = context.getString(R.string.price_with_tax)
            val unitSuffix = context.getString(R.string.unit_price_suffix)

            val taxValue = item.TOTAL_TAX?.toDouble() ?: 0.0
            binding.itemTax.text = "$taxLabel: ${String.format(Locale.US, "%.2f", taxValue)} $currency" /////////newww malakkk

            val totalPrice = item.TOTAL_UNIT_PRICE_WITH_TAX?.toDouble() ?: 0.0
            binding.price.text = "${String.format(Locale.US, "%.2f", totalPrice)} $currency" /////////newww malakkk

            val unitPrice = item.UNIT_PRICE_WITHOUT_TAX?.toDouble() ?: 0.0
            binding.unitPriceDisplay.text = "(${String.format(Locale.US, "%.2f", unitPrice)} $unitSuffix)" /////////newww malakkk

            val priceWithTaxValue = item.UNIT_PRICE_WITH_TAX?.toDouble() ?: 0.0
            binding.priceWithTax.text = "$priceWithTaxLabel: ${String.format(Locale.US, "%.2f", priceWithTaxValue)} $currency"

            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {

        val binding = LayoutOrderItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        holder.bind(mList[position])
    }

    override fun getItemCount(): Int {
        return mList.size
    }
}