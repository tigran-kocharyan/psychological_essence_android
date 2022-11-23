package ru.hse.pe.domain.model

data class Test(
    var name: String? = null,
    var desc: String? = null,
    var time: Int? = null,
    var countQuestions: Int? = null,
    var questions: List<String>? = null,
    var answers: HashMap<String, String>? = null,
    var scales: HashMap<String, Scale>? = null
) {}


data class Scale(
    var counting: String? = null,
    var countingR: String? = null,
    var text: String? = null,
    var desc: String? = null,
    var res: HashMap<String, HashMap<String, String>>? = null
) {}