package ru.hse.pe.utils

import android.view.View
import android.widget.EditText
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar

object Utils {
    fun String.validateEmail() = android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()

    fun EditText.isInvalid() = this.text.toString().isBlank()

    fun EditText.value() = this.text.toString()

    fun getSnackbar(view: View, message: String) = Snackbar.make(view, message, Snackbar.LENGTH_SHORT).setMaxLines(5)

    fun getLongSnackbar(view: View, message: String) = Snackbar.make(view, message, Snackbar.LENGTH_LONG).setMaxLines(5)

    fun Snackbar.setMaxLines(lines: Int): Snackbar = apply {
        view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text).apply {
            maxLines = lines
            textAlignment = View.TEXT_ALIGNMENT_CENTER
        }
    }

    fun View.setGone() = run { this.visibility = View.GONE }
    fun View.setVisible() = run { this.visibility = View.VISIBLE }
    fun View.setInvisible() = run { this.visibility = View.INVISIBLE }
}