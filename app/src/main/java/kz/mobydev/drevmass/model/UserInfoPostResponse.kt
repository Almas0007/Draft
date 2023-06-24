package kz.mobydev.drevmass.model


import com.google.gson.annotations.SerializedName

data class UserInfoPostResponse(
    @SerializedName("email")
    val email: String, // amantay.or@gmail.com
    @SerializedName("id")
    val id: Int, // 1
    @SerializedName("information")
    val information: Information,
    @SerializedName("name")
    val name: String // Amantay
) {
    data class Information(
        @SerializedName("activity")
        val activity: Int, // 3
        @SerializedName("birth")
        val birth: String, // 1999-08-21
        @SerializedName("gender")
        val gender: Int, // 0
        @SerializedName("height")
        val height: String, // 181
        @SerializedName("id")
        val id: Int, // 1
        @SerializedName("user_id")
        val userId: Int, // 1
        @SerializedName("weight")
        val weight: String // 75.5
    )
}