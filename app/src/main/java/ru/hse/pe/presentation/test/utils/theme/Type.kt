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
    Font(R.font.montserrat_regular, weight = FontWeight.Normal)
)

val nunitoSans = FontFamily(
    Font(R.font.nunito_sans, weight = FontWeight.Normal)
)

val montserratTypography = Typography(
    subtitle1 = TextStyle(
        fontFamily = montserrat,
        fontWeight = FontWeight.Normal,
        fontSize = 18.sp
    )
)

val nunitoSansTypography = Typography(
    subtitle1 = TextStyle(
        fontFamily = nunitoSans,
        fontWeight = FontWeight.Normal,
        fontSize = 20.sp
    )
)