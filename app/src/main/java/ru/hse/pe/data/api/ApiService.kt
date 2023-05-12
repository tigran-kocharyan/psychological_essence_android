package ru.hse.pe.data.api

import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.http.*
import ru.hse.pe.domain.model.*

/**
 * API service for getting content from db.
 *
 * @author Tigran Kocharyan
 */
interface ApiService {

    /**
     * Method calls API to get the quizzes data from DB
     */
    @GET(GET_QUIZZES)
    fun getQuizzes(): Single<List<QuizEntity>>

    /**
     * Method calls API to get the quiz from DB
     */
    @GET(GET_QUIZ)
    fun getQuiz(@Path("id") id: String): Single<QuizEntity>

    /**
     * Method calls API to get the quizzes result data from DB
     */
    @POST(GET_QUIZ_RESULT)
    fun getQuizResult(@Body answer: QuizAnswerEntity): Single<QuizResultEntity>

    /**
     * Method calls API to get the courses data from DB
     */
    @GET(GET_COURSES)
    fun getCourses(): Single<List<CourseEntity>>

    /**
     * Method calls API to get the courses without lessons data from DB
     */
    @GET(GET_SHORT_COURSES)
    fun getShortCourses(): Single<List<CourseEntity>>

    /**
     * Method calls API to get the lesson from DB
     */
    @GET(GET_LESSON)
    fun getLesson(@Path("id") id: String): Single<LessonEntity>

    /**
     * Method calls API to get the articles data from DB
     */
    @GET(GET_ARTICLES)
    fun getArticles(): Single<List<ArticleEntity>>

    /**
     * Method calls API to get the techniques data from DB
     */
    @GET(GET_TECHNIQUES)
    fun getTechniques(): Single<List<ArticleEntity>>

    /**
     * Method calls API to get the recommendations data from DB
     */
    @GET(GET_RECOMMENDATIONS)
    fun getRecommendations(): Single<List<RecommendationEntity>>

    /**
     * Method calls API to get the facts data from DB
     */
    @GET(GET_FACTS)
    fun getFacts(): Single<List<FactEntity>>

    /**
     * Method calls API to add the user to DB
     */
    @POST(ADD_USER)
    fun addUser(@Body user: UserEntity): Completable

    /**
     * Method calls API to get the user data from DB
     */
    @GET(GET_USER)
    fun getUser(@Path("uid") uid: String): Single<UserEntity>

    /**
     * Method calls API to get the user data from DB
     */
    @GET(GET_USER_WITH_CREDENTIALS)
    fun getUserWithCredentials(
        @Path("email") email: String,
        @Path("password") password: String
    ): Single<UserEntity>

    /**
     * Method calls API to get the user data from DB
     */
    @GET(GET_USER_SUBSCRIPTION)
    fun getUserSubscriptionStatus(@Path("uid") uid: String): Single<Boolean>

    /**
     * Method calls API to get the subscription unique temporary url
     */
    @GET(GET_SUBSCRIPTION_URL)
    fun getSubscriptionUrl(@Path("uid") uid: String): Single<String>

    companion object {
        private const val GET_QUIZ = "extapi/quizzes/id/{id}"
        private const val GET_SHORT_COURSES = "extapi/courses/fast"
        private const val GET_LESSON = "extapi/lessons/id/{id}"
        private const val GET_COURSES = "extapi/courses"
        private const val GET_ARTICLES = "extapi/articles"
        private const val GET_TECHNIQUES = "extapi/techniques"
        private const val GET_QUIZZES = "extapi/quizzes"
        private const val GET_QUIZ_RESULT = "extapi/quiz/submit-answers"
        private const val GET_RECOMMENDATIONS = "extapi/recommendations"
        private const val GET_FACTS = "extapi/facts"
        private const val GET_USER = ""
        private const val GET_USER_WITH_CREDENTIALS = "user/login"
        private const val GET_USER_SUBSCRIPTION = "user/is_subscribed/{uid}"
        private const val GET_SUBSCRIPTION_URL = "payment/link_uid/{uid}"
        private const val ADD_USER = "extapi/user/add"
    }
}