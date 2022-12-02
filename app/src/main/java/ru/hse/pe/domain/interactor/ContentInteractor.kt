package ru.hse.pe.domain.interactor

import io.reactivex.Single
import ru.hse.pe.data.api.ContentService
import ru.hse.pe.domain.model.ArticleEntity
import ru.hse.pe.domain.model.QuizEntity
import javax.inject.Inject

class ContentInteractor @Inject constructor(private val contentService: ContentService) {
    fun getQuizzes(): Single<List<QuizEntity>> = contentService.getQuizzes()
    fun getArticles(): Single<List<ArticleEntity>> = contentService.getArticles()
}