package ru.hse.pe.utils

import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import ru.hse.pe.R
import ru.hse.pe.TopAppBarFragment

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

    fun AppCompatActivity.openFragment(holder: Int, fragment: Fragment){
        supportFragmentManager
            .beginTransaction()
            .replace(holder, fragment)
            .commit()
    }

    fun View.setGone() = run { this.visibility = View.GONE }
    fun View.setVisible() = run { this.visibility = View.VISIBLE }
    fun View.setInvisible() = run { this.visibility = View.INVISIBLE }
}