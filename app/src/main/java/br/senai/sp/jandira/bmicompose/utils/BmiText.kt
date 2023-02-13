package br.senai.sp.jandira.bmicompose.utils

import br.senai.sp.jandira.bmicompose.R

fun getBmiText(bmiResult: Double): String {
    return if(bmiResult < 18.5)
        R.string.underweight.toString()
    else if(bmiResult in 18.5..25.0)
        R.string.normal_weight.toString()
    else if(bmiResult in 25.0..30.0)
        R.string.overweight.toString()
    else if(bmiResult in 30.0..35.0)
        R.string.obesity_one.toString()
    else if(bmiResult in 35.0..40.0)
        R.string.obesity_two.toString()
    else
        R.string.obesity_three.toString()

}