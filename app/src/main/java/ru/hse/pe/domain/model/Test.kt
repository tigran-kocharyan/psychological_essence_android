package ru.hse.pe.domain.model

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.navigation.NavController
import com.example.test.viewModels.TestViewModel

data class Test(
    var name: String? = null,
    var desc: String? = null,
    var time: Int? = null,
    var countQuestions: Int? = null,
    var questions: List<String>? = null,
    var answers: HashMap<String, String>? = null,
    var scales: HashMap<String, Scale>? = null
) {}


data class TestItem(
    var counter: MutableState<Int>,
    var maxCounter: MutableState<Int>,
    var counterQ: MutableState<Int>,
    var progress: MutableState<Double>,
    var toggleBtn: MutableState<Boolean>,

    var questionsMap: HashMap<Int, String>,
    var answers: HashMap<Int, String>,
    var answersPoint: MutableList<Int>,
    var answersBoolean: MutableList<Boolean>,

    var scales: HashMap<String, Scale> ,
    var sortedScales: Map<String, Scale>,

    var context: Context,
    var navController: NavController?= null,
)


data class Scale(
    var counting: String? = null,
    var countingR: String? = null,
    var text: String? = null,
    var desc: String? = null,
    var res: HashMap<String, HashMap<String, String>>? = null
) {}


