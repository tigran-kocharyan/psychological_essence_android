package ru.hse.pe.domain.interactor

import io.reactivex.Single
import ru.hse.pe.data.api.ApiService
import ru.hse.pe.domain.model.*
import javax.inject.Inject

class ContentInteractor @Inject constructor(private val apiService: ApiService) {
    fun getQuizzes(): Single<List<QuizEntity>> = apiService.getQuizzes()
    fun getQuiz(id: String): Single<QuizEntity> = apiService.getQuiz(id)
    fun getQuizResult(answer: QuizAnswerEntity): Single<QuizResultEntity> =
        apiService.getQuizResult(answer)

    fun getCourses(): Single<List<CourseEntity>> = apiService.getCourses()
    fun getFastCourses(): Single<List<CourseEntity>> = apiService.getShortCourses()
    fun getLesson(id: String): Single<LessonEntity> = apiService.getLesson(id)
    fun getArticles(): Single<List<ArticleEntity>> = apiService.getArticles()
    fun getFacts(): Single<List<FactEntity>> = apiService.getFacts()
    fun getRecommendations(): Single<List<RecommendationEntity>> =
        apiService.getRecommendations()
}