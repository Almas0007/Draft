package kz.mobydev.drevmass.api

import kz.mobydev.drevmass.model.Lesson
import kz.mobydev.drevmass.model.Logout
import kz.mobydev.drevmass.model.User
import kz.mobydev.drevmass.model.UserInfoGet
import kz.mobydev.drevmass.model.UserInfoPostRequest
import kz.mobydev.drevmass.model.UserInfoPostResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    //user
    @POST(REGISTER_POST)
    suspend fun register(
        @Query("name") name: String,
        @Query("email") email: String,
        @Query("password") password: String,
        @Query("password_confirmation") password_confirmation: String
    ): User

    @POST(LOGIN_POST)
    suspend fun login(
        @Query("email") email: String,
        @Query("password") password: String
    ): User

//TODO: Надо будет узнать у бэк запросов на сброс пароля
//    @GET(RESET_PASSWORD)
//    suspend fun resetPassword(
//        @Query("email") email: String
//    ): User

    @POST(USER_POST)
    suspend fun updateUserInfo(
        @Header("Authorization") token: String,
        @Body userInfoPost: UserInfoPostRequest
    ): UserInfoPostResponse

    @GET(LOGOUT_GET)
    suspend fun logout(
        @Header("Authorization") token: String
    ): Logout

    @GET(USER_INFORMATION_GET)
    suspend fun getUserInformation(
        @Header("Authorization") token: String
    ): UserInfoGet

    //contents
    @GET(LESSONS_GET)
    suspend fun getLessons(
        @Header("Authorization") token: String
    ): List<Lesson>

    @GET("$LESSONS_GET/{id}")
    suspend fun getLessonById(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Lesson


}