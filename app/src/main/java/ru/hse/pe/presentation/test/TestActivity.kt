package ru.hse.pe.presentation.test

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import ru.hse.pe.domain.model.TestItem
import ru.hse.pe.presentation.test.utils.theme.TestTheme


class TestActivity : ComponentActivity() {
    public lateinit var testItem: TestItem
    val context = this@TestActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TestTheme {
                Navigation(context)
            }
        }
    }
}









