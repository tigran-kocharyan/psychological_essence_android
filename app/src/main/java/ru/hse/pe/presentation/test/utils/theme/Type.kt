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
    Font(R.font.nunitosans_regular, weight = FontWeight.Normal)
)

val typography = Typography(
    subtitle1 = TextStyle(
        fontFamily = montserrat,
        fontWeight = FontWeight.Normal,
        fontSize = 18.sp
    ),
    subtitle2 = TextStyle(
        fontFamily = nunitoSans,
        fontWeight = FontWeight.Normal,
        fontSize = 20.sp
    ),
    h3 = TextStyle(
        fontFamily = montserrat,
        fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp,
        lineHeight = 26.sp
    ),
    h4 = TextStyle(
        fontFamily = montserrat,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 18.sp
    )


)

val nunitoSansTypography = Typography(
    subtitle1 = TextStyle(
        fontFamily = nunitoSans,
        fontWeight = FontWeight.Normal,
        fontSize = 20.sp
    )
)