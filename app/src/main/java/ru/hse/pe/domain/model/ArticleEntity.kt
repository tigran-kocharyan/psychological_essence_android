package ru.hse.pe.domain.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ArticleEntity(
    @SerializedName("id") val id: Int? = 0,
    @SerializedName("author") val author: String = "",
    @SerializedName("title") val title: String = "",
    @SerializedName("category") val category: String = "",
    @SerializedName("content") val content: String = "",
    @SerializedName("needs_subscription") val requiresSubscription: Boolean = false,
    @SerializedName("date") val date: String = "",
    @SerializedName("reading_time") val time: String = "",
    @SerializedName("article_url") val articleUrl: String = "",
    @SerializedName("image_url") val imageUrl: String = "",
    @SerializedName("views") val views: Int? = 0,
    @SerializedName("likes") val likes: Int? = 0
): Parcelable
