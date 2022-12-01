package ru.hse.pe.presentation.test

import android.content.Context
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
import androidx.compose.ui.unit.TextUnit
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
import ru.hse.pe.domain.model.TestItem
import ru.hse.pe.presentation.test.utils.sealed.DataState
import ru.hse.pe.presentation.test.utils.sealed.Routes
import ru.hse.pe.presentation.test.utils.theme.TestTheme
import ru.hse.pe.utils.Utils.MyTopAppBar
import ru.hse.pe.utils.Utils.SystemBarsNotVisible


class TestActivity : ComponentActivity() {
    private lateinit var testItem: TestItem


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TestTheme {
                Navigation()
            }
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
                Results()
            }
        }
    }



    @Composable
    fun Results() {
        Column {
            SystemBarsNotVisible()
            MyTopAppBar(getString(R.string.results), false)

            Box(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxSize(0.87f)
                    .background(Color.White),
            ) {
                LazyColumn {
                    val keys = testItem.sortedScales.keys.toList()
                    var resDiff: HashMap<String, HashMap<String, String>>
                    var resSize: Int

                    var text: String
                    var desc = ""

                    val reversePoints = hashMapOf<Int, Int>()
                    val answersKeyList = testItem.answers.keys.toList()

                    var from = 0
                    var to = answersKeyList.size - 1

                    while (from < answersKeyList.size) {
                        reversePoints[answersKeyList[to]] = answersKeyList[from]
                        from++
                        to--
                    }

                    items(testItem.sortedScales.size) { index ->
                        Column() {
                            val counting = testItem.sortedScales[keys[index]]?.counting
                                ?.split(", ")?.toList()
                            val countingR = testItem.sortedScales[keys[index]]?.countingR
                                ?.split(", ")?.toList()
                            var mark = 0

                            if (counting != null) {
                                for (i in counting.indices) {
                                    mark += testItem.answersPoint[counting[i].toInt()]
                                }
                            }

                            if (countingR != null) {
                                for (i in countingR.indices) {
                                    mark += reversePoints[testItem.answersPoint[countingR[i].toInt()]]!!
                                }
                            }

                            if (testItem.sortedScales[keys[index]]?.res != null) {
                                resDiff = testItem.sortedScales[keys[index]]?.res!!

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
                                desc = testItem.sortedScales[keys[index]]?.desc.toString()
                            }

                            val textArray = testItem.sortedScales[keys[index]]?.text
                                .toString().split("mark")
                            text = textArray[0] + mark + textArray[1]

                            Text(
                                text = text,
                                fontSize = 18.sp,
                                modifier = Modifier.padding(top = 32.dp, bottom = 13.dp),
                                style = MaterialTheme.typography.h3,
                            )

                            Text(
                                text = desc,
                                modifier = Modifier.padding(bottom = 24.dp),
                                style = MaterialTheme.typography.h4,
                            )
                        }
                    }
                }
            }

            Button(
                onClick = {
                    TODO()
                },
                shape = RoundedCornerShape(15.dp),
                modifier = Modifier.padding(start = 25.dp, end = 25.dp).fillMaxWidth().height(45.dp),
                colors = ButtonDefaults
                    .buttonColors(
                        backgroundColor = colorResource(id = R.color.purple),
                        contentColor = Color.White
                    )
            ) {
                Text(text = stringResource(id = R.string.backToTests))
            }
        }
    }

    @Composable
    fun Test(navController: NavController) {
        val viewModel: TestViewModel by viewModels()
        SystemBarsNotVisible()
        SetData(viewModel, navController)
    }


    @Composable
    fun ShowData(test: Test?, navController: NavController) {
        val name = test?.name.toString()
        val countQuestions = test?.countQuestions
        val listQuestions = test?.questions
        val listAnswers = test?.answers

        val questionsMap = hashMapOf<Int, String>()
        val answers = hashMapOf<Int, String>()

        val answersPoint = mutableListOf<Int>()
        val answersBoolean = mutableListOf<Boolean>()
        var sortedScales: Map<String, Scale>

        var scales: HashMap<String, Scale> = test?.scales!!
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


        val progress = remember { mutableStateOf(0.0) }
        val toggleBtn = remember { mutableStateOf(false) }
        val counter = remember { mutableStateOf(1) }
        val maxCounter = remember { mutableStateOf(countQuestions) }
        val counterQ = remember { mutableStateOf(0) }
        val context = this@TestActivity

        Box() {
            for (i in 1 until maxCounter.value + 2) {
                answersPoint.add(-1)
                answersBoolean.add(false)
            }

            testItem = TestItem(counter, maxCounter, counterQ, progress, toggleBtn, questionsMap, answers,
                answersPoint, answersBoolean, scales, sortedScales, context)

            Column(
                modifier = Modifier.background(Color.White)
            ) {
                MyTopAppBar(name, true)
                CardItem(navController)
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
    fun CardItem(navController: NavController) {
        Card(
            elevation = 0.dp,
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 32.dp, end = 27.dp, bottom = 20.dp),

            ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                CreateTopPartCard()
                CreateAnswersCard()
                Spacer(Modifier.weight(1f, true))
                CreateBtnCard(navController)
            }
        }
    }

    @Composable
    fun CreateTopPartCard() {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 3.dp, bottom = 8.dp, start = 40.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = if (testItem.counterQ.value in 1..9) {
                    "0${testItem.counterQ.value}/"
                } else {
                    "${testItem.counterQ.value}/"
                },
                style = TextStyle(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = MaterialTheme.typography.subtitle1.fontSize,
                    fontFamily = MaterialTheme.typography.subtitle1.fontFamily
                ),
                color = colorResource(id = R.color.black)
            )
            Text(
                text = "${testItem.maxCounter.value}",
                style = MaterialTheme.typography.subtitle1
            )
        }
        LinearProgressIndicator(
            progress = testItem.progress.value.toFloat(),
            modifier = Modifier
                .width(310.dp)
                .padding(bottom = 32.dp, start = 40.dp),
            color = colorResource(id = R.color.purple),
            backgroundColor = colorResource(id = R.color.grayLight),
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
                .padding(bottom = 0.dp, start = 40.dp),
            style = TextStyle(
                fontFamily = MaterialTheme.typography.subtitle1.fontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            ),

            text = testItem.counter.value.toString() + "."
        )

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, bottom = 35.dp, start = 40.dp),

            text = testItem.questionsMap[testItem.counter.value].toString(),
            style = MaterialTheme.typography.subtitle1
        )
    }

    @Composable
    fun CreateAnswersCard() {
        Column(Modifier.selectableGroup(), horizontalAlignment = Alignment.Start) {
            val (selectedOption, onOptionSelected) = remember { mutableStateOf(0) }
            LazyColumn(
                modifier = Modifier
                    .selectableGroup()
                    .fillMaxWidth()
                    .padding(bottom = 57.dp, start = 40.dp),
                horizontalAlignment = Alignment.Start
            ) {
                val diff = 1.0 / testItem.maxCounter.value
                items(testItem.answers.size) { index ->
                    Row(
                        modifier = Modifier.height(30.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Box(modifier = Modifier
                            .width(20.dp)
                            .height(20.dp)) {
                            RadioButton(
                                selected = (testItem.answers.keys.toList()[index] == testItem.answersPoint[testItem.counter.value]),
                                onClick = {
                                    onOptionSelected(testItem.answers.keys.toList()[index])
                                    testItem.answersPoint[testItem.counter.value] = testItem.answers.keys.toList()[index]

                                    if (!testItem.answersBoolean[testItem.counter.value]) {
                                        testItem.counterQ.value++
                                        if (testItem.progress.value < 1.0f) testItem.progress.value += diff
                                        testItem.answersBoolean[testItem.counter.value] = true

                                        var c = 0
                                        for (i in 1 until testItem.answersBoolean.size) {
                                            if (testItem.answersBoolean[i]) {
                                                c++
                                            }
                                        }

                                        if (c == testItem.maxCounter.value) {
                                            testItem.toggleBtn.value = true
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
                            text = testItem.answers[testItem.answers.keys.toList()[index]].toString(),
                            fontSize = 13.sp,
                            modifier = Modifier.padding(start = 22.dp),
                            style = MaterialTheme.typography.subtitle2
                        )
                    }
                }
            }
        }
    }


    @Composable
    fun CreateBtnCard(navController: NavController) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 0.dp, bottom = 0.dp, start = 40.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Button(
                onClick = {
                    if (testItem.answersPoint[testItem.counter.value] == -1) {
                        Toast.makeText(
                            testItem.context,
                            "Выберите вариант ответа или нажмите кнопку пропустить",
                            Toast.LENGTH_SHORT
                        ).show()
                        return@Button
                    } else {
                        testItem.counter.value--

                        if (testItem.counter.value < 1) {
                            testItem.counter.value = testItem.maxCounter.value
                        }
                    }
                },
                modifier = Modifier.width(163.dp).height(45.dp).padding(end = 16.dp),
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
                    if (testItem.answersPoint[testItem.counter.value] == -1) {
                        Toast.makeText(
                            testItem.context,
                            "Выберите вариант ответа или нажмите кнопку пропустить",
                            Toast.LENGTH_SHORT
                        ).show()
                        return@Button
                    } else {
                        testItem.counter.value++
                        if (testItem.counter.value > testItem.maxCounter.value) {
                            testItem.counter.value = 1
                        }
                    }
                },
                shape = RoundedCornerShape(15.dp),
                modifier = Modifier
                    .width(if (testItem.toggleBtn.value) 0.dp else 165.dp)
                    .height(if (testItem.toggleBtn.value) 0.dp else 45.dp),
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
                    .width(if (testItem.toggleBtn.value) 165.dp else 0.dp)
                    .height(if (testItem.toggleBtn.value) 45.dp else 0.dp),
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
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 40.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            Button(
                onClick = {
                    testItem.counter.value++
                    if (testItem.counter.value > testItem.maxCounter.value) {
                        testItem.counter.value = 1
                    }
                },
                modifier = Modifier
                    .fillMaxWidth(if (testItem.toggleBtn.value) 0.0f else 0.7f),
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






