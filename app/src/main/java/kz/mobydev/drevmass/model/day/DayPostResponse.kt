package kz.mobydev.drevmass.model.day


import com.google.gson.annotations.SerializedName

data class DayPostResponse(
    @SerializedName("friday")
    val friday: String, // 0
    @SerializedName("id")
    val id: Int, // 3
    @SerializedName("monday")
    val monday: String, // 1
    @SerializedName("saturday")
    val saturday: String, // 1
    @SerializedName("sunday")
    val sunday: String, // 0
    @SerializedName("thursday")
    val thursday: String, // 1
    @SerializedName("time")
    val time: String, // 20:01
    @SerializedName("tuesday")
    val tuesday: String, // 1
    @SerializedName("user_id")
    val userId: Int, // 34
    @SerializedName("wednesday")
    val wednesday: String // 1
)