package kz.mobydev.drevmass.model


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class UserInfoGet(
    @SerializedName("day")
    val day: Any?, // null
    @SerializedName("email")
    val email: String, // qa@q.q
    @SerializedName("id")
    val id: Int, // 4
    @SerializedName("information")
    val information: Information,
    @SerializedName("name")
    val name: String // qazaq
): Serializable {
    data class Information(
        @SerializedName("activity")
        val activity: Int, // 2
        @SerializedName("birth")
        val birth: String, // 2003-03-14
        @SerializedName("gender")
        val gender: Int, // 1
        @SerializedName("height")
        val height: String, // 190
        @SerializedName("id")
        val id: Int, // 2
        @SerializedName("user_id")
        val userId: Int, // 4
        @SerializedName("weight")
        val weight: String // 72.9
    ): Serializable
}