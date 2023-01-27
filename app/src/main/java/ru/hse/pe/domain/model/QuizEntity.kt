package ru.hse.pe.domain.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class QuizEntity(
    @SerializedName("id") val id: Int? = 0,
    @SerializedName("category") val category: String? = "",
    @SerializedName("name") val name: String? = "",
    @SerializedName("description") val description: String? = "",
    @SerializedName("time") val time: String? = "",
    @SerializedName("questions") val questions: List<String> = emptyList(),
    @SerializedName("answers") val answers: List<String> = emptyList(),
    @SerializedName("views") val views: Int? = 0,
    @SerializedName("likes") val likes: Int? = 0,
    @SerializedName("needs_subscription") val requiresSubscription: Boolean = false
) : Parcelable, ContentEntity

@Parcelize
data class QuizResultEntity(
    @SerializedName("test_id") val test_id: Int? = 0,
    @SerializedName("user_id") val user_id: Int? = 0,
    @SerializedName("markdown") val markdown: String? = ""
) : Parcelable