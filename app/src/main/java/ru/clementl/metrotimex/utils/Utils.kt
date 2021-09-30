package ru.clementl.metrotimex.utils

import android.app.Activity
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment

const val LOG_TAG = "MetroTime"

fun logd(message: String) {
    Log.d(LOG_TAG, message)
}

fun Fragment.showToast(message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

fun Activity.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Fragment.stringFrom(resId: Int): String {
    return requireActivity().getString(resId)
}