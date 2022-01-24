package com.example.task.presentation.utils

import android.content.Context
import android.view.View
import androidx.preference.PreferenceManager
import com.example.task.presentation.utils.Constants.MOCK_TIME_NUM_KEY
import com.example.task.presentation.utils.Constants.NUM_OF_MOCKED_HORS
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.*


// date
/**
 * Pattern: yyyy-MM-dd HH:mm:ss
 */
fun Long.convertLongToStrDate(): String {
    val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    return format.format(this)
}

fun Long.mockDate(context: Context): Long {

    val currentTime = System.currentTimeMillis()

    /**seconds = diff / 1000
    minutes = seconds / 60
    hours = minutes / 60
    days = hours / 24*/
    val diff: Long = (this - currentTime) / (1000 * 60 * 60 * 24)


    return if (diff > 1) {
        val mockTimeNum = getSharedPreferences(MOCK_TIME_NUM_KEY, NUM_OF_MOCKED_HORS, context)
        saveSharedPreferences(MOCK_TIME_NUM_KEY, mockTimeNum + NUM_OF_MOCKED_HORS, context)
        // current time plus num of mock hours (second * minutes * hours)
        currentTime + (mockTimeNum * (60 * 60 * 1000))
    } else
        this
}


// shared pref
fun saveSharedPreferences(key: String, value: Int, context: Context) {
    val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
    editor.putInt(key, value)
    editor.apply()
}

fun getSharedPreferences(key: String, defValue: Int, context: Context): Int {
    val editor = PreferenceManager.getDefaultSharedPreferences(context)
    return editor.getInt(key, defValue)
}


// view
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



