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
 //   var scales: HashMap<String, Scale>? = null
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



//class TestItem(
//    var counter: MutableState<Int>,
//    var maxCounter: MutableState<Int>,
//    var counterQ: MutableState<Int>,
//    var progress: MutableState<Double>,
//    var toggleBtn: MutableState<Boolean>,
//
//    var questions: List<String>,
//    //var answers: MutableList<String>,
//    var answers: HashMap<Int, String>,
//    var userAnswers: MutableList<String>,
//    var answersPoint:  MutableList<Int>,
//    var answersBoolean: MutableList<Boolean>,
//
//    var context: Context,
//    var navController: NavController?= null,
//)


//data class TestItem(
//    var counter: MutableState<Int>,
//    var maxCounter: MutableState<Int>,
//    var counterQ: MutableState<Int>,
//    var progress: MutableState<Double>,
//    var toggleBtn: MutableState<Boolean>,
//
//    var questions: List<String>,
//    //var answers: MutableList<String>,
//    var answers: HashMap<Int, String>,
//    var userAnswers: MutableList<String>,
//    var answersPoint:  MutableList<Int>,
//    var answersBoolean: MutableList<Boolean>,
//
//    var context: Context,
//    var navController: NavController?= null,
//)


//    var scales: HashMap<String, Scale> ,
//    var sortedScales: Map<String, Scale>,


//data class Scale(
//    var counting: String? = null,
//    var countingR: String? = null,
//    var text: String? = null,
//    var desc: String? = null,
//    var res: HashMap<String, HashMap<String, String>>? = null
//) {}

@Parcelize
data class QuizEntityy(
    @SerializedName("id") val id: Int? = 0,
    @SerializedName("category") val category: String? = "",
    @SerializedName("filename") val filename: String? = "",
    @SerializedName("name") val name: String? = "",
    @SerializedName("description") val description: String? = "",
    @SerializedName("time") val time: String? = "",
    @SerializedName("questions") val questions: List<String> = emptyList(),
    @SerializedName("answers") val answers: List<String> = emptyList(),
    @SerializedName("views") val views: Int? = 0,
    @SerializedName("likes") val likes: Int? = 0,
    @SerializedName("needs_subscription") val requiresSubscription: Boolean = false
) : Parcelable


@Parcelize
data class QuizResultEntityy(
    @SerializedName("test_id") val test_id: Int? = 0,
    @SerializedName("user_id") val user_id: Int? = 0,
    @SerializedName("markdown") val markdown: String? = ""
) : Parcelable
