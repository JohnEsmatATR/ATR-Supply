package com.akhnaton.atrapp.ui.nav.cart.checkout.paymentMethod

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import com.akhnaton.atrapp.databinding.ActivityAddCardBinding
import com.akhnaton.atrapp.shared.BaseActivity

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

}