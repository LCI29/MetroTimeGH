package ru.clementl.metrotimex.utils

fun Double.salaryStyle(): String {
    return String.format("%.2f \u20BD", this)
}

fun String.toDoubleEmptyZero(): Double = if (isEmpty()) 0.0 else toDouble()