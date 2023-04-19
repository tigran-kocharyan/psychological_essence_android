package ru.hse.pe.domain.interactor

import io.reactivex.Single
import ru.hse.pe.data.api.ApiService
import ru.hse.pe.domain.model.*
import javax.inject.Inject

/**
 * Interactor to obtain content data from ViewModels
 *
 * @param apiService service for content related api calls
 */
class ContentInteractor @Inject constructor(private val apiService: ApiService) {

    /**
     * Obtain quizzes from server data store
     *
     * @return List<QuizEntity> wrapped into Single flow
     */
    fun getQuizzes(): Single<List<QuizEntity>> = apiService.getQuizzes()
    fun getQuiz(id: String): Single<QuizEntity> = apiService.getQuiz(id)

    /**
     * Obtain quiz result from server data store
     *
     * @param answer answers of user for quiz
     * @return QuizResultEntity wrapped into Single flow
     */
    fun getQuizResult(answer: QuizAnswerEntity): Single<QuizResultEntity> =
        apiService.getQuizResult(answer)

    /**
     * Obtain courses from server data store
     *
     * @return List<CourseEntity> wrapped into Single flow
     */
    fun getCourses(): Single<List<CourseEntity>> = apiService.getCourses()
    fun getFastCourses(): Single<List<CourseEntity>> = apiService.getShortCourses()
    fun getLesson(id: String): Single<LessonEntity> = apiService.getLesson(id)

    /**
     * Obtain articles from server data store
     *
     * @return List<ArticleEntity> wrapped into Single flow
     */
    fun getArticles(): Single<List<ArticleEntity>> = apiService.getArticles()

    /**
     * Obtain techniques from server data store
     *
     * @return List<ArticleEntity> wrapped into Single flow
     */
    fun getTechniques(): Single<List<ArticleEntity>> = apiService.getTechniques()

    /**
     * Obtain facts from server data store
     *
     * @return List<FactEntity> wrapped into Single flow
     */
    fun getFacts(): Single<List<FactEntity>> = apiService.getFacts()

    /**
     * Obtain recommendations from server data store
     *
     * @return RecommendationEntity wrapped into Single flow
     */
    fun getRecommendations(): Single<List<RecommendationEntity>> =
        apiService.getRecommendations()
}