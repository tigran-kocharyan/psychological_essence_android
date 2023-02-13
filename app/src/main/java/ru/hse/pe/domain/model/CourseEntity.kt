package ru.hse.pe.domain.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
data class CourseEntity (
    @SerializedName("id") val id: Int? = 0,
    @SerializedName("name") val name: String? = "",
    @SerializedName("description") val description: String? = "",
    @SerializedName("duration") val duration: String? = "",
    @SerializedName("image_url") val imageUrl: String? = "",
    @SerializedName("lessons") val lessons: @RawValue List<LessonEntity> = emptyList(),
    @SerializedName("views") val views: Int? = 0,
    @SerializedName("likes") val likes: Int? = 0,
    @SerializedName("needs_subscription") val requiresSubscription: Boolean = false
    ) : Parcelable, ContentEntity
