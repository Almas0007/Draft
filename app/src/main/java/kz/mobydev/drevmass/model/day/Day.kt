package kz.mobydev.drevmass.model.day


import com.google.gson.annotations.SerializedName

data class Day(
    @SerializedName("friday")
    val friday: Int, // 0
    @SerializedName("id")
    val id: Int, // 4
    @SerializedName("monday")
    val monday: Int, // 1
    @SerializedName("saturday")
    val saturday: Int, // 1
    @SerializedName("sunday")
    val sunday: Int, // 0
    @SerializedName("thursday")
    val thursday: Int, // 1
    @SerializedName("time")
    val time: String, // 20:01:00
    @SerializedName("tuesday")
    val tuesday: Int, // 1
    @SerializedName("user_id")
    val userId: Int, // 36
    @SerializedName("wednesday")
    val wednesday: Int // 1
)