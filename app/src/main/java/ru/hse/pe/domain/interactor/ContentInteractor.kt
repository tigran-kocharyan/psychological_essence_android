package ru.hse.pe.domain.interactor

import io.reactivex.Single
import ru.hse.pe.data.api.ContentService
import ru.hse.pe.domain.model.ArticleEntity
import ru.hse.pe.domain.model.FactEntity
import ru.hse.pe.domain.model.QuizEntity
import ru.hse.pe.domain.model.RecommendationEntity
import javax.inject.Inject

class ContentInteractor @Inject constructor(private val contentService: ContentService) {
    fun getQuizzes(): Single<List<QuizEntity>> = contentService.getQuizzes()
    fun getArticles(): Single<List<ArticleEntity>> = contentService.getArticles()
    fun getFacts(): Single<List<FactEntity>> = contentService.getFacts()
    fun getRecommendations(): Single<List<RecommendationEntity>> =
        contentService.getRecommendations()
}