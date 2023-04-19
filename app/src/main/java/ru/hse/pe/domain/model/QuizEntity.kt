package ru.hse.pe.domain.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

/**
 * Entity for quiz
 */
@Parcelize
data class QuizEntity(
    @SerializedName("id") val id: Int? = 0,
    @SerializedName("category") val category: String? = "",
    @SerializedName("name") val name: String? = "",
    @SerializedName("description") val description: String? = "",
    @SerializedName("time") val time: String? = "",
    @SerializedName("questions") val questions: List<String> = emptyList(),
    @SerializedName("questions_metadata") val quizMetaData: Map<Int, QuizMetaDataEntity> ?= mapOf(),
    @SerializedName("answers") val answers: List<List<String>> = emptyList(),
    @SerializedName("views") val views: Int? = 0,
    @SerializedName("likes") val likes: Int? = 0,
    @SerializedName("image_url") val imageUrl: String? = "",
    @SerializedName("needs_subscription") val requiresSubscription: Boolean = false
) : Parcelable, ContentEntity

/**
 * Entity for quiz answer
 */
@Parcelize
data class QuizMetaDataEntity(
    val categories: List<String> ?= emptyList(),
    val multiple_answers: Boolean ?= false
): Parcelable, ContentEntity

@Parcelize
data class QuizAnswerEntity(
    @SerializedName("quiz_id") val quiz_id: Int? = 0,
    @SerializedName("user_uid") val user_uid: String? = "",
    @SerializedName("answers") val answers: @RawValue List<String>? = arrayListOf()
) : Parcelable, ContentEntity

/**
 * Entity for quiz result
 */
@Parcelize
data class QuizResultEntity(
    @SerializedName("images") val images: List<String>? = arrayListOf(),
    @SerializedName("content") val content: String? = ""
) : Parcelable, ContentEntity