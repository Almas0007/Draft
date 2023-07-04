package kz.mobydev.drevmass.model


import com.google.gson.annotations.SerializedName

data class MessageResetPassword(
    @SerializedName("email")
    val email: String // Please wait before retrying.
)