package ru.hse.pe.presentation.test.utils.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import ru.hse.pe.R

// Set of Material typography styles to start with
val montserrat = FontFamily(
    Font(R.font.montserrat, weight = FontWeight.Normal)
)


val montserratTypography = Typography(
    h1 = TextStyle(
        fontFamily = montserrat,
        fontWeight = FontWeight.Normal,
        fontSize = 18.sp
    )
)