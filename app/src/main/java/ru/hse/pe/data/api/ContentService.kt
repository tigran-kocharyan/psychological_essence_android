package ru.hse.pe.data.api

import io.reactivex.Single
import retrofit2.http.*
import ru.hse.pe.domain.model.ArticleEntity
import ru.hse.pe.domain.model.QuizEntity

/**
 * Event API for getting content from db.
 *
 * @author Tigran Kocharyan
 */
interface ContentService {

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

    companion object {
        private const val GET_ARTICLES = "articles"
        private const val GET_QUIZZES = "quizzes"
    }
}