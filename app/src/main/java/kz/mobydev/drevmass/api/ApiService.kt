package kz.mobydev.drevmass.api

import kz.mobydev.drevmass.model.day.DaysPostRequest
import kz.mobydev.drevmass.model.Lesson
import kz.mobydev.drevmass.model.Logout
import kz.mobydev.drevmass.model.MessageResetPassword
import kz.mobydev.drevmass.model.Products
import kz.mobydev.drevmass.model.StatusModel
import kz.mobydev.drevmass.model.Support
import kz.mobydev.drevmass.model.User
import kz.mobydev.drevmass.model.UserInfoGet
import kz.mobydev.drevmass.model.UserInfoPostRequest
import kz.mobydev.drevmass.model.UserInfoPostResponse
import kz.mobydev.drevmass.model.day.DayPostResponse
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

    @GET(PRODUCTS_GET)
    suspend fun getProducts(
        @Header("Authorization") token: String
    ): Products

    @GET("$PRODUCTS_GET/{id}")
    suspend fun getProductById(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Products.ProductsItem

    @POST(FAVORITE)
    suspend fun actionFavorite(
        @Header("Authorization") token: String,
        @Query("lesson_id") lesson_id: Int,
        @Query("action") action: String
    ):StatusModel

    @GET(FAVORITE)
    suspend fun getFavorite(
        @Header("Authorization") token: String
    ):List<Lesson>

    @POST(SUPPORTS)
    suspend fun supportMessage(
        @Header("Authorization") token: String,
        @Query("problem_description") problem_description: String
    ):Support

    @POST(DAYS)
    suspend fun setDays(
        @Header("Authorization") token: String,
        @Body day: DaysPostRequest
    ):DayPostResponse

    @POST(RESET_PASSWORD)
    suspend fun forgetPassword(
        @Query("email") email: String
    ):MessageResetPassword

}