package kz.mobydev.drevmass.model


import com.google.gson.annotations.SerializedName

data class Logout(
    @SerializedName("message")
    val message: String // Logged out
)