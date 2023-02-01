import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import ru.hse.pe.R
import ru.hse.pe.SharedViewModel
import ru.hse.pe.domain.model.TestItem
import ru.hse.pe.domain.model.TestItem.answersPoint
import ru.hse.pe.presentation.test.utils.sealed.Routes
import ru.hse.pe.utils.Utils

@Composable
fun Test(navController: NavController, sharedViewModel: SharedViewModel) {
    Utils.SystemBarsNotVisible()
    ShowData(navController, sharedViewModel)
}

@Composable
fun ShowData(navController: NavController, sharedViewModel: SharedViewModel) {
    val name = sharedViewModel.quiz.value?.name.toString()
    val listQuestions = sharedViewModel.quiz.value?.questions?.toList()
    val listAnswers = sharedViewModel.quiz.value?.answers?.get(0)?.toList()
    val userAnswers = mutableListOf<String>()
    val countQuestions = listQuestions!!.size
    val answers = hashMapOf<Int, String>()
    val answersPoint = mutableListOf<Int>()
    val answersBoolean = mutableListOf<Boolean>()
    val progress = remember { mutableStateOf(0.0) }
    val toggleBtn = remember { mutableStateOf(false) }
    val counter = remember { mutableStateOf(1) }
    val maxCounter = remember { mutableStateOf(countQuestions) }
    val counterQ = remember { mutableStateOf(0) }

    Box {
        for (i in 1 until maxCounter.value + 2) {
            answersPoint.add(-1)
            answersBoolean.add(false)
            userAnswers.add("")
        }

        for (i in listAnswers!!.indices){
            answers[i] = listAnswers[i]
        }

        TestItem.counter = counter
        TestItem.maxCounter = maxCounter
        TestItem.counterQ = counterQ
        TestItem.progress = progress
        TestItem.toggleBtn = toggleBtn
        TestItem.questions = listQuestions
        TestItem.answers = answers
        TestItem.userAnswers = userAnswers
        TestItem.answersPoint = answersPoint
        TestItem.answersBoolean = answersBoolean

        Column(
            modifier = Modifier.background(Color.White)
        ) {
            Utils.MyTopAppBar(name, true)
            CardItem(navController)
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
            text = if (TestItem.counterQ.value in 1..9) {
                "0${TestItem.counterQ.value}/"
            } else {
                "${TestItem.counterQ.value}/"
            },
            style = TextStyle(
                fontWeight = FontWeight.SemiBold,
                fontSize = MaterialTheme.typography.subtitle1.fontSize,
                fontFamily = MaterialTheme.typography.subtitle1.fontFamily
            ),
            color = colorResource(id = R.color.black)
        )
        Text(
            text = "${TestItem.maxCounter.value}",
            style = MaterialTheme.typography.subtitle1
        )
    }
    LinearProgressIndicator(
        progress = TestItem.progress.value.toFloat(),
        modifier = Modifier
            .width(310.dp)
            .padding(bottom = 32.dp, start = 40.dp),
        color = colorResource(id = R.color.purple),
        backgroundColor = colorResource(id = R.color.grayLight),
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
        text = TestItem.counter.value.toString() + "."
    )

    Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, bottom = 35.dp, start = 40.dp),
        text = TestItem.questions[TestItem.counter.value - 1],
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
            val diff = 1.0 / TestItem.maxCounter.value
            items(TestItem.answers.size) { index ->
                Row(
                    modifier = Modifier.height(30.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                     Box(modifier = Modifier
                        .width(20.dp)
                        .height(20.dp)) {
                        RadioButton(
                            selected = (TestItem.answers.keys.toList()[index] == TestItem.answersPoint[TestItem.counter.value]),
                            onClick = {
                                onOptionSelected(TestItem.answers.keys.toList()[index])
                                TestItem.answersPoint[TestItem.counter.value] = TestItem.answers.keys.toList()[index]
                                TestItem.userAnswers[TestItem.counter.value] =
                                    TestItem.answers[TestItem.answers.keys.toList()[index]].toString()

                                if (!TestItem.answersBoolean[TestItem.counter.value]) {
                                    TestItem.counterQ.value++
                                    if (TestItem.progress.value < 1.0f) TestItem.progress.value += diff
                                    TestItem.answersBoolean[TestItem.counter.value] = true

                                    var c = 0
                                    for (i in 1 until TestItem.answersBoolean.size) {
                                        if (TestItem.answersBoolean[i]) {
                                            c++
                                        }
                                    }

                                    if (c == TestItem.maxCounter.value) {
                                        TestItem.toggleBtn.value = true
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
                        text = TestItem.answers[TestItem.answers.keys.toList()[index]].toString(),
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
                if (!TestItem.answersBoolean[TestItem.counter.value]) {
                    Toast.makeText(
                        TestItem.context,
                        "Выберите вариант ответа или нажмите кнопку пропустить",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@Button
                } else {
                    TestItem.counter.value--

                    if (TestItem.counter.value < 1) {
                        TestItem.counter.value = TestItem.maxCounter.value
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
                if (!TestItem.answersBoolean[TestItem.counter.value]) {
                    Toast.makeText(
                        TestItem.context,
                        "Выберите вариант ответа или нажмите кнопку пропустить",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@Button
                } else {
                    TestItem.counter.value++
                    if (TestItem.counter.value > TestItem.maxCounter.value) {
                        TestItem.counter.value = 1
                    }
                }
            },
            shape = RoundedCornerShape(15.dp),
            modifier = Modifier
                .width(if (TestItem.toggleBtn.value) 0.dp else 165.dp)
                .height(if (TestItem.toggleBtn.value) 0.dp else 45.dp),
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
                .width(if (TestItem.toggleBtn.value) 165.dp else 0.dp)
                .height(if (TestItem.toggleBtn.value) 45.dp else 0.dp),
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
                TestItem.counter.value++
                if (TestItem.counter.value > TestItem.maxCounter.value) {
                    TestItem.counter.value = 1
                }
            },
            modifier = Modifier
                .fillMaxWidth(if (TestItem.toggleBtn.value) 0.0f else 0.7f),
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
