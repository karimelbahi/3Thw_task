package com.example.task.presentation.utils

import android.view.View
import com.google.android.material.snackbar.Snackbar
import java.sql.Time
import java.text.SimpleDateFormat
import java.util.*

/**
 * Pattern: yyyy-MM-dd HH:mm:ss
 */
fun Long.convertLongToStrDate(): String {
    val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    return format.format(this)
}

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun View.gone() {
    visibility = View.GONE
}


inline fun View.snack(message: String, length: Int = Snackbar.LENGTH_LONG, f: Snackbar.() -> Unit) {
    val snack = Snackbar.make(this, message, length)
    snack.f()
    snack.show()
}