package ru.netology.myapp

import java.text.DecimalFormat

fun numberToString(number: Int): String {
    return when {
        number < 1000 -> number.toString()
        number < 1099 -> "1K"
        number < 10000 -> DecimalFormat("#0.0").format(number/1000.0)+"K"
        number < 100000 -> (number / 1000).toString()+"K"
        number < 110000 -> "1M"
        else -> DecimalFormat("#0.0").format(number/100000.0)+"M"
    }
}