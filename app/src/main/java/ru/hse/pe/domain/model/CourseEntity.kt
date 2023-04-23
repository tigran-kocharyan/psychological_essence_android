package ru.hse.pe.domain.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * Entity for course
 */
@Parcelize
data class CourseEntity(
    @SerializedName("id") val id: Int? = 0,
    @SerializedName("name") val name: String? = "",
    @SerializedName("description") val description: String? = "",
    @SerializedName("duration") val duration: String? = "",
    @SerializedName("image_url") val imageUrl: String? = "",
    @SerializedName("lessons_count") val lessonsCount: Int? = 0,
    @SerializedName("lessons_ids") val lessonsIds: List<Int>? = emptyList(),
    @SerializedName("lessons_names") val lessonsNames: List<String>? = emptyList(),
    @SerializedName("views") val views: Int? = 0,
    @SerializedName("likes") val likes: Int? = 0,
    @SerializedName("needs_subscription") val requiresSubscription: Boolean = false
) : Parcelable, ContentEntity
