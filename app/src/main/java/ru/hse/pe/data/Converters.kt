package ru.hse.pe.data

import androidx.room.TypeConverter
import com.google.gson.Gson
import ru.hse.pe.domain.model.ArticleEntity
import ru.hse.pe.domain.model.CourseEntity
import ru.hse.pe.domain.model.QuizEntity

/**
 * Object for converting the entities to json string and vice versa.
 */
object Converters {
    /**
     * Converts profile entity to JSON string.
     */
    @TypeConverter
    fun articleToJson(value: ArticleEntity?) = Gson().toJson(value)

    /**
     * Converts JSON string to article entity.
     */
    @TypeConverter
    fun jsonToArticle(value: String) = Gson().fromJson(value, ArticleEntity::class.java)

    /**
     * Converts Boolean to JSON string.
     */
    @TypeConverter
    fun booleanToJson(value: Boolean) = Gson().toJson(value)

    /**
     * Converts JSON string to Boolean.
     */
    @TypeConverter
    fun jsonToBoolean(value: String) = Gson().fromJson(value, Boolean::class.java)

    /**
     * Converts list of articles to JSON string.
     */
    @TypeConverter
    fun articlesToJson(value: List<ArticleEntity>) = Gson().toJson(value)

    /**
     * Converts JSON string to list of articles.
     */
    @TypeConverter
    fun jsonToArticles(value: String) =
        Gson().fromJson(value, Array<ArticleEntity>::class.java).toList()

    /**
     * Converts list of courses to JSON string.
     */
    @TypeConverter
    fun coursesToJson(value: List<CourseEntity>) = Gson().toJson(value)

    /**
     * Converts list of profile entities to JSON string.
     */
    @TypeConverter
    fun quizToJson(value: List<QuizEntity>) = Gson().toJson(value)

    /**
     * Converts JSON string to quiz entity.
     */
    @TypeConverter
    fun jsonToQuiz(value: String) = run {
        val quizEntity = Gson().fromJson(value, QuizEntity::class.java)
        val map = quizEntity.quizMetaData?.toMap()
    }

    /**
     * Converts JSON string to course.
     */
    @TypeConverter
    fun jsonToCourse(value: String) = Gson().fromJson(value, CourseEntity::class.java)
}