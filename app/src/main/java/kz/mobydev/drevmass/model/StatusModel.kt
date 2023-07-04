package kz.mobydev.drevmass.model


import com.google.gson.annotations.SerializedName

data class StatusModel(
    @SerializedName("status")
    val status: String // success
)