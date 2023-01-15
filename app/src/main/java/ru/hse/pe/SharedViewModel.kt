package ru.hse.pe

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.hse.pe.domain.model.ArticleEntity
import ru.hse.pe.domain.model.FactEntity
import ru.hse.pe.domain.model.RecommendationEntity

class SharedViewModel : ViewModel() {
    private var _article = MutableLiveData<ArticleEntity?>(null)
    private var _fact = MutableLiveData<FactEntity?>(null)
    private var _recommendation = MutableLiveData<RecommendationEntity?>(null)

    var article: LiveData<ArticleEntity?> = _article
    var recommendation: LiveData<RecommendationEntity?> = _recommendation
    var fact: LiveData<FactEntity?> = _fact

    fun setArticle(article: ArticleEntity) {
        _article.value = article
    }

    fun setRecommendation(recommendation: RecommendationEntity) {
        _recommendation.value = recommendation
    }

    fun setFact(fact: FactEntity) {
        _fact.value = fact
    }
}