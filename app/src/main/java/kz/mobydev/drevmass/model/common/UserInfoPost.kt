package kz.mobydev.drevmass.model.common


import com.google.gson.annotations.SerializedName

data class UserInfoPost(
    @SerializedName("email")
    val email: String, // qa@q.q
    @SerializedName("information")
    val information: Information,
    @SerializedName("name")
    val name: String, // qazaq
    @SerializedName("password")
    val password: String, // qwerty23
    @SerializedName("password_confirmation")
    val passwordConfirmation: String // qwerty23
) {
    data class Information(
        @SerializedName("activity")
        val activity: Int, // 2
        @SerializedName("birth")
        val birth: String, // 2003-03-14
        @SerializedName("gender")
        val gender: Boolean, // true
        @SerializedName("height")
        val height: Int, // 190
        @SerializedName("weight")
        val weight: Double // 72.9
    )
}