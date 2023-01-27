package ru.hse.pe.domain.interactor

import io.reactivex.Single
import ru.hse.pe.data.api.ApiService
import ru.hse.pe.domain.model.ArticleEntity
import ru.hse.pe.domain.model.FactEntity
import ru.hse.pe.domain.model.QuizEntity
import ru.hse.pe.domain.model.RecommendationEntity
import javax.inject.Inject

class ContentInteractor @Inject constructor(private val apiService: ApiService) {
    fun getQuizzes(): Single<List<QuizEntity>> = apiService.getQuizzes()
    fun getArticles(): Single<List<ArticleEntity>> = apiService.getArticles()
    fun getFacts(): Single<List<FactEntity>> = apiService.getFacts()
    fun getRecommendations(): Single<List<RecommendationEntity>> =
        apiService.getRecommendations()
}