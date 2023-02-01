package ru.hse.pe

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.hse.pe.domain.model.*

class SharedViewModel : ViewModel() {
    private var _uid = MutableLiveData<String>(null)
    private var _article = MutableLiveData<ArticleEntity?>(null)
    private var _fact = MutableLiveData<FactEntity?>(null)
    private var _quiz = MutableLiveData<QuizEntity?>(null)
    private var _recommendation = MutableLiveData<RecommendationEntity?>(null)

    val uid: LiveData<String> get() = _uid
    var article: LiveData<ArticleEntity?> = _article
    var recommendation: LiveData<RecommendationEntity?> = _recommendation
    var fact: LiveData<FactEntity?> = _fact
    var quiz: LiveData<QuizEntity?> = _quiz

    fun setUser(uid: String) {
        _uid.value = uid
    }

    fun setArticle(article: ArticleEntity) {
        _article.value = article
    }

    fun setRecommendation(recommendation: RecommendationEntity) {
        _recommendation.value = recommendation
    }

    fun setQuiz(quiz: QuizEntity) {
        _quiz.value = quiz
    }

    fun setFact(fact: FactEntity) {
        _fact.value = fact
    }
}