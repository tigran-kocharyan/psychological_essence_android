package ru.hse.pe.domain.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class BookEntity(
    @SerializedName("id") val id: Int? = 0,
    @SerializedName("author") val author: String = "",
    @SerializedName("title") val title: String = "",
    @SerializedName("category") val category: String = "",
    @SerializedName("country") val country: String = "",
    @SerializedName("type") val type: String = "",
    @SerializedName("content") val content: String = "",
    @SerializedName("image_urls") val imageUrls: List<String> = emptyList(),
    @SerializedName("needs_subscription") val requiresSubscription: Boolean = false
) : Parcelable, ContentEntity
