package ru.hse.pe.domain.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserEntity(
    @SerializedName("uid") val uid: String? = "",
    @SerializedName("name") val name: String = "",
    @SerializedName("sex") val sex: String = "",
    @SerializedName("email") val email: String = "",
    @SerializedName("isSubscribed") val isSubscribed: Boolean = false,
) : Parcelable