package com.akhnaton.atrapp.ui.nav.cart

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.akhnaton.atrapp.R
import com.akhnaton.atrapp.data.model.CartProduct
import com.akhnaton.atrapp.databinding.LayoutCartBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class CartAdapter(
    private val onClick: (product: CartProduct, position: Int) -> Unit,
    private val onPlusClick: (product: CartProduct, position: Int, quantity: Int) -> Unit,
    private val onMinusClick: (product: CartProduct, position: Int, quantity: Int) -> Unit,
    private val onDeleteClick: (product: CartProduct, position: Int) -> Unit
) : RecyclerView.Adapter<CartAdapter.ViewHolder>() {

    private var productsList = ArrayList<CartProduct>()
    private var isVisible: Boolean = true
    private var type: String = ""
    private var debounceJob: Job? = null


    fun setData(cartProducts: List<CartProduct>, isVisible: Boolean, type: String) {
        productsList.clear()
        productsList.addAll(cartProducts)
        this.isVisible = isVisible
        this.type = type
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: LayoutCartBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: CartProduct, position: Int) {

            binding.cart = item
            binding.quantity = item.myQuantity
            if (isVisible) {
                binding.btnPlus.visibility = View.VISIBLE
                binding.btnMinus.visibility = View.VISIBLE
                binding.deleteItem.visibility = View.VISIBLE
            } else {
                binding.btnPlus.visibility = View.GONE
                binding.btnMinus.visibility = View.GONE
                binding.deleteItem.visibility = View.GONE
            }

            // تحميل الصورة
            binding.imItem.load(item.imageUrl) {
                crossfade(true)
                placeholder(R.drawable.ic_logo)
                error(R.drawable.ic_logo)
            }
            if (item.bonusQuantity.toInt() == 0) {
                binding.txtbonus.visibility = View.GONE
                binding.textView3.visibility=View.GONE
                binding.imageView.visibility = View.GONE
            } else {
                binding.txtbonus.visibility = View.VISIBLE
                binding.imageView.visibility = View.VISIBLE
                binding.textView3.visibility=View.VISIBLE
                binding.txtbonus.text = item.bonusQuantity
            }

            val editQuantity = binding.txtQuantity
            editQuantity.setText(item.myQuantity.toString())
            editQuantity.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(s: Editable?) {
                    val input = s.toString()
                    if (input.isEmpty()) return


                    debounceJob?.cancel()


                    debounceJob = CoroutineScope(Dispatchers.Main).launch {
                        delay(1000)

                        var quantity = input.toIntOrNull() ?: 1

                        if (quantity > item.quantity) {
                            quantity = item.quantity
                            editQuantity.setText(quantity.toString())
                            editQuantity.setSelection(editQuantity.text.length)
                        }

                        if (quantity < 1) {
                            quantity = 1
                            editQuantity.setText(quantity.toString())
                            editQuantity.setSelection(editQuantity.text.length)
                        }

                        onPlusClick(item.copy(myQuantity = quantity), bindingAdapterPosition, quantity)
                    }
                }
            })

            // زر + زيادة الكمية
            binding.btnPlus.isEnabled = item.myQuantity < item.quantity
            binding.btnPlus.setOnClickListener {
                val pos = bindingAdapterPosition
                if (pos != RecyclerView.NO_POSITION) {
                    var quantity = binding.txtQuantity.text.toString().toInt()
                    if (quantity < item.quantity) {
                        quantity++
                        binding.txtQuantity.setText(quantity.toString())
                        onPlusClick(item.copy(myQuantity = quantity), pos, quantity)
                        binding.btnPlus.isEnabled = quantity < item.quantity
                    }
                }
            }

            // زر - نقصان الكمية
            binding.btnMinus.setOnClickListener {
                val pos = bindingAdapterPosition
                if (pos != RecyclerView.NO_POSITION) {
                    var quantity = binding.txtQuantity.text.toString().toInt()
                    if (quantity > 1) {
                        quantity--
                        binding.txtQuantity.setText(quantity.toString())
                        onMinusClick(item.copy(myQuantity = quantity), pos, quantity)
                        binding.btnPlus.isEnabled = quantity < item.quantity
                    }
                }
            }

            // زر الحذف
            binding.deleteItem.setOnClickListener {
                val pos = bindingAdapterPosition
                if (pos != RecyclerView.NO_POSITION) {
                    onDeleteClick(item, pos)
                }
            }

            // كليك على العنصر
            itemView.setOnClickListener {
                val pos = bindingAdapterPosition
                if (pos != RecyclerView.NO_POSITION) {
                    onClick(item, pos)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = LayoutCartBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(productsList[position], position)
    }

    override fun getItemCount(): Int = productsList.size
}
