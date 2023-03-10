package br.senai.sp.jandira.bmicompose

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateOffsetAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.interaction.FocusInteraction
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.sharp.Add
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.senai.sp.jandira.bmicompose.ui.theme.BMIComposeTheme
import br.senai.sp.jandira.bmicompose.utils.bmiCalculate
import br.senai.sp.jandira.bmicompose.utils.getBmiText
import br.senai.sp.jandira.bmicompose.utils.getCardColor
import java.text.DecimalFormat

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BMIComposeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    BMICalculator()
                }
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun BMICalculator() {

    var weightState by rememberSaveable() {
        mutableStateOf("")
    }

    var heightState by rememberSaveable() {
        mutableStateOf("")
    }

    var footerExpandState by remember {
        mutableStateOf(false)
    }

    var bmiResult by remember {
        mutableStateOf(0.0)
    }

    var weightError by remember {
        mutableStateOf(false)
    }

    var heightError by remember {
        mutableStateOf(false)
    }

    // Object that control the focus request
    val weightFocusRequester = FocusRequester()

    Column( // Container
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {

        Column( // Header
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.bmi),
                contentDescription = "BMI Icon",
                modifier = Modifier.size(100.dp)
            )
            Text(
                text = stringResource(id = R.string.app_title),
                color = Color.Blue,
                fontSize = 36.sp,
                letterSpacing = 4.sp
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 32.dp, end = 32.dp, top = 36.dp)
        ) { // Form
            Text(
                text = stringResource(id = R.string.weight),
                modifier = Modifier.padding(bottom = 8.dp),
                fontSize = 16.sp
            )
            OutlinedTextField(
                value = weightState,
                onValueChange = {
                    var lastChar = if (it.isEmpty()) {
                        weightError = true
                        it
                    }
                    else {
                        it[it.length - 1]
                        weightError = false
                    }

                    var newValue = if (lastChar == '.' || lastChar == ',')
                        it.dropLast(1)
                    else
                        it

                    weightState = newValue
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(weightFocusRequester),
                leadingIcon = {
                    Icon(imageVector = Icons.Filled.Email, contentDescription = "")
                },
                trailingIcon = {
                    if(weightError)
                        Icon(imageVector = Icons.Default.Error, contentDescription = "")
                },
                isError = weightError,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )
            AnimatedVisibility(
                visible = weightError,
                enter = slideInVertically(),
                exit = slideOutVertically() + shrinkVertically()
            ) {
                Text(
                    text = stringResource(id = R.string.required_height),
                    color = MaterialTheme.colors.error,
                    style = MaterialTheme.typography.caption,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.End
                )
            }
//            if(weightError) {
//                Text(
//                    text = stringResource(id = R.string.required_weight),
//                    color = MaterialTheme.colors.error,
//                    style = MaterialTheme.typography.caption,
//                    modifier = Modifier.fillMaxWidth(),
//                    textAlign = TextAlign.End
//                )
//            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(id = R.string.height),
                modifier = Modifier.padding(bottom = 8.dp),
                fontSize = 16.sp
            )
            OutlinedTextField(
                value = heightState,
                onValueChange = {
                    var lastChar = if (it.isEmpty()) {
                        heightError = true
                        it
                    }
                    else {
                        it[it.length - 1]
                        heightError = false
                    }

                    var newValue = if (lastChar == '.' || lastChar == ',')
                        it.dropLast(1)
                    else
                        it

                    heightState = newValue
                },
                isError = heightError,
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = {
                    if(heightError)
                        Icon(imageVector = Icons.Default.Error, contentDescription = "")
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )
            AnimatedVisibility(
                visible = heightError,
                enter = slideInVertically(),
                exit = slideOutVertically() + shrinkVertically()
            ) {
                Text(
                    text = stringResource(id = R.string.required_height),
                    color = MaterialTheme.colors.error,
                    style = MaterialTheme.typography.caption,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.End
                )
            }
//            if(heightError) {
//                Text(
//                    text = stringResource(id = R.string.required_height),
//                    color = MaterialTheme.colors.error,
//                    style = MaterialTheme.typography.caption,
//                    modifier = Modifier.fillMaxWidth(),
//                    textAlign = TextAlign.End
//                )
//            }

            Button(
                onClick = {
                    weightError = weightState.isEmpty()
                    heightError = heightState.isEmpty()

                    if(!heightError && !weightError) {
                        bmiResult = bmiCalculate(weightState.toInt(), heightState.toDouble())
                        footerExpandState = true
                    }

                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 32.dp)
                    .height(48.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(Color(40, 150, 30))
            ) {
                Text(
                    text = stringResource(id = R.string.button_calculate),
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(32.dp))
        }

        // Footer
        AnimatedVisibility(
            visible = footerExpandState,
            enter = expandVertically(expandFrom = Alignment.Bottom) {
                20
            },
            exit = shrinkVertically(shrinkTowards = Alignment.Bottom)
//            enter = slideIn(tween(durationMillis = 500)) {
//                IntOffset(it.width, 100)
//            },
//            exit = slideOut(tween(durationMillis = 500)) {
//                IntOffset(it.width, 100)
//            }
        ) {

            Card(
                modifier = Modifier
                    .fillMaxHeight(1f)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(topEnd = 32.dp, topStart = 32.dp),
                backgroundColor = getCardColor(bmiResult)
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.SpaceEvenly,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(id = R.string.your_score),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = String.format("%.2f", bmiResult),
                        fontSize = 40.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = getBmiText(bmiResult),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )

                    Row() {

                        Button(
                            onClick = {
                                footerExpandState = false
                                weightState = ""
                                heightState = ""
                                weightFocusRequester.requestFocus()
                            },
                            colors = ButtonDefaults.buttonColors(Color(153, 111, 221, 255))
                        ) {
                            Text(
                                text = stringResource(id = R.string.reset_button),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.W600
                            )
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        Button(
                            onClick = { /*TODO*/ },
                            colors = ButtonDefaults.buttonColors(Color(153, 111, 221, 255))
                        ) {
                            Text(
                                text = stringResource(id = R.string.share_button),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.W600
                            )
                        }

                    }
                }
            }
        }
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun BMICalculatorPreview() {
    BMICalculator()
}

