package ru.hse.pe.domain.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * Entity for user credentials
 */
@Parcelize
data class UserEntity(
    @SerializedName("uid") val uid: String = "",
    @SerializedName("name") val name: String = "",
    @SerializedName("sex") val sex: String = "",
    @SerializedName("email") val email: String = "",
    @SerializedName("password") val password: String = "",
    @Transient @SerializedName("is_subscribed") val isSubscribed: Boolean = false,
) : Parcelable
