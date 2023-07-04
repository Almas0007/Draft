package kz.mobydev.drevmass.data.remote

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
import kz.mobydev.drevmass.model.common.ResultData
import kz.mobydev.drevmass.model.day.DayPostResponse
import kz.mobydev.drevmass.model.day.DaysPostRequest

interface RemoteDataSource {
    suspend fun getRegister(name: String, email: String, password: String, password_confirmation: String): ResultData<User>
    suspend fun getLogin(email: String, password: String): ResultData<User>
    suspend fun getLogout(token: String): ResultData<Logout>
    suspend fun getUserInformation(token: String): ResultData<UserInfoGet>
    suspend fun updateUserInfo(token: String, userInfoPostRequest: UserInfoPostRequest): ResultData<UserInfoPostResponse>

    suspend fun getLessons(token: String): ResultData<List<Lesson>>
    suspend fun getLessonById(token: String, id: Int): ResultData<Lesson>

    suspend fun getProduct(token: String): ResultData<Products>
    suspend fun getProductById(token: String,id: Int):ResultData<Products.ProductsItem>

    suspend fun actionFavorite(token: String, id: Int, action: String): ResultData<StatusModel>
    suspend fun getFavorite(token: String): ResultData<List<Lesson>>

    suspend fun supportMessage(token: String, message: String): ResultData<Support>
    suspend fun setDay(token:String, day:DaysPostRequest): ResultData<DayPostResponse>

    suspend fun forgetPassword(email: String): ResultData<MessageResetPassword>
}