package com.test.wingstest.core.utils

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.widget.Toast
import com.test.wingstest.R
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*


fun String.toCurrencyFormat(): String {
    val formatter = DecimalFormat("###,###")
    return formatter.format(this.toDouble()) + ",-"
}

fun checkStringNullOrNot(text: String?): Boolean =
    text != null && text.trim { it <= ' ' }.isNotBlank() && text != "null"

@SuppressLint("SimpleDateFormat")
fun getCurrentTime(): String {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    return dateFormat.format(Calendar.getInstance().time)
}

fun Context.makeToast(message: String) = Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

fun Context.createAlertDialog(unit: (() -> Unit)? = null){
    val builder = AlertDialog.Builder(this)
    builder.setTitle(this.getString(R.string.checkOut_confirmation))
    builder.setMessage(getString(R.string.checkOut_confirmation_message))
    builder.setPositiveButton(getString(R.string.yes)) { _, _ ->
        unit?.invoke()
    }

    builder.setNegativeButton(getString(R.string.no)) { _, _ ->

    }
    builder.show()
}