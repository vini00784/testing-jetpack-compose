package br.senai.sp.jandira.bmicompose.utils

import androidx.compose.ui.graphics.Color

fun getCardColor(bmiResult: Double): Color {
    return if(bmiResult < 18.5)
        Color.Red
    else if(bmiResult in 18.5..25.0)
        Color.Green
    else if(bmiResult in 25.0..30.0)
        Color(235, 145, 12, 255)
    else
        Color.Red
}