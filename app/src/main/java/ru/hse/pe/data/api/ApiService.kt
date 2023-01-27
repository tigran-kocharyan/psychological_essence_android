package ru.hse.pe.data.api

import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.http.*
import ru.hse.pe.domain.model.*

/**
 * Event API for getting content from db.
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
     * Method calls API to get the articles data from DB
     */
    @GET(GET_ARTICLES)
    fun getArticles(): Single<List<ArticleEntity>>

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
    fun getUser(): Single<UserEntity>

    companion object {
        private const val GET_ARTICLES = "articles"
        private const val GET_QUIZZES = "quizzes"
        private const val GET_RECOMMENDATIONS = "recommendations"
        private const val GET_FACTS = "facts"
        private const val GET_USER = ""
        private const val ADD_USER = "user/add"
    }
}