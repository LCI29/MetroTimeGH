package ru.clementl.metrotimex.utils

import android.content.Context
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