package com.akhnaton.atrSupply.ui.nav.cart

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.akhnaton.atrSupply.R
import com.akhnaton.atrSupply.data.model.CartProduct
import com.akhnaton.atrSupply.databinding.LayoutCartArBinding
import com.akhnaton.atrSupply.databinding.LayoutCartBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class CartAdapter(
    private val language: String,
    private val onClick: (product: CartProduct, position: Int) -> Unit,
    private val onPlusClick: (product: CartProduct, position: Int, quantity: Int) -> Unit,
    private val onMinusClick: (product: CartProduct, position: Int, quantity: Int) -> Unit,
    private val onDeleteClick: (product: CartProduct, position: Int) -> Unit
) : RecyclerView.Adapter<CartAdapter.ViewHolder>() {

    private var productsList = ArrayList<CartProduct>()
    private var isVisible: Boolean = true
    private var type: String = ""
    private var debounceJob: Job? = null
    private var isManualChange = false
    private var isProcessing = false

    fun setData(cartProducts: List<CartProduct>, isVisible: Boolean, type: String) {
        productsList.clear()
        productsList.addAll(cartProducts)
        this.isVisible = isVisible
        this.type = type
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: Any) : RecyclerView.ViewHolder(
        if (binding is LayoutCartBinding) binding.root else (binding as LayoutCartArBinding).root
    ) {

        fun bind(item: CartProduct, position: Int) {
            when (binding) {
                is LayoutCartBinding -> bindCommon(binding, item)
                is LayoutCartArBinding -> bindCommon(binding, item)
            }
        }

        private fun bindCommon(binding: LayoutCartBinding, item: CartProduct) {
            binding.cart = item
            binding.quantity = item.myQuantity
            setVisibility(binding.btnPlus, binding.btnMinus, binding.deleteItem)
            binding.imItem.load(item.imageUrl) {
                crossfade(true)
                placeholder(R.drawable.ic_logo)
                error(R.drawable.ic_logo)
            }
            setBonus(binding.txtbonus, binding.imageView, binding.textView3, item)
            handleQuantity(binding.txtQuantity, item)

            binding.btnPlus.isLongClickable = false
            binding.btnMinus.isLongClickable = false

            binding.btnPlus.setOnClickListener { changeQuantity(binding, item, true) }
            binding.btnMinus.setOnClickListener { changeQuantity(binding, item, false) }

            binding.deleteItem.setOnClickListener {
                val pos = bindingAdapterPosition
                if (pos != RecyclerView.NO_POSITION) onDeleteClick(item, pos)
            }

            itemView.setOnClickListener {
                val pos = bindingAdapterPosition
                if (pos != RecyclerView.NO_POSITION) onClick(item, pos)
            }
        }

        private fun bindCommon(binding: LayoutCartArBinding, item: CartProduct) {
            binding.cart = item
            binding.quantity = item.myQuantity
            setVisibility(binding.btnPlus, binding.btnMinus, binding.deleteItem)
            binding.imItem.load(item.imageUrl) {
                crossfade(true)
                placeholder(R.drawable.ic_logo)
                error(R.drawable.ic_logo)
            }
            setBonus(binding.txtbonus, binding.imageView, binding.textView3, item)
            handleQuantity(binding.txtQuantity, item)

            binding.btnPlus.isLongClickable = false
            binding.btnMinus.isLongClickable = false

            binding.btnPlus.setOnClickListener { changeQuantity(binding, item, true) }
            binding.btnMinus.setOnClickListener { changeQuantity(binding, item, false) }

            binding.deleteItem.setOnClickListener {
                val pos = bindingAdapterPosition
                if (pos != RecyclerView.NO_POSITION) onDeleteClick(item, pos)
            }

            itemView.setOnClickListener {
                val pos = bindingAdapterPosition
                if (pos != RecyclerView.NO_POSITION) onClick(item, pos)
            }
        }

        private fun setVisibility(btnPlus: View, btnMinus: View, deleteItem: View) {
            val visibility = if (isVisible) View.VISIBLE else View.GONE
            btnPlus.visibility = visibility
            btnMinus.visibility = visibility
            deleteItem.visibility = visibility
        }

        private fun setBonus(txtBonus: View, image: View, textView: View, item: CartProduct) {
            if (item.bonusQuantity.toInt() == 0) {
                txtBonus.visibility = View.GONE
                image.visibility = View.GONE
                textView.visibility = View.GONE
            } else {
                txtBonus.visibility = View.VISIBLE
                image.visibility = View.VISIBLE
                textView.visibility = View.VISIBLE
                if (txtBonus is TextView) txtBonus.text = item.bonusQuantity
            }
        }

        private fun handleQuantity(editQuantity: EditText, item: CartProduct) {
            editQuantity.setText(item.myQuantity.toString())
            editQuantity.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
                override fun afterTextChanged(s: Editable?) {
                    if (isManualChange) return
                    val input = s.toString()
                    if (input.isEmpty()) return

                    debounceJob?.cancel()
                    debounceJob = CoroutineScope(Dispatchers.Main).launch {
                        delay(1000)
                        var quantity = input.toIntOrNull() ?: 1
                        if (quantity > item.quantity) quantity = item.quantity
                        if (quantity < 1) quantity = 1
                        isManualChange = true
                        editQuantity.setText(quantity.toString())
                        editQuantity.setSelection(editQuantity.text.length)
                        isManualChange = false
                        onPlusClick(item.copy(myQuantity = quantity), bindingAdapterPosition, quantity)
                    }
                }
            })
        }

        private fun changeQuantity(binding: Any, item: CartProduct, isPlus: Boolean) {
            if (isProcessing) return
            isProcessing = true

            val editQuantity: EditText = when (binding) {
                is LayoutCartBinding -> binding.txtQuantity
                is LayoutCartArBinding -> binding.txtQuantity
                else -> {
                    isProcessing = false
                    return
                }
            }

            var quantity = editQuantity.text.toString().toInt()
            if (isPlus && quantity < item.quantity) quantity++
            else if (!isPlus && quantity > 1) quantity--
            else {
                isProcessing = false
                return
            }

            isManualChange = true
            editQuantity.setText(quantity.toString())
            editQuantity.setSelection(editQuantity.text.length)
            isManualChange = false

            if (isPlus) onPlusClick(item.copy(myQuantity = quantity), bindingAdapterPosition, quantity)
            else onMinusClick(item.copy(myQuantity = quantity), bindingAdapterPosition, quantity)

            if (binding is LayoutCartBinding) binding.btnPlus.isEnabled = quantity < item.quantity
            if (binding is LayoutCartArBinding) binding.btnPlus.isEnabled = quantity < item.quantity

            isProcessing = false
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return if (language == "ar") {
            val binding = LayoutCartArBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            ViewHolder(binding)
        } else {
            val binding = LayoutCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            ViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(productsList[position], position)
    }

    override fun getItemCount(): Int = productsList.size
}
