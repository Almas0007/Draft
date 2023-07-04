package kz.mobydev.drevmass.model


import com.google.gson.annotations.SerializedName

data class Support(
    @SerializedName("id")
    val id: Int, // 1
    @SerializedName("problem_description")
    val problemDescription: String, // DESCRIPTION
    @SerializedName("user_id")
    val userId: Int // 33
)