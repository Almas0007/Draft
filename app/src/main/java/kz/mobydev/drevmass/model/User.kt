package kz.mobydev.drevmass.model


import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("access_token")
    val accessToken: String, // 12|dPvhQy9AwS9rP496APfkrZlqWKOA6eJ4yU8O6oM3
    @SerializedName("token_type")
    val tokenType: String, // Bearer
    @SerializedName("user")
    val user: User
) {
    data class User(
        @SerializedName("email")
        val email: String, // q@q.q
        @SerializedName("id")
        val id: Int, // 4
        @SerializedName("name")
        val name: String // q
    )
}