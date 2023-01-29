import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.hse.pe.R
import ru.hse.pe.domain.model.TestItem
import ru.hse.pe.presentation.test.TestActivity
import ru.hse.pe.utils.Utils


@Composable
fun Results() {

    Column {
        Utils.SystemBarsNotVisible()
        //Utils.MyTopAppBar(getString(R.string.results), false)
        Utils.MyTopAppBar("Просто тестик", false)

        TestItem.userAnswers.removeAt(0)
        Log.d("userAnswers", TestItem.userAnswers.toString())

        Box(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxSize(0.87f)
                .background(Color.White),
        ) {
            LazyColumn {
                items(2) { index ->
                    Column {
                        Text(
                            text = "Ваши результаты: ",
                            fontSize = 18.sp,
                            modifier = Modifier.padding(top = 32.dp, bottom = 13.dp),
                            style = MaterialTheme.typography.h3,
                        )

                        Text(
                            text = "130/130",
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
            modifier = Modifier.padding(start = 25.dp, end = 25.dp).fillMaxWidth()
                .height(45.dp),
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


//package ru.hse.pe.presentation.test
//
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material.*
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.MutableState
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.res.colorResource
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.res.stringResource
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.navigation.NavController
//import ru.hse.pe.R
//import ru.hse.pe.domain.model.Scale
//import ru.hse.pe.utils.Utils.SystemBarsNotVisible
//
//class ResultsClass : TestClass(){
//
//    @Composable
//    fun Results() {
//        Column {
//            SystemBarsNotVisible()
//            MyTopResBar("Результаты: ")
//
//            Box(
//                modifier = Modifier
//                    .padding(24.dp)
//                    .fillMaxSize(0.87f)
//                    .background(Color.White),
//            ) {
//                LazyColumn {
//                    val keys = testItem.sortedScales.keys.toList()
//                    var resDiff: HashMap<String, HashMap<String, String>>
//                    var resSize: Int
//
//                    var text: String
//                    var desc = ""
//
//                    val reversePoints = hashMapOf<Int, Int>()
//                    val answersKeyList = testItem.answers.keys.toList()
//
//                    var from = 0
//                    var to = answersKeyList.size - 1
//
//                    while (from < answersKeyList.size) {
//                        reversePoints[answersKeyList[to]] = answersKeyList[from]
//                        from++
//                        to--
//                    }
//
//                    items(testItem.sortedScales.size) { index ->
//                        Column() {
//                            val counting = testItem.sortedScales[keys[index]]?.counting
//                                ?.split(", ")?.toList()
//                            val countingR = testItem.sortedScales[keys[index]]?.countingR
//                                ?.split(", ")?.toList()
//                            var mark = 0
//
//                            if (counting != null) {
//                                for (i in counting.indices) {
//                                    mark += testItem.answersPoint[counting[i].toInt()]
//                                }
//                            }
//
//                            if (countingR != null) {
//                                for (i in countingR.indices) {
//                                    mark += reversePoints[testItem.answersPoint[countingR[i].toInt()]]!!
//                                }
//                            }
//
//                            if (testItem.sortedScales[keys[index]]?.res != null) {
//                                resDiff = testItem.sortedScales[keys[index]]?.res!!
//
//                                val resDiffKeys = resDiff.keys.toList()
//                                resSize = resDiffKeys.size
//                                for (i in 0 until resSize) {
//                                    val points = resDiff[resDiffKeys[i]]?.get("points")
//                                    var leftNum: Int
//                                    var rightNum: Int
//
//                                    if (points != null) {
//                                        if (points.contains("-")) {
//                                            val pointArr = points.split("-")
//                                            leftNum = pointArr[0].toInt()
//                                            rightNum = pointArr[1].toInt()
//
//                                            if (mark in leftNum..rightNum) {
//                                                desc =
//                                                    resDiff[resDiffKeys[i]]?.get("desc").toString()
//                                                break
//                                            }
//                                        } else {
//                                            leftNum = points.toInt()
//                                            if (mark > leftNum) {
//                                                desc =
//                                                    resDiff[resDiffKeys[i]]?.get("desc").toString()
//                                                break
//                                            }
//                                        }
//                                    }
//                                }
//                            } else {
//                                desc = testItem.sortedScales[keys[index]]?.desc.toString()
//                            }
//
//                            val textArray = testItem.sortedScales[keys[index]]?.text
//                                .toString().split("mark")
//                            text = textArray[0] + mark + textArray[1]
//
//                            Text(
//                                text = text,
//                                fontSize = 18.sp,
//                                modifier = Modifier.padding(top = 32.dp, bottom = 13.dp),
//                                style = MaterialTheme.typography.h3,
//                            )
//
//                            Text(
//                                text = desc,
//                                modifier = Modifier.padding(bottom = 24.dp),
//                                style = MaterialTheme.typography.h4,
//                            )
//                        }
//                    }
//                }
//            }
//
//            Button(
//                onClick = {
//                    TODO()
//                },
//                shape = RoundedCornerShape(15.dp),
//                modifier = Modifier.padding(start = 25.dp, end = 25.dp).fillMaxWidth().height(45.dp),
//                colors = ButtonDefaults
//                    .buttonColors(
//                        backgroundColor = colorResource(id = R.color.purple),
//                        contentColor = Color.White
//                    )
//            ) {
//                Text(text = stringResource(id = R.string.backToTests))
//            }
//        }
//    }
//
//
//    @Composable
//    fun MyTopResBar(name: String) {
//        Card(
//            backgroundColor = colorResource(id = R.color.purple),
//            contentColor = Color.White,
//            modifier = Modifier.height(78.dp),
//            shape = RoundedCornerShape(bottomStart = 15.dp, bottomEnd = 15.dp),
//        ) {
//            Row(
//                horizontalArrangement = Arrangement.Center,
//                modifier = Modifier.fillMaxWidth(),
//                verticalAlignment = Alignment.CenterVertically
//            )
//            {
//                Text(
//                    text = name,
//                    modifier = Modifier.padding(horizontal = 30.dp).width(250.dp),
//                    textAlign = TextAlign.Center,
//                    style = MaterialTheme.typography.subtitle1
//                )
//            }
//        }
//    }
//}
//
////@Composable
////fun Results(
////) {
////    Column {
////        SystemBarsNotVisible()
////        MyTopResBar(getString(R.string.results))
////
////        Box(
////            modifier = Modifier
////                .padding(24.dp)
////                .fillMaxSize(0.87f)
////                .background(Color.White),
////        ) {
////            LazyColumn {
////                val keys = sortedScales.keys.toList()
////                var resDiff: HashMap<String, HashMap<String, String>>
////                var resSize: Int
////
////                var text: String
////                var desc = ""
////
////                val reversePoints = hashMapOf<Int, Int>()
////                val answersKeyList = answers.keys.toList()
////
////                var from = 0
////                var to = answersKeyList.size - 1
////
////                while (from < answersKeyList.size) {
////                    reversePoints[answersKeyList[to]] = answersKeyList[from]
////                    from++
////                    to--
////                }
////
////                items(sortedScales.size) { index ->
////                    Column() {
////                        val counting = sortedScales[keys[index]]?.counting
////                            ?.split(", ")?.toList()
////                        val countingR = sortedScales[keys[index]]?.countingR
////                            ?.split(", ")?.toList()
////                        var mark = 0
////
////                        if (counting != null) {
////                            for (i in counting.indices) {
////                                mark += answersPoint[counting[i].toInt()]
////                            }
////                        }
////
////                        if (countingR != null) {
////                            for (i in countingR.indices) {
////                                mark += reversePoints[answersPoint[countingR[i].toInt()]]!!
////                            }
////                        }
////
////                        if (sortedScales[keys[index]]?.res != null) {
////                            resDiff = sortedScales[keys[index]]?.res!!
////
////                            val resDiffKeys = resDiff.keys.toList()
////                            resSize = resDiffKeys.size
////                            for (i in 0 until resSize) {
////                                val points = resDiff[resDiffKeys[i]]?.get("points")
////                                var leftNum: Int
////                                var rightNum: Int
////
////                                if (points != null) {
////                                    if (points.contains("-")) {
////                                        val pointArr = points.split("-")
////                                        leftNum = pointArr[0].toInt()
////                                        rightNum = pointArr[1].toInt()
////
////                                        if (mark in leftNum..rightNum) {
////                                            desc =
////                                                resDiff[resDiffKeys[i]]?.get("desc").toString()
////                                            break
////                                        }
////                                    } else {
////                                        leftNum = points.toInt()
////                                        if (mark > leftNum) {
////                                            desc =
////                                                resDiff[resDiffKeys[i]]?.get("desc").toString()
////                                            break
////                                        }
////                                    }
////                                }
////                            }
////                        } else {
////                            desc = sortedScales[keys[index]]?.desc.toString()
////                        }
////
////                        val textArray = sortedScales[keys[index]]?.text
////                            .toString().split("mark")
////                        text = textArray[0] + mark + textArray[1]
////
////                        Text(
////                            text = text,
////                            fontSize = 18.sp,
////                            modifier = Modifier.padding(top = 32.dp, bottom = 13.dp),
////                            style = MaterialTheme.typography.h3,
////                        )
////
////                        Text(
////                            text = desc,
////                            modifier = Modifier.padding(bottom = 24.dp),
////                            style = MaterialTheme.typography.h4,
////                        )
////                    }
////                }
////            }
////        }
////
////        Button(
////            onClick = {
////                TODO()
////            },
////            shape = RoundedCornerShape(15.dp),
////            modifier = Modifier.padding(start = 25.dp, end = 25.dp).fillMaxWidth().height(45.dp),
////            colors = ButtonDefaults
////                .buttonColors(
////                    backgroundColor = colorResource(id = R.color.purple),
////                    contentColor = Color.White
////                )
////        ) {
////            Text(text = stringResource(id = R.string.backToTests))
////        }
////    }
////}
////
////
////
////
////@Composable
////fun MyTopResBar(name: String) {
////    Card(
////        backgroundColor = colorResource(id = R.color.purple),
////        contentColor = Color.White,
////        modifier = Modifier.height(78.dp),
////        shape = RoundedCornerShape(bottomStart = 15.dp, bottomEnd = 15.dp),
////    ) {
////        Row(
////            horizontalArrangement = Arrangement.Center,
////            modifier = Modifier.fillMaxWidth(),
////            verticalAlignment = Alignment.CenterVertically
////        )
////        {
////            Text(
////                text = name,
////                modifier = Modifier.padding(horizontal = 30.dp).width(250.dp),
////                textAlign = TextAlign.Center,
////                style = MaterialTheme.typography.subtitle1
////            )
////        }
////    }
////}
//
//
////
////testItem.scales = test?.scales!!
////testItem.sortedScales = testItem.scales!!.toSortedMap()
////testItem.counter = remember { mutableStateOf(1) }
////testItem.maxCounter = remember { countQuestions?.let { mutableStateOf(it) } }
////testItem.counterQ = remember { mutableStateOf(0) }
////testItem.progress = remember { mutableStateOf(0.0) }
////testItem.toggleBtn = remember { mutableStateOf(false) }