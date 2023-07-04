package kz.mobydev.drevmass.model


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class UserInfoPostRequest(
    @SerializedName("email")
    val email: String, // qw@qssjjsjs.q
    @SerializedName("information")
    val information: Information,
    @SerializedName("name")
    val name: String, // ttttttttttt
    @SerializedName("password")
    val password: String, // qwerty23
    @SerializedName("password_confirmation")
    val passwordConfirmation: String // qwerty23
)  {
    data class Information(
        @SerializedName("activity")
        val activity: Int, // 2
        @SerializedName("birth")
        val birth: String, // 2003-01-01
        @SerializedName("gender")
        val gender: Int, // true
        @SerializedName("height")
        val height: Int, // 10
        @SerializedName("weight")
        val weight: Double // 72.9
    )
}