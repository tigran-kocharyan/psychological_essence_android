package ru.hse.pe.utils

import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.google.accompanist.systemuicontroller.SystemUiController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.android.material.snackbar.Snackbar
import ru.hse.pe.R

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

    @Composable
    fun SystemBarsNotVisible() {
        val systemUiController: SystemUiController = rememberSystemUiController()
        systemUiController.isSystemBarsVisible = false
    }


    @Composable
    fun MyTopAppBar(name: String, arrow: Boolean) {
        Card(
            backgroundColor = colorResource(id = R.color.purple),
            contentColor = Color.White,
            modifier = Modifier.height(78.dp),
            shape = RoundedCornerShape(bottomStart = 15.dp, bottomEnd = 15.dp),
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            )
            {
                if(arrow) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_arrow_back),
                        contentDescription = "arrowLeft"
                    )
                }
                Text(
                    text = name,
                    modifier = Modifier.padding(horizontal = 30.dp).width(250.dp),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.subtitle1
                )
                if(arrow) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_question),
                        contentDescription = "question"
                    )
                }
            }
        }
    }
}