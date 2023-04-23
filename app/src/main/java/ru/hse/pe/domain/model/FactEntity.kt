package ru.hse.pe.domain.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * Entity for fact
 */
@Parcelize
data class FactEntity(
    @SerializedName("id") val id: Int? = 0,
    @SerializedName("title") val title: String = "",
    @SerializedName("content") val content: String = "",
    @SerializedName("needs_subscription") val requiresSubscription: Boolean = false,
    @SerializedName("image_url") val imageUrl: String = ""
) : Parcelable, ContentEntity
