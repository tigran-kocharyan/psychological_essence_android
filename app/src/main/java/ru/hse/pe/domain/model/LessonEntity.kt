package ru.hse.pe.domain.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * Entity for lesson
 */
@Parcelize
data class LessonEntity(
    @SerializedName("id") val id: Int? = 0,
    @SerializedName("name") val name: String? = "",
    @SerializedName("content") val content: String? = "",
    @SerializedName("description") val description: String? = "",
    @SerializedName("image_url") val imageUrl: String? = "",
    @SerializedName("quiz") val quiz: QuizEntity? = null,
) : Parcelable, ContentEntity
