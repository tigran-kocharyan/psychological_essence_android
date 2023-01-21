package ru.hse.pe.domain.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RecommendationEntity(
    @SerializedName("id") val id: Int? = 0,
    @SerializedName("author") val author: String = "",
    @SerializedName("title") val title: String = "",
    @SerializedName("category_ru") val category: String = "",
    @SerializedName("year") val year: String = "",
    @SerializedName("country") val country: String = "",
    @SerializedName("duration") val duration: String = "",
    @SerializedName("episodes") val episodes: String = "",
    @SerializedName("genre") val genre: String = "",
    @SerializedName("content") val content: String = "",
    @SerializedName("extra_content") val extraContent: String = "",
    @SerializedName("image_urls") val imageUrls: List<String> = emptyList(),
    @SerializedName("needs_subscription") val requiresSubscription: Boolean = false
) : Parcelable, ContentEntity
