package ru.hse.pe.domain.model

import android.content.Context
import android.os.Parcelable
import androidx.compose.runtime.MutableState
import androidx.navigation.NavController
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

data class Test(
    var name: String? = null,
    var desc: String? = null,
    var time: Int? = null,
    var countQuestions: Int? = null,
    var questions: List<String>? = null,
    var answers: HashMap<String, String>? = null,
) {}

object TestItem {
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

    lateinit var context: Context
}
