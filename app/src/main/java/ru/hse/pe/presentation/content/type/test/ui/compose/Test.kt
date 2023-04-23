package ru.hse.pe.presentation.content.type.test.ui.compose

import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.burnoutcrew.reorderable.*
import ru.hse.pe.R
import ru.hse.pe.SharedViewModel
import ru.hse.pe.domain.model.QuizMetaDataEntity
import ru.hse.pe.presentation.content.type.test.ui.TestResultFragment
import ru.hse.pe.utils.Utils
import ru.hse.pe.utils.Utils.setCommonAnimations


/*
* Здесь подготавливаются к отображению данные с сервера
*/
@Composable
fun Test(
    sharedViewModel: SharedViewModel,
) {
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
    val indexCounter = remember { mutableStateOf(0) } // счетчик, который начинается с 0
    val maxCounter = remember { mutableStateOf(countQuestions) } // макс кол-во отвеченных вопросов
    val counterQ = remember { mutableStateOf(0) } // подсчет отвеченных вопросов

    Box {
        for (i in 1 until maxCounter.value + 2) {
            answersPoint.add(-1)
            answersBoolean.add(false)
        }

        var k = 1
        val isOrdinaryTest = listQuestions.size != listAnswers!!.size
        for (i in listQuestions.indices) {
            val list = mutableListOf<String>()

            if (isOrdinaryTest) {
                for (j in 0 until listAnswers[0].size) {
                    list.add(listAnswers[0][j])
                }
            } else {
                for (j in 0 until listAnswers[i].size) {
                    list.add(listAnswers[i][j])
                }
            }
            answers[k] = list
            k++
        }

        Test.counter = counter
        Test.indexCounter = indexCounter
        Test.maxCounter = maxCounter
        Test.counterQ = counterQ
        Test.progress = progress
        Test.toggleBtn = toggleBtn
        Test.questions = listQuestions
        Test.answers = answers
        Test.userAnswers = userAnswers
        Test.answersPoint = answersPoint
        Test.answersBoolean = answersBoolean
        Test.dragAndDropItems = mutableListOf()
        Test.dragAndDropItemsCopied = mutableListOf()
        Test.dragAndDropAnswersData = hashMapOf()

        // Подготавливаем шаблон с ответами
        Test.userAnswers.add("") // Отсчет начинается с 1
        var j = 1

        if (quizMetaData != null) {
            for (i in 0 until maxCounter.value) {
                if (quizMetaData[i]?.multiple_answers == true) {
                    Test.userAnswers.add(j, mutableListOf<String>())
                } else if (quizMetaData[i]?.categories?.isNotEmpty() == true) {
                    Test.userAnswers.add(j, mutableMapOf<String, List<String>>())
                } else {
                    Test.userAnswers.add(j, "")
                }
                j++
            }
            Test.quizMetaData = quizMetaData
        } else {
            Test.quizMetaData = mapOf()
            for (i in 0 until maxCounter.value) {
                Test.userAnswers.add(j, "")
                j++
            }
        }

        // Это список для сохранения несколько вариантов ответа
        val listOfMultipleAnswers = mutableListOf<MutableList<String>>()
        listOfMultipleAnswers.add(mutableListOf())
        for (i in 1 until Test.answers.size + 1) {
            val list = mutableListOf<String>()
            for (j in 0 until Test.answers[i]!!.size) {
                list.add("")
            }
            listOfMultipleAnswers.add(list)
        }
        Test.multipleAnswers = listOfMultipleAnswers

        // Заполняются значения dragAndDrop
        val dragAndDropAnswers = hashMapOf<Int, HashMap<String, MutableList<String>>>()
        for ((key, value) in Test.quizMetaData) {
            val hashMap = hashMapOf<String, MutableList<String>>()
            val indexKey = key + 1
            if (value.categories?.isEmpty() == false) {
                for (i in value.categories.indices) {
                    if (i == value.categories.size - 1) {
                        hashMap[value.categories[value.categories.size - 1]] =
                            Test.answers[indexKey]?.toMutableList()!!
                    } else {
                        hashMap[value.categories[i]] = mutableListOf()
                    }

                }
            }
            dragAndDropAnswers[key] = hashMap
            Test.dragAndDropAnswersData[key] = mutableListOf()
        }
        Test.dragAndDropAnswers = dragAndDropAnswers
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

/*
* Показывается сам вопрос и варианты ответа
*/
@Composable
fun CardItem() {
    Card(
        elevation = 0.dp,
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 32.dp, end = 27.dp, bottom = 20.dp),
    ) {
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.height(LocalConfiguration.current.screenHeightDp.dp)
        ) {
            item {
                CreateTopPartCard()
                CreateAnswersCard()
            }

        }
    }
}

/*
* Показывается верхняя часть вопроса:
* кол-во вопросов, индикатор прогресса и сам вопрос
*/
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
                "0${Test.counterQ.value} / "
            } else {
                "${Test.counterQ.value} / "
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
            .padding(top = 16.dp, bottom = 15.dp, start = 40.dp),
        text = Test.questions[Test.counter.value - 1],
        style = MaterialTheme.typography.subtitle1
    )
}

/*
* Показываются варианты ответов:
*/
@Composable
fun CreateAnswersCard() {
    val counter = Test.counter.value - 1
    if (Test.quizMetaData[counter]?.multiple_answers == true) {
        MultipleAnswers()
    } else if (Test.quizMetaData[counter]?.categories?.isEmpty() == false) {
        val categories = Test.quizMetaData[counter]?.categories
        if (categories != null) {
            DragAndDrop(categories)
            val diff = 1.0 / Test.maxCounter.value
            if (!Test.answersBoolean[Test.counter.value]) {
                Test.counterQ.value++
                if (Test.progress.value < 1.0f) Test.progress.value += diff
                Test.answersBoolean[Test.counter.value] = true

                if (isFinish()) {
                    Test.toggleBtn.value = true
                }
            }
        }
    } else {
        OrdinaryAnswers()
    }
}

/*
* Варианты ответов со множественным выбором
*/
@Composable
fun MultipleAnswers() {
    val (selectedOption, onOptionSelected) = remember { mutableStateOf(0) }
    LazyColumn(
        modifier = Modifier
            .selectableGroup()
            .fillMaxWidth()
            .padding(bottom = 40.dp, start = 40.dp)
            .heightIn(max = 3000.dp),
        horizontalAlignment = Alignment.Start
    ) {
        val diff = 1.0 / Test.maxCounter.value
        val onOptionSelectedKeys = mutableListOf<Int>()
        for (i in 0 until Test.answers[Test.counter.value]?.size!!) {
            onOptionSelectedKeys.add(i)
        }
        items(Test.answers[Test.counter.value]!!.size) { index ->
            val isChecked =
                remember { mutableStateOf(Test.multipleAnswers[Test.counter.value][index] != "") }
            onOptionSelected(onOptionSelectedKeys[index])
            Row(
                modifier = Modifier
                    .padding(bottom = 30.dp)
                    .selectable(
                        selected = Test.multipleAnswers[Test.counter.value][index] != "",
                        onClick = {
                            onClickMultipleAnswers(index, diff, isChecked)
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
                        checked = Test.multipleAnswers[Test.counter.value][index] != "",
                        onCheckedChange = {
                            onClickMultipleAnswers(index, diff, isChecked)
                        },
                        modifier = Modifier.padding(5.dp),
                        colors = CheckboxDefaults.colors(
                            checkedColor = Color(R.color.purple),
                        )
                    )
                }

                Text(
                    text = Test.answers[Test.counter.value]?.get(index).toString(),
                    fontSize = 13.sp,
                    modifier = Modifier.padding(start = 22.dp),
                    style = MaterialTheme.typography.subtitle2
                )
            }
        }
    }
    CreateBtnCard()

}

/*
* Обработка нажатия на вопрос со множественным выбором
*/
fun onClickMultipleAnswers(index: Int, diff: Double, isChecked: MutableState<Boolean>) {
    if (isChecked.value) {
        Test.multipleAnswers[Test.counter.value][index] = ""
        isChecked.value = false
    } else {
        Test.multipleAnswers[Test.counter.value][index] =
            Test.answers[Test.counter.value]
                ?.get(index)
                .toString()
        isChecked.value = true
    }

    if (!Test.answersBoolean[Test.counter.value]) {
        Test.counterQ.value++
        if (Test.progress.value < 1.0f) Test.progress.value += diff
        Test.answersBoolean[Test.counter.value] = true

        if (isFinish()) {
            Test.toggleBtn.value = true
        }
    }

    Test.userAnswers[Test.counter.value] =
        Test.multipleAnswers[Test.counter.value]
}

/*
* Варианты ответов с dragAndDrop
*/
@Composable
fun DragAndDrop(categories: List<String>) {
    var values = mutableListOf<ItemData>()
    if (Test.dragAndDropAnswersData[Test.indexCounter.value]!!.isEmpty()) {
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

        val answers = Test.answers[Test.counter.value]
        for (answer in answers!!) {
            values.add(ItemData(answer, "id$id", false))
            id++
        }
    } else {
        values = Test.dragAndDropItems
    }

    var data by remember {
        mutableStateOf(
            values
        )
    }
    Test.dragAndDropItems = data

    val state = rememberReorderableLazyListState(
        onMove = { from, to ->
            data = data.toMutableList().apply {
                add(to.index, removeAt(from.index))
            }
        },
        canDragOver = { draggedOver, _ ->
            isDragEnabled(draggedOver, data)
        },
        onDragEnd = { _: Int, _: Int ->
            // Отслеживаем изменение списка
            val dragAndDropAnswer = hashMapOf<String, MutableList<String>>()
            var category = ""

            Test.dragAndDropItemsCopied = mutableListOf()
            val iterator = Test.dragAndDropItems.iterator()
            while (iterator.hasNext()) {
                val itemData = iterator.next()
                if (itemData.title != "") {
                    if (itemData.isLocked) {
                        category = itemData.title
                        dragAndDropAnswer[itemData.title] = mutableListOf()
                    } else {
                        dragAndDropAnswer[category]?.add(itemData.title)
                    }
                }
                Test.dragAndDropItemsCopied.add(itemData)
            }
        }
    )

    LazyColumn(
        state = state.listState,
        modifier = Modifier
            .reorderable(state)
            .detectReorderAfterLongPress(state)
            .fillMaxWidth()
            .padding(bottom = 40.dp, start = 40.dp)
            .heightIn(max = 2000.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        userScrollEnabled = false,
    ) {
        items(Test.dragAndDropItems, { item -> item.key }) { item ->
            ReorderableItem(state, item.key) { dragging ->
                if (item.isLocked) {
                    Column(
                        modifier = Modifier.fillMaxWidth()
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
    CreateBtnCard()
}

// Разрешено ли перетаскивать элементы в dragAndDrop
// Категорию - нет, варианты ответов - да
fun isDragEnabled(draggedOver: ItemPosition, data: List<ItemData>) =
    data.getOrNull(draggedOver.index)?.isLocked != true

/*
* Варианты ответов с одиночным выбором
*/
@Composable
fun OrdinaryAnswers() {
    val (selectedOption, onOptionSelected) = remember { mutableStateOf(0) }
    LazyColumn(
        modifier = Modifier
            .selectableGroup()
            .fillMaxWidth()
            .padding(bottom = 40.dp, start = 40.dp)
            .heightIn(max = 2000.dp),
        horizontalAlignment = Alignment.Start
    ) {
        val diff = 1.0 / Test.maxCounter.value
        val onOptionSelectedKeys = mutableListOf<Int>()
        for (i in 0 until Test.answers[Test.counter.value]?.size!!) {
            onOptionSelectedKeys.add(i)
        }

        items(Test.answers[Test.counter.value]!!.size) { index ->
            onOptionSelected(onOptionSelectedKeys[index])
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .selectable(
                        selected = (Test.answers.keys.toList()[index] == Test.answersPoint[Test.counter.value]),
                        onClick = {
                            Test.answersPoint[Test.counter.value] =
                                Test.answers.keys.toList()[index]

                            Test.userAnswers[Test.counter.value] =
                                Test.answers[Test.counter.value]
                                    ?.get(index)
                                    .toString()

                            if (!Test.answersBoolean[Test.counter.value]) {
                                Test.counterQ.value++
                                if (Test.progress.value < 1.0f) Test.progress.value += diff
                                Test.answersBoolean[Test.counter.value] = true

                                if (isFinish()) {
                                    Test.toggleBtn.value = true
                                }
                            }
                        }

                    )
                    .padding(bottom = 15.dp, top = 15.dp),
                verticalAlignment = Alignment.CenterVertically,
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
                    text = Test.answers[Test.counter.value]?.get(index).toString(),
                    fontSize = 13.sp,
                    modifier = Modifier.padding(start = 22.dp),
                    style = MaterialTheme.typography.subtitle2
                )
            }
        }
    }

    CreateBtnCard()
}

/*
* Показываются кнопки завершить, следующий, предыдущий
*/
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
                val counterMultipleAnswer = checkMultipleAnswers()
                if (!Test.answersBoolean[Test.counter.value] || counterMultipleAnswer == 0) {
                    Toast.makeText(
                        context,
                        "Выберите вариант ответа или нажмите кнопку пропустить",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@Button
                } else {
                    checkDragAndDrop()
                    Test.counter.value--
                    Test.indexCounter.value--

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
                if (!Test.answersBoolean[Test.counter.value] || counterMultipleAnswer == 0) {
                    Toast.makeText(
                        context,
                        "Выберите вариант ответа или нажмите кнопку пропустить",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@Button
                } else {
                    checkDragAndDrop()
                    Test.counter.value++
                    Test.indexCounter.value++
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
                validateTest()
                activity.supportFragmentManager
                    .beginTransaction()
                    .setCommonAnimations()
                    .addToBackStack(null)
                    .add(
                        R.id.fragment_container, TestResultFragment.newInstance(),
                        TestResultFragment.TAG
                    )
                    .commit()
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
            Text(text = stringResource(id = R.string.finishBtn), textAlign = TextAlign.Center)
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

// Проверяем заполнен ли хотя бы один ответ из множественного выбора
fun checkMultipleAnswers(): Int {
    var counterMultipleAnswer = -1
    val tempMultipleAnswersItems = mutableListOf<String>()
    if (Test.userAnswers[Test.counter.value] is MutableList<*>) {
        counterMultipleAnswer = 0
        for (multipleAnswer in Test.userAnswers[Test.counter.value] as MutableList<*>) {
            if (multipleAnswer != null) {
                if (multipleAnswer != "") {
                    counterMultipleAnswer++
                    tempMultipleAnswersItems.add(multipleAnswer as String)
                }
            }
        }
        Test.userAnswers[Test.counter.value] = tempMultipleAnswersItems
    }

    return counterMultipleAnswer
}

// Проверяем ответил ли пользователь на все вопросы
fun isFinish(): Boolean {
    var c = 0
    for (i in 1 until Test.answersBoolean.size) {
        if (Test.answersBoolean[i]) {
            c++
        }
    }
    return c == Test.maxCounter.value
}

// Готовим данные drag and drop для отправки на сервер
fun checkDragAndDrop() {
    for ((key, value) in Test.dragAndDropAnswers) {
        // является ли вопрос dragAndDrop
        if (key == Test.indexCounter.value && value.isNotEmpty()) {
            Test.dragAndDropAnswersData[Test.indexCounter.value] = Test.dragAndDropItemsCopied
            Test.dragAndDropItems = Test.dragAndDropItemsCopied

            val map = mutableMapOf<String, MutableList<String>>()
            val data = Test.dragAndDropAnswersData[Test.indexCounter.value]
            var category = ""
            if (data != null) {
                for (item in data) {
                    if (item.title != "") {
                        if (item.isLocked) {
                            category = item.title
                            map[category] = mutableListOf()
                        } else {
                            map[category]?.add(item.title)
                        }
                    }
                }
            }
            Test.userAnswers[Test.counter.value] = map
        }
    }
}

// Готовим данные для отправки на сервер
fun validateTest() {
    Test.userAnswers.removeAt(0)
    val userAnswersCopied = mutableListOf<Any>()
    // Убираю пустые значения в множественном выборе
    for (userAnswer in Test.userAnswers) {
        if (userAnswer is HashMap<*, *>) {
            val dragAndDropCopied = mutableMapOf<String, List<String>>()
            for ((key, value) in userAnswer) {
                if (value is MutableList<*> && value.isEmpty()) {
                    continue
                } else {
                    dragAndDropCopied[key as String] = value as List<String>
                }
            }
            userAnswersCopied.add(dragAndDropCopied)
        } else if (userAnswer is MutableList<*>) {
            userAnswersCopied.add(userAnswer)
        } else {
            userAnswersCopied.add(userAnswer)
        }
    }

    Test.userAnswers = userAnswersCopied
    Test.userAnswersString = mutableListOf()

    // здесь преобразуею mutableList<Any> -> mutableList<String>
    for (userAnswer in Test.userAnswers) {
        var str = ""
        when (userAnswer) {
            is HashMap<*, *> -> {
                val userAnswerMap = userAnswer as MutableMap<String, List<String>>
                var k = 0
                for ((key, value) in userAnswerMap) {
                    str += if (k == 0) {
                        "{\"$key\":"
                    } else {
                        "\"$key\":"
                    }
                    for ((c, userAnswerItem) in value.withIndex()) {
                        val userAnswerItemString = userAnswerItem
                        if (value.size > 1) {
                            if (c == 0) {
                                str += "[\"$userAnswerItemString\", "
                            } else if (c == value.size - 1) {
                                str += "\"$userAnswerItemString\"]"
                            } else {
                                str += "\"$userAnswerItemString\", "
                            }
                        } else {
                            str += "[\"${userAnswerItemString}\"]"
                        }

                    }
                    if (k != userAnswerMap.size - 1) {
                        str += ", "
                    }
                    k++
                }
                str += "}"
                Test.userAnswersString.add(str)
            }
            is MutableList<*> -> {
                if (userAnswer.size > 1) {
                    for ((c, userAnswerItem) in userAnswer.withIndex()) {
                        val userAnswerItemString = userAnswerItem.toString()
                        if (c == 0) {
                            str += "[\"$userAnswerItemString\", "
                        } else if (c == userAnswer.size - 1) {
                            str += "\"$userAnswerItemString\"]"
                        } else {
                            str += "\"$userAnswerItemString\", "
                        }
                    }
                } else {
                    str += "[\"${userAnswer.joinToString()}\"]"
                }
                Test.userAnswersString.add(str)
            }
            else -> {
                Test.userAnswersString.add(userAnswer.toString())
            }
        }
    }
}

// вариант ответа или категория в dragAndDrop
data class ItemData(val title: String, val key: String, val isLocked: Boolean = false)

// Специальный объект для доступа к переменным из любого места программы
object Test {
    lateinit var counter: MutableState<Int>
    lateinit var indexCounter: MutableState<Int>
    lateinit var maxCounter: MutableState<Int>
    lateinit var counterQ: MutableState<Int>
    lateinit var progress: MutableState<Double>
    lateinit var toggleBtn: MutableState<Boolean>
    lateinit var questions: List<String>
    lateinit var quizMetaData: Map<Int, QuizMetaDataEntity>
    lateinit var answers: HashMap<Int, List<String>>
    lateinit var userAnswers: MutableList<Any>
    var userAnswersString = mutableListOf<String>()
    lateinit var answersPoint: MutableList<Int>
    lateinit var answersBoolean: MutableList<Boolean>

    lateinit var multipleAnswers: MutableList<MutableList<String>>
    lateinit var dragAndDropItems: MutableList<ItemData>
    lateinit var dragAndDropItemsCopied: MutableList<ItemData>
    lateinit var dragAndDropAnswers: HashMap<Int, HashMap<String, MutableList<String>>>
    lateinit var dragAndDropAnswersData: HashMap<Int, MutableList<ItemData>>
}