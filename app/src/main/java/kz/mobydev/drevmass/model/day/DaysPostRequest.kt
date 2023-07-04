package kz.mobydev.drevmass.model.day


import com.google.gson.annotations.SerializedName

data class DaysPostRequest(
    @SerializedName("friday")
    val friday: String, // 0
    @SerializedName("monday")
    val monday: String, // 1
    @SerializedName("saturday")
    val saturday: String, // 0
    @SerializedName("sunday")
    val sunday: String, // 0
    @SerializedName("thursday")
    val thursday: String, // 1
    @SerializedName("time")
    val time: String, // 20:01
    @SerializedName("tuesday")
    val tuesday: String, // 1
    @SerializedName("wednesday")
    val wednesday: String // 1
)