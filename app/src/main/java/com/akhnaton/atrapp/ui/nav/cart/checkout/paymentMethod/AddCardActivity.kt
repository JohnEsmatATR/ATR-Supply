package com.akhnaton.atrapp.ui.nav.cart.checkout.paymentMethod

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import com.akhnaton.atrapp.databinding.ActivityAddCardBinding
import com.akhnaton.atrapp.shared.BaseActivity
import java.util.Calendar

class AddCardActivity : BaseActivity() {

    private lateinit var binding: ActivityAddCardBinding

    private val cardNumberTotalSymbols = 19
    private val cardNumberTotalDigits = 16
    private val cardNumberDividerModulo = 5
    private val cardNumberDividerPosition = cardNumberDividerModulo - 1
    private val cardNumberDivider = '-'

    private val cardDateTotalSymbols = 5
    private val cardDateTotalDigits = 4
    private val cardDateDividerModulo = 3
    private val cardDateDividerPosition = cardDateDividerModulo - 1
    private val cardDateDivider = '/'

    private val cardCVCTotalSymbols = 3

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddCardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
        onClick()
        binding.btnCheckout.setOnClickListener {
            val name = binding.txtCardHolderName.text.toString()
            val number = binding.txtCardNumber.text.toString()
            val expiry = binding.txtExpiryDate.text.toString()
            val cvv = binding.txtCvv.text.toString()

            if (!isValidCardHolderName(name)) {
                binding.txtCardHolderName.error = "Please enter a valid name"
                return@setOnClickListener
            }

            if (!isValidCardNumber(number)) {
                binding.txtCardNumber.error = "Invalid card number"
                return@setOnClickListener
            }

            if (!isValidExpiryDate(expiry)) {
                binding.txtExpiryDate.error = "Invalid expiry date"
                return@setOnClickListener
            }

            if (!isValidCVV(cvv)) {
                binding.txtCvv.error = "Invalid CVV"
                return@setOnClickListener
            }


            Toast.makeText(this, "Card is valid!", Toast.LENGTH_SHORT).show()
        }

    }

    private fun init() {

    }

    private fun onClick() {
        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.txtCardNumber.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (!isInputCorrect(s, cardNumberTotalSymbols, cardNumberDividerModulo, cardNumberDivider)) {
                    s?.replace(0, s.length, concatString(getDigitArray(s, cardNumberTotalDigits), cardNumberDividerPosition, cardNumberDivider))
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        binding.txtExpiryDate.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (!isInputCorrect(s, cardDateTotalSymbols, cardDateDividerModulo, cardDateDivider)) {
                    s?.replace(0, s.length, concatString(getDigitArray(s, cardDateTotalDigits), cardDateDividerPosition, cardDateDivider))
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        binding.txtCvv.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if ((s?.length ?: 0) > cardCVCTotalSymbols) {
                    s?.delete(cardCVCTotalSymbols, s.length)
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun isInputCorrect(s: Editable?, size: Int, dividerPosition: Int, divider: Char): Boolean {
        var isCorrect = (s?.length ?: 0) <= size
        s?.forEachIndexed { index, c ->
            isCorrect = if (index > 0 && (index + 1) % dividerPosition == 0) {
                isCorrect && divider == c
            } else {
                isCorrect && Character.isDigit(c)
            }
        }
        return isCorrect
    }

    private fun concatString(digits: CharArray, dividerPosition: Int, divider: Char): String {
        val formatted = StringBuilder()

        digits.forEachIndexed { index, c ->
            if (c != 0.toChar()) {
                formatted.append(c)
                if (index > 0 && index < digits.size - 1 && (index + 1) % dividerPosition == 0) {
                    formatted.append(divider)
                }
            }
        }

        return formatted.toString()
    }

    private fun getDigitArray(s: Editable?, size: Int): CharArray {
        val digits = CharArray(size)
        var index = 0
        s?.forEach { c ->
            if (Character.isDigit(c)) {
                try {
                    digits[index] = c
                    index++
                } catch (_: Exception){}
            }
        }
        return digits
    }

    // Card Validation
    private fun isValidCardHolderName(name: String): Boolean {
        return name.trim().isNotEmpty() && name.matches(Regex("^[a-zA-Z\\s]{2,40}$"))
    }
    private fun isValidCardNumber(number: String): Boolean {
        val cleanNumber = number.replace("-", "")
        if (cleanNumber.length != 16) return false
        return isValidLuhn(cleanNumber)
    }
    private fun isValidLuhn(number: String): Boolean {
        var sum = 0
        var alternate = false
        for (i in number.length - 1 downTo 0) {
            var n = number[i].toString().toInt()
            if (alternate) {
                n *= 2
                if (n > 9) n -= 9
            }
            sum += n
            alternate = !alternate
        }
        return sum % 10 == 0
    }
    private fun isValidExpiryDate(date: String): Boolean {
        if (!date.matches(Regex("^\\d{2}/\\d{2}$"))) return false

        val parts = date.split("/")
        val month = parts[0].toIntOrNull() ?: return false
        val year = parts[1].toIntOrNull() ?: return false

        if (month !in 1..12) return false

        val currentYear = Calendar.getInstance().get(Calendar.YEAR) % 100
        val currentMonth = Calendar.getInstance().get(Calendar.MONTH) + 1

        return year > currentYear || (year == currentYear && month >= currentMonth)
    }
    private fun isValidCVV(cvv: String): Boolean {
        return cvv.length == 3 && cvv.all { it.isDigit() }
    }


}