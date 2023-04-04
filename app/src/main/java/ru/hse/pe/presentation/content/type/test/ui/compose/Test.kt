package ru.hse.pe.presentation.content.type.test.ui.compose

import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.burnoutcrew.reorderable.*
import ru.hse.pe.R
import ru.hse.pe.SharedViewModel
import ru.hse.pe.domain.model.QuizMetaDataEntity
import ru.hse.pe.presentation.content.type.test.ui.TestResultFragment
import ru.hse.pe.utils.Utils

@Composable
fun Test(
    sharedViewModel: SharedViewModel,
) {
    val name = sharedViewModel.quiz.value?.name.toString()
    val listQuestions = sharedViewModel.quiz.value?.questions?.toList()
    val listAnswers = sharedViewModel.quiz.value?.answers?.toList()
    val userAnswers = mutableListOf<Any>()
    val countQuestions = listQuestions!!.size
    val quizMetaData = sharedViewModel.quiz.value!!.quizMetaData

    val answers = hashMapOf<Int, List<String>>() // вопросы
    val answersPoint = mutableListOf<Int>()
    val answersBoolean = mutableListOf<Boolean>() // ответили ли на вопрос
    val progress = remember { mutableStateOf(0.0) }
    val toggleBtn = remember { mutableStateOf(false) }
    val counter = remember { mutableStateOf(1) } // общий счетчик
    val counterAnswers = remember { mutableStateOf(0) } // кол-во вариантов ответа в вопросе
    val maxCounter = remember { mutableStateOf(countQuestions) } // макс кол-во отвеченных вопросов
    val counterQ = remember { mutableStateOf(0) } // подсчет отвеченных вопросов


    Box {
        for (i in 1 until maxCounter.value + 2) {
            answersPoint.add(-1)
            answersBoolean.add(false)
        }

        for (i in listAnswers!!.indices) {
            val list = mutableListOf<String>()
            for (j in 0 until listAnswers[i].size) {
                list.add(listAnswers[i][j])
            }
            answers[i] = list
        }

        Test.counter = counter
        Test.maxCounter = maxCounter
        Test.counterQ = counterQ
        Test.counterAnswers = counterAnswers
        Test.progress = progress
        Test.toggleBtn = toggleBtn
        Test.questions = listQuestions
        Test.answers = answers
        Test.userAnswers = userAnswers
        Test.answersPoint = answersPoint
        Test.answersBoolean = answersBoolean

        Log.d("answersEnt", quizMetaData.toString())


        // Подготавливаем шаблон с ответами
        if (quizMetaData != null) {
            for (i in 1 until maxCounter.value) {
                if (quizMetaData[i]?.multiple_answers == true) {
                    Test.userAnswers.add(i, mutableListOf<String>())
                } else if (quizMetaData[i]?.categories?.isNotEmpty() == true) {
                    Test.userAnswers.add(i, mutableMapOf<String, String>())
                } else {
                    Test.userAnswers.add(i, "")
                }

            }
            Test.quizMetaData = quizMetaData
        } else {
            Test.quizMetaData = mapOf()
        }
        Log.d("Test.userAns", Test.userAnswers.toString())

        // Это список для сохранения несколько вариантов ответа
        val listOfMultipleAnswers = mutableListOf<MutableList<String>>()
        for (i in 0 until Test.answers.size) {
            val list = mutableListOf<String>()
            for (j in 0 until Test.answers.size) {
                list.add("")
            }
            listOfMultipleAnswers.add(list)
        }
        Test.multipleAnswers = listOfMultipleAnswers

        Column(
            modifier = Modifier.background(Color.White)
        ) {
            val title: String = if (sharedViewModel.quiz.value?.name?.length?.compareTo(30)!! > 0) {
                sharedViewModel.quiz.value?.name?.substring(0, 30) + "..."
            } else {
                sharedViewModel.quiz.value?.name.toString()
            }
            Utils.MyTopAppBar(name = title, arrow = false)
            CardItem()
        }
    }
}


@Composable
fun CardItem() {
    Card(
        elevation = 0.dp,
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 32.dp, end = 27.dp, bottom = 20.dp),
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CreateTopPartCard()
            CreateAnswersCard()
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
fun CreateAnswersCard() {
    val counter = Test.counter.value - 1
    if (Test.quizMetaData[counter]?.multiple_answers == true) {
        MultipleAnswers()
    } else if (Test.quizMetaData[Test.counter.value]?.categories?.isEmpty() == false) {
        val categories = Test.quizMetaData[Test.counter.value]?.categories
        if (categories != null) {
            DragAndDrop(categories)
        }
    } else {
        OrdinaryAnswers()
    }
}

@Composable
fun MultipleAnswers() {
    Column(
        Modifier
            .selectableGroup(), horizontalAlignment = Alignment.Start
    ) {
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
            items(Test.answers[Test.counterAnswers.value]!!.size) { index ->
                val checkedState = remember { mutableStateOf(false) }
                onOptionSelected(Test.answers.keys.toList()[index])
                Row(
                    modifier = Modifier
                        .padding(bottom = 30.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .width(20.dp)
                            .height(20.dp)
                    ) {
                        Checkbox(
                            checked = Test.multipleAnswers[Test.counterAnswers.value][index] != "",
                            onCheckedChange = {
                                checkedState.value = it

                                if (!checkedState.value) {
                                    Test.multipleAnswers[Test.counterAnswers.value][index] = ""
                                } else {
                                    Test.multipleAnswers[Test.counterAnswers.value][index] =
                                        Test.answers[index]?.get(0).toString()
                                }

                                Test.userAnswers[Test.counter.value] = Test.multipleAnswers
                                Test.answersBoolean[Test.counter.value] = true
                            },
                            modifier = Modifier.padding(5.dp),
                            colors = CheckboxDefaults.colors(
                                checkedColor = Color(0xff, 0xb6, 0xc1),
                                checkmarkColor = Color.Red
                            )
                        )
                    }

                    Text(
                        text = Test.answers[Test.counterAnswers.value]?.get(index).toString(),
                        fontSize = 13.sp,
                        modifier = Modifier.padding(start = 22.dp),
                        style = MaterialTheme.typography.subtitle2
                    )
                }
            }
        }
        CreateBtnCard()
    }
}

data class ItemData(val title: String, val key: String, val isLocked: Boolean = false)

@Composable
fun DragAndDrop(categories: List<String>) {
    val values = mutableListOf<ItemData>()
    var id = 0
    for ((i, category) in categories.withIndex()) {
        values.add(ItemData(category, "id$id", true))
        id++

        // Добавляю скрытый элемент, чтобы можно было вставить ответ между категориями
        if (i < categories.size - 1) {
            values.add(ItemData("", "id${id}invisible", false))
            id++
        }
    }


//    Log.d("Test.answers", Test.answers[0].toString())
//    for (i in 0..Test.answers[0]!!.length){
//
//    }

    val answers = listOf("dsadas", "dsadasd")
    for (answer in answers) {
        values.add(ItemData(answer, "id$id", false))
        id++
    }

    var data by remember {
        mutableStateOf(
            values
        )
    }

    val state = rememberReorderableLazyListState(
        onMove = { from, to ->
            data = data.toMutableList().apply {
                add(to.index, removeAt(from.index))
            }
        },
        canDragOver = { draggedOver, dragging ->
            isDragEnabled(draggedOver, dragging, data)
        }
    )

    LazyColumn(
        state = state.listState,
        modifier = Modifier
            .padding(start = 40.dp)
            .reorderable(state)
            .detectReorderAfterLongPress(state),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(data, { item -> item.key }) { item ->
            ReorderableItem(state, item.key) { dragging ->
                val elevation = animateDpAsState(if (dragging) 8.dp else 0.dp)
                if (item.isLocked) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .pointerInput(Unit) {
                                detectDragGesturesAfterLongPress(onDragStart = {
                                    // Специально ничего не писал, чтобы данный элемент нельзя было перетащить
                                }, onDrag = { change, dragAmount ->
                                    // Специально ничего не писал, чтобы данный элемент нельзя было перетащить
                                })
                            }
                    ) {
                        Text(
                            text = item.title,
                            fontSize = 20.sp,
                            modifier = Modifier.padding(bottom = 24.dp),
                            style = MaterialTheme.typography.subtitle2
                        )
                    }
                } else if (item.key.contains("invisible")) {
                    Card(
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .alpha(0f)
                            .height(0.dp),
                    ) {
                        Text(
                            text = item.title,
                            fontSize = 12.sp,
                            style = MaterialTheme.typography.subtitle2,
                            modifier = Modifier
                                .background(colorResource(id = R.color.purpleTransparent))
                        )
                    }
                } else {
                    Card(
                        shape = RoundedCornerShape(10.dp),
                    ) {
                        Text(
                            text = item.title,
                            fontSize = 12.sp,
                            style = MaterialTheme.typography.subtitle2,
                            modifier = Modifier
                                .background(colorResource(id = R.color.purpleTransparent))
                                .padding(start = 16.dp, end = 16.dp, top = 7.dp, bottom = 7.dp),
                        )
                    }
                }
            }
        }

    }
}

fun isDragEnabled(draggedOver: ItemPosition, dragging: ItemPosition, data: List<ItemData>) =
    data.getOrNull(draggedOver.index)?.isLocked != true

@Composable
fun OrdinaryAnswers() {
    Column(
        Modifier
            .selectableGroup(), horizontalAlignment = Alignment.Start
    ) {
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
            //    Log.d("Test.anss", Test.answers[Test.counter.value]!!.size.toString())
            items(Test.answers[Test.counterAnswers.value]!!.size) { index ->
                onOptionSelected(Test.answers.keys.toList()[index])
                Row(
                    modifier = Modifier
                        .padding(bottom = 30.dp)
                        .selectable(
                            selected = (Test.answers.keys.toList()[index] == Test.answersPoint[Test.counter.value]),
                            onClick = {
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
                            }
                        ),
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
                            onClick = null,
                            modifier = Modifier
                                .fillMaxSize(),
                            colors = RadioButtonDefaults
                                .colors(selectedColor = Color(R.color.purple))
                        )
                    }

                    Text(
                        text = Test.answers[Test.counterAnswers.value]?.get(index).toString(),
                        fontSize = 13.sp,
                        modifier = Modifier.padding(start = 22.dp),
                        style = MaterialTheme.typography.subtitle2
                    )
                }
            }
        }

        CreateBtnCard()
    }
}


@Composable
fun CreateBtnCard() {
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
                val counterMultipleAnswer = checkMultipleAnswers()
                Log.d("Test.userAns", Test.userAnswers.toString())
                if (!Test.answersBoolean[Test.counter.value] || counterMultipleAnswer == 0) {
                    Toast.makeText(
                        context,
                        "Выберите вариант ответа или нажмите кнопку пропустить",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@Button
                } else {
                    Test.counter.value++
                    Test.counterAnswers.value++
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
        val activity = LocalContext.current as AppCompatActivity
        Button(
            onClick = {
                activity.supportFragmentManager
                    .beginTransaction()
                    .setCustomAnimations(
                        R.anim.slide_in,
                        R.anim.fade_out,
                        R.anim.pop_enter,
                        R.anim.pop_exit
                    )
                    .addToBackStack(null)
                    .add(
                        R.id.fragment_container, TestResultFragment.newInstance(),
                        TestResultFragment.TAG
                    )
                    .commit()
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
                Test.counterAnswers.value++
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

fun checkMultipleAnswers(): Int {
    // Проверяем заполнен ли хотя бы один ответ из множественного выбора
    Log.d("Test.userAns", Test.userAnswers.toString())
    Log.d("Test.userAns", "with counter: " + Test.userAnswers[Test.counter.value].toString())
    var counterMultipleAnswer = -1
    if (Test.userAnswers[Test.counter.value] is MutableList<*>) {
        counterMultipleAnswer = 0
        for (multipleAnswer in Test.userAnswers[Test.counter.value] as MutableList<*>) {
            if (multipleAnswer != null) {
                if (multipleAnswer == "") {
                    counterMultipleAnswer++
                }
            }
        }
    }
    return counterMultipleAnswer
}

object Test {
    lateinit var counter: MutableState<Int>
    lateinit var maxCounter: MutableState<Int>
    lateinit var counterQ: MutableState<Int>
    lateinit var counterAnswers: MutableState<Int>
    lateinit var progress: MutableState<Double>
    lateinit var toggleBtn: MutableState<Boolean>
    lateinit var questions: List<String>
    lateinit var quizMetaData: Map<Int, QuizMetaDataEntity>
    lateinit var answers: HashMap<Int, List<String>>
    lateinit var userAnswers: MutableList<Any>
    lateinit var answersPoint: MutableList<Int>
    lateinit var answersBoolean: MutableList<Boolean>


    lateinit var multipleAnswers: MutableList<MutableList<String>>
}

//object UserAnswers{
//    lateinit var multipleAnswers: MutableList<MutableList<String>>
//    lateinit var ordinaryAnswer: MutableState<String>
//    lateinit var dragAndDropAnswers: <String, String>
//}


//object MultipleAnswers{
//    lateinit var multipleAnswers: MutableList<String>
//}

/*
 items(Test.answers.size) { index ->
                onOptionSelected(Test.answers.keys.toList()[index])
                onOptionSelected(Test.answers.keys.toList()[index])
                Row(
                    modifier = Modifier
                        .padding(bottom = 30.dp)
                        .selectable(
                            selected = (Test.answers.keys.toList()[index] == Test.answersPoint[Test.counter.value]),
                            onClick = {
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
                            }
                        ),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .width(20.dp)
                            .height(20.dp)
                    ) {
                        Checkbox(
                            checked = Test.answers.keys.toList()[index] == Test.answersPoint[Test.counter.value],
                            onCheckedChange = { },
                            modifier = Modifier.padding(5.dp),
                            colors = CheckboxDefaults.colors(checkedColor = Color(0xff, 0xb6, 0xc1), checkmarkColor = Color.Red)
                        )
                        Log.d("Test.answers",  Test.answers.keys.toList()[index].toString())
                        Log.d("Test.answers",  Test.answersPoint[Test.counter.value].toString())
                    }

                    Text(
                        text = Test.answers[Test.answers.keys.toList()[index]]?.get(0).toString(),
                        fontSize = 13.sp,
                        modifier = Modifier.padding(start = 22.dp),
                        style = MaterialTheme.typography.subtitle2
                    )
                }
            }
 */

