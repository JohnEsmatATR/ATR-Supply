package com.akhnaton.atrapp.util

import java.text.NumberFormat
import java.util.Locale

fun formatPrice(
    value: Double,
    fractionDigits: Int = 2,
    locale: Locale = Locale.getDefault()
): String {
    val formatter = NumberFormat.getNumberInstance(locale).apply {
        minimumFractionDigits = fractionDigits
        maximumFractionDigits = fractionDigits
    }
    return formatter.format(value)
}
