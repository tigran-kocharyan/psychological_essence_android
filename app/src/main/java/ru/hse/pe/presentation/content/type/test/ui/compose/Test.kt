import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.commit
import ru.hse.pe.R
import ru.hse.pe.SharedViewModel
import ru.hse.pe.domain.model.QuizAnswerEntity
import ru.hse.pe.presentation.MainActivity
import ru.hse.pe.presentation.content.type.test.ui.TestResultFragment
import ru.hse.pe.presentation.content.type.test.ui.TestsFragment
import ru.hse.pe.presentation.content.viewmodel.ContentViewModel


@Composable
fun Test(
    sharedViewModel: SharedViewModel,
    viewModel: ContentViewModel,
) {
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

        for (i in listAnswers!!.indices) {
            answers[i] = listAnswers[i]
        }

        Test.counter = counter
        Test.maxCounter = maxCounter
        Test.counterQ = counterQ
        Test.progress = progress
        Test.toggleBtn = toggleBtn
        Test.questions = listQuestions
        Test.answers = answers
        Test.userAnswers = userAnswers
        Test.answersPoint = answersPoint
        Test.answersBoolean = answersBoolean

        Column(
            modifier = Modifier.background(Color.White)
        ) {
            CardItem(sharedViewModel, viewModel)
        }
    }
}


@Composable
fun CardItem(
    sharedViewModel: SharedViewModel,
    viewModel: ContentViewModel,
) {
    Card(
        elevation = 0.dp,
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 32.dp, end = 27.dp, bottom = 20.dp),
        ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CreateTopPartCard()
            CreateAnswersCard(sharedViewModel, viewModel)
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
            text = if (Test.counterQ.value in 1..9) {
                "0${Test.counterQ.value}/"
            } else {
                "${Test.counterQ.value}/"
            },
            style = TextStyle(
                fontWeight = FontWeight.SemiBold,
                fontSize = MaterialTheme.typography.subtitle1.fontSize,
                fontFamily = MaterialTheme.typography.subtitle1.fontFamily
            ),
            color = colorResource(id = R.color.black)
        )
        Text(
            text = "${Test.maxCounter.value}",
            style = MaterialTheme.typography.subtitle1
        )
    }
    LinearProgressIndicator(
        progress = Test.progress.value.toFloat(),
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
        text = Test.counter.value.toString() + "."
    )

    Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, bottom = 35.dp, start = 40.dp),
        text = Test.questions[Test.counter.value - 1],
        style = MaterialTheme.typography.subtitle1
    )
}

@Composable
fun CreateAnswersCard(
    sharedViewModel: SharedViewModel,
    viewModel: ContentViewModel,
) {
    Column(
        Modifier
            .selectableGroup(), horizontalAlignment = Alignment.Start) {
        val (selectedOption, onOptionSelected) = remember { mutableStateOf(0) }
        LazyColumn(
            modifier = Modifier
                .selectableGroup()
                .fillMaxWidth()
                .padding(bottom = 40.dp, start = 40.dp)
                .weight(1f),

            horizontalAlignment = Alignment.Start
        ) {
            val diff = 1.0 / Test.maxCounter.value
            items(Test.answers.size) { index ->
                Row(
                    modifier = Modifier.padding(bottom = 30.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .width(20.dp)
                            .height(20.dp)
                    ) {
                        RadioButton(
                            selected = (Test.answers.keys.toList()[index] == Test.answersPoint[Test.counter.value]),
                            onClick = {
                                onOptionSelected(Test.answers.keys.toList()[index])
                                Test.answersPoint[Test.counter.value] =
                                    Test.answers.keys.toList()[index]
                                Test.userAnswers[Test.counter.value] =
                                    Test.answers[Test.answers.keys.toList()[index]].toString()

                                if (!Test.answersBoolean[Test.counter.value]) {
                                    Test.counterQ.value++
                                    if (Test.progress.value < 1.0f) Test.progress.value += diff
                                    Test.answersBoolean[Test.counter.value] = true

                                    var c = 0
                                    for (i in 1 until Test.answersBoolean.size) {
                                        if (Test.answersBoolean[i]) {
                                            c++
                                        }
                                    }

                                    if (c == Test.maxCounter.value) {
                                        Test.toggleBtn.value = true
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
                        text = Test.answers[Test.answers.keys.toList()[index]].toString(),
                        fontSize = 13.sp,
                        modifier = Modifier.padding(start = 22.dp),
                        style = MaterialTheme.typography.subtitle2
                    )
                }

            }
        }
        CreateBtnCard(sharedViewModel, viewModel)
    }
}

@Composable
fun CreateBtnCard(
    sharedViewModel: SharedViewModel,
    viewModel: ContentViewModel,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 0.dp, bottom = 0.dp, start = 40.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        val context = LocalContext.current
        Button(
            onClick = {
                if (!Test.answersBoolean[Test.counter.value]) {
                    Toast.makeText(
                        context,
                        "Выберите вариант ответа или нажмите кнопку пропустить",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@Button
                } else {
                    Test.counter.value--

                    if (Test.counter.value < 1) {
                        Test.counter.value = Test.maxCounter.value
                    }
                }
            },
            modifier = Modifier
                .width(163.dp)
                .height(45.dp)
                .padding(end = 16.dp),
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
                if (!Test.answersBoolean[Test.counter.value]) {
                    Toast.makeText(
                        context,
                        "Выберите вариант ответа или нажмите кнопку пропустить",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@Button
                } else {
                    Test.counter.value++
                    if (Test.counter.value > Test.maxCounter.value) {
                        Test.counter.value = 1
                    }
                }
            },
            shape = RoundedCornerShape(15.dp),
            modifier = Modifier
                .width(if (Test.toggleBtn.value) 0.dp else 165.dp)
                .height(if (Test.toggleBtn.value) 0.dp else 45.dp),
            colors = ButtonDefaults
                .buttonColors(
                    backgroundColor = colorResource(id = R.color.purple),
                    contentColor = Color.White
                )
        ) {
            Text(text = stringResource(id = R.string.nextBtn))
        }
        val activity = LocalContext.current as? MainActivity
        Button(
            onClick = {
                activity?.supportFragmentManager
                    ?.beginTransaction()
                    ?.setCustomAnimations(
                        R.anim.slide_in,
                        R.anim.fade_out,
                        R.anim.pop_enter,
                        R.anim.pop_exit
                    )?.add(R.id.fragment_container, TestResultFragment.newInstance(),
                        "TestResultFragment")
                    ?.addToBackStack(null)
                    ?.commit()

                Test.userAnswers.removeAt(0)
            },
            shape = RoundedCornerShape(15.dp),
            modifier = Modifier
                .width(if (Test.toggleBtn.value) 165.dp else 0.dp)
                .height(if (Test.toggleBtn.value) 45.dp else 0.dp),
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
                Test.counter.value++
                if (Test.counter.value > Test.maxCounter.value) {
                    Test.counter.value = 1
                }
            },
            modifier = Modifier
                .width(if (Test.toggleBtn.value) 0.dp else 200.dp)
                .height(if (Test.toggleBtn.value) 0.dp else 45.dp),
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

object Test {
    lateinit var counter: MutableState<Int>
    lateinit var maxCounter: MutableState<Int>
    lateinit var counterQ: MutableState<Int>
    lateinit var progress: MutableState<Double>
    lateinit var toggleBtn: MutableState<Boolean>
    lateinit var questions: List<String>
    lateinit var answers: HashMap<Int, String>
    lateinit var userAnswers: MutableList<String>
    lateinit var answersPoint: MutableList<Int>
    lateinit var answersBoolean: MutableList<Boolean>
}



