package ru.hse.pe.presentation.test

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.test.viewModels.TestViewModel
import com.google.accompanist.systemuicontroller.SystemUiController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import ru.hse.pe.R
import ru.hse.pe.domain.model.Scale
import ru.hse.pe.domain.model.Test
import ru.hse.pe.presentation.test.utils.sealed.DataState
import ru.hse.pe.presentation.test.utils.sealed.Routes


class TestActivity : ComponentActivity() {
    private val questionsMap = hashMapOf<Int, String>()
    private val answers = hashMapOf<Int, String>()

    private val answersPoint = mutableListOf<Int>()
    private val answersBoolean = mutableListOf<Boolean>()
    private var scales = hashMapOf<String, Scale>()
    private var sortedScales = mapOf<String, Scale>()


    private val context = this@TestActivity
    private val viewModel: TestViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Navigation()
        }
    }

    @Composable
    fun Navigation() {
        val navController = rememberNavController()
        NavHost(navController = navController, startDestination = Routes.Test.route) {
            composable(Routes.Test.route) {
                Test(navController)
            }

            composable(Routes.Results.route) {
                Results(navController)
            }
        }
    }

    @Composable
    fun Results(navController: NavController) {

        Column(
            //   modifier = Modifier.verticalScroll(ScrollState(0))
        ) {
            SystemBarsNotVisible()
            MyTopAppBar(getString(R.string.results))

            Box(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxSize()
                    .background(Color.White),
            ) {
                LazyColumn {
                    val keys = sortedScales.keys.toList()
                    var resDiff: HashMap<String, HashMap<String, String>>
                    var resSize: Int

                    var text: String
                    var desc = ""

                    val reversePoints = hashMapOf<Int, Int>()
                    val answersKeyList = answers.keys.toList()

                    var from = 0
                    var to = answersKeyList.size - 1

                    while (from < answersKeyList.size) {
                        reversePoints[answersKeyList[to]] = answersKeyList[from]
                        from++
                        to--
                    }

                    items(sortedScales.size) { index ->
                        Column() {
                            val counting = sortedScales[keys[index]]?.counting
                                ?.split(", ")?.toList()
                            val countingR = sortedScales[keys[index]]?.countingR
                                ?.split(", ")?.toList()
                            var mark = 0

                            if (counting != null) {
                                for (i in counting.indices) {
                                    mark += answersPoint[counting[i].toInt()]
                                }
                            }

                            if (countingR != null) {
                                for (i in countingR.indices) {
                                    mark += reversePoints[answersPoint[countingR[i].toInt()]]!!
                                }
                            }



                            if (sortedScales[keys[index]]?.res != null) {
                                resDiff = sortedScales[keys[index]]?.res!!

                                val resDiffKeys = resDiff.keys.toList()
                                resSize = resDiffKeys.size

                                for (i in 0 until resSize) {
                                    val points = resDiff[resDiffKeys[i]]?.get("points")
                                    var leftNum: Int
                                    var rightNum: Int

                                    if (points != null) {
                                        if (points.contains("-")) {
                                            val pointArr = points.split("-")
                                            leftNum = pointArr[0].toInt()
                                            rightNum = pointArr[1].toInt()

                                            if (mark in leftNum..rightNum) {
                                                desc =
                                                    resDiff[resDiffKeys[i]]?.get("desc").toString()
                                                break
                                            }
                                        } else {
                                            leftNum = points.toInt()
                                            if (mark > leftNum) {
                                                desc =
                                                    resDiff[resDiffKeys[i]]?.get("desc").toString()
                                                break
                                            }
                                        }
                                    }
                                }
                            } else {
                                desc = sortedScales[keys[index]]?.desc.toString()
                            }

                            val textArray = sortedScales[keys[index]]?.text
                                .toString().split("mark")
                            text = textArray[0] + mark + textArray[1]

                            Text(
                                text = text,
                                fontSize = 18.sp,
                                modifier = Modifier.padding(top = 30.dp, bottom = 10.dp),
                                style = TextStyle(fontWeight = FontWeight.W600),
                            )

                            Text(
                                text = desc,
                                modifier = Modifier.padding(bottom = 20.dp),
                                fontSize = 14.sp
                            )
                        }
                    }
                }

            }
            Button(onClick = {
                navController.navigate(Routes.Test.route)
            }) {
                Text(text = getString(R.string.backToTests), color = Color.White)
            }

        }
    }

    @Composable
    fun Test(navController: NavController) {
        SystemBarsNotVisible()
        SetData(viewModel, navController)
    }

    @Composable
    fun SystemBarsNotVisible() {
        val systemUiController: SystemUiController = rememberSystemUiController()
        systemUiController.isSystemBarsVisible = false // Status & Navigation bars
    }


    @Composable
    fun ShowData(test: Test?, navController: NavController) {
        val name = test?.name.toString()
        val countQuestions = test?.countQuestions
        val listQuestions = test?.questions
        val listAnswers = test?.answers
        scales = test?.scales!!
        sortedScales = scales.toSortedMap()

        for (i in 0 until countQuestions!! + 1) {
            questionsMap[i] = listQuestions?.get(i) ?: ""
        }


        if (listAnswers != null) {
            val sortedListAnswers = listAnswers.toSortedMap()
            val listAnswersKeys = sortedListAnswers.keys.toList().sorted()

            var key: String
            for (i in 0 until listAnswers.size) {
                key = listAnswersKeys[i].drop(1).dropLast(1)

                answers[key.toInt()] = sortedListAnswers[listAnswersKeys[i]].toString()
            }
        }

        Box() {
            val counter = remember { mutableStateOf(1) }
            val maxCounter = remember { mutableStateOf(countQuestions) }
            val counterQ = remember { mutableStateOf(0) }

            for (i in 1 until maxCounter.value + 2) {
                answersPoint.add(-1)
                answersBoolean.add(false)
            }

            Column(
                modifier = Modifier.background(Color.White)
            ) {
                MyTopAppBar(name)
                CardItem(counter, maxCounter, counterQ, navController)
            }
        }
    }

    @Composable
    fun SetData(viewModel: TestViewModel, navController: NavController) {
        when (val result = viewModel.responce.value) {
            is DataState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is DataState.Success -> {
                ShowData(result.data, navController)
            }

            is DataState.Failure -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = result.message,
                        fontSize = MaterialTheme.typography.h5.fontSize
                    )
                }
            }

            else -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Error fetching data",
                        fontSize = MaterialTheme.typography.h5.fontSize
                    )
                }
            }
        }
    }


    @Composable
    fun MyTopAppBar(name: String) {
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
                Image(
                    painter = painterResource(id = R.drawable.ic_arrow_back),
                    contentDescription = "arrowLeft"
                )

                Text(
                    text = name,
                    modifier = Modifier
                        .padding(horizontal = 30.dp)
                        .width(250.dp),
                    textAlign = TextAlign.Center,
                    style = TextStyle(
                        fontSize = 18.sp, fontWeight = FontWeight(500)
                    )
                )

                Image(
                    painter = painterResource(id = R.drawable.ic_question),
                    contentDescription = "question"
                )
            }
        }
    }


    @Composable
    fun CardItem(
        counter: MutableState<Int>,
        maxCounter: MutableState<Int>,
        counterQ: MutableState<Int>,
        navController: NavController,
    ) {

        val progress = remember { mutableStateOf(0.0) }
        val toggleBtn = remember { mutableStateOf(false) }


        Card(
            elevation = 0.dp,
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 20.dp, start = 40.dp, end = 27.dp, bottom = 20.dp),
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                CreateTopPartCard(counter, counterQ, maxCounter, progress)
                CreateAnswersCard(counter, counterQ, maxCounter, progress, toggleBtn)
                Spacer(Modifier.weight(1f, true))
                CreateBtnCard(counter, maxCounter, toggleBtn, navController)
            }
        }
    }

    @Composable
    fun CreateTopPartCard(
        counter: MutableState<Int>,
        counterQ: MutableState<Int>,
        maxCounter: MutableState<Int>,
        progress: MutableState<Double>,

        ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 3.dp, bottom = 15.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {

            Text(
                text = "${counterQ.value}/",
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    //   fontFamily = FontFamily(typeface = Typeface())
                ),
                color = colorResource(id = R.color.black)
            )
            Text(
                text = "${maxCounter.value}",
                style = TextStyle(
                    fontSize = 18.sp,
                ),
            )
        }


        LinearProgressIndicator(
            progress = progress.value.toFloat(),
            modifier = Modifier
                .width(310.dp)
                .padding(bottom = 30.dp),
            color = colorResource(id = R.color.purple),
//            modifier = Modifier.background(brush = Brush.verticalGradient(
//                colors = listOf(
//                    colorResource(id = R.color.indicatorActFrom),
//                    colorResource(id = R.color.indicatorActTo)
//                )
//            ))
        )

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 0.dp), // 15 dp bottom
            style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold),
            text = counter.value.toString() + "."
        )

//        Box(modifier = Modifier
//            .width(310.dp)
//            .height(145.dp)
//            .background(Color.Gray)
//        ){}

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 15.dp, bottom = 10.dp),
            style = TextStyle(fontSize = 18.sp),
            text = questionsMap[counter.value].toString()
        )
    }

    @Composable
    fun CreateAnswersCard(
        counter: MutableState<Int>,
        counterQ: MutableState<Int>,
        maxCounter: MutableState<Int>,
        progress: MutableState<Double>,
        toggleBtn: MutableState<Boolean>,
    ) {
        Column(Modifier.selectableGroup(), horizontalAlignment = Alignment.Start) {
            val (selectedOption, onOptionSelected) = remember { mutableStateOf(0) }

            LazyColumn(
                modifier = Modifier
                    .selectableGroup()
                    .fillMaxWidth()
                    .padding(bottom = 10.dp),
                horizontalAlignment = Alignment.Start
            ) {
                val diff = 1.0 / maxCounter.value
                items(answers.size) { index ->
                    Row(
                        modifier = Modifier.height(30.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Box(modifier = Modifier
                            .width(18.dp)
                            .height(18.dp)) {
                            RadioButton(
                                selected = (answers.keys.toList()[index] == answersPoint[counter.value]),
                                onClick = {
                                    onOptionSelected(answers.keys.toList()[index])
                                    answersPoint[counter.value] = answers.keys.toList()[index]

                                    if (!answersBoolean[counter.value]) {
                                        counterQ.value++
                                        if (progress.value < 1.0f) progress.value += diff
                                        answersBoolean[counter.value] = true

                                        var c = 0
                                        for (i in 1 until answersBoolean.size) {
                                            if (answersBoolean[i]) {
                                                c++
                                            }
                                        }

                                        if (c == maxCounter.value) {
                                            toggleBtn.value = true
                                        }
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxSize(),
                                colors = RadioButtonDefaults
                                    .colors(selectedColor = Color(R.color.purple))
                            )
                        }

                        Text(
                            text = answers[answers.keys.toList()[index]].toString(),
                            fontSize = 13.sp,
                            modifier = Modifier.padding(start = 20.dp)
                        )
                    }
                }
            }
        }
    }


    @Composable
    fun CreateBtnCard(
        counter: MutableState<Int>,
        maxCounter: MutableState<Int>,
        toggleBtn: MutableState<Boolean>,
        navController: NavController,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 0.dp, bottom = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Button(
                onClick = {
                    if (answersPoint[counter.value] == -1) {
                        Toast.makeText(
                            context,
                            "Выберите вариант ответа или нажмите кнопку пропустить",
                            Toast.LENGTH_SHORT
                        ).show()
                        return@Button
                    } else {
                        counter.value--

                        if (counter.value < 1) {
                            counter.value = maxCounter.value
                        }
                    }
                },
                modifier = Modifier.width(160.dp),
                shape = RoundedCornerShape(15.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
                border = BorderStroke(2.dp, color = colorResource(id = R.color.purple))
            ) {
                Text(
                    text = stringResource(id = R.string.backBtn),
                    color = colorResource(id = R.color.purple)
                )
            }
            Button(
                onClick = {
                    if (answersPoint[counter.value] == -1) {
                        Toast.makeText(
                            context,
                            "Выберите вариант ответа или нажмите кнопку пропустить",
                            Toast.LENGTH_SHORT
                        ).show()
                        return@Button
                    } else {
                        counter.value++
                        if (counter.value > maxCounter.value) {
                            counter.value = 1
                        }
                    }
                },
                shape = RoundedCornerShape(15.dp),
                modifier = Modifier
                    .width(if (toggleBtn.value) 0.dp else 165.dp)
                    .height(if (toggleBtn.value) 0.dp else 37.dp),
                colors = ButtonDefaults
                    .buttonColors(
                        backgroundColor = colorResource(id = R.color.purple),
                        contentColor = Color.White
                    )
            ) {
                Text(text = stringResource(id = R.string.nextBtn))
            }

            Button(
                onClick = {
                    navController.navigate(Routes.Results.route)
                },

                shape = RoundedCornerShape(15.dp),
                modifier = Modifier
                    .width(if (toggleBtn.value) 165.dp else 0.dp)
                    .height(if (toggleBtn.value) 37.dp else 0.dp),
                colors = ButtonDefaults
                    .buttonColors(
                        backgroundColor = colorResource(id = R.color.purple),
                        contentColor = Color.White
                    )
            ) {
                Text(text = stringResource(id = R.string.finishBtn))
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            Button(
                onClick = {
                    counter.value++
                    if (counter.value > maxCounter.value) {
                        counter.value = 1
                    }
                },
                modifier = Modifier
                    .fillMaxWidth(if (toggleBtn.value) 0.0f else 0.7f),
                elevation = ButtonDefaults.elevation(0.dp, 0.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
            ) {
                Text(
                    text = stringResource(id = R.string.skipBtn),
                    color = colorResource(id = R.color.purple)
                )
            }
        }
    }
}






