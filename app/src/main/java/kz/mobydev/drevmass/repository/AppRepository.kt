package kz.mobydev.drevmass.repository

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

interface AppRepository {
    suspend fun getRegisterApi(name: String, email: String, password: String, password_confirmation: String): ResultData<User>
    suspend fun getLoginApi(email: String, password: String): ResultData<User>
    suspend fun getLogoutApi(token: String): ResultData<Logout>
    suspend fun getUserInformationApi(token: String): ResultData<UserInfoGet>
    suspend fun updateUserInformationApi(token: String,userInfoPostRequest: UserInfoPostRequest):ResultData<UserInfoPostResponse>

    suspend fun getLessonsApi(token: String): ResultData<List<Lesson>>
    suspend fun getLessonsDb(): ResultData<List<Lesson>>

    suspend fun getLessonsAnywhere(token: String):ResultData<List<Lesson>>
    suspend fun getLessonByIdApi(token: String, id: Int): ResultData<Lesson>
    suspend fun getLessonByIdDb( id: Int): ResultData<Lesson>

    suspend fun getProducts(token:String):ResultData<Products>
    suspend fun getProductsById(token: String,id: Int):ResultData<Products.ProductsItem>

    suspend fun actionFavoriteAPI(token: String, id: Int, action: String): ResultData<StatusModel>
    suspend fun getFavoriteAPI(token: String): ResultData<List<Lesson>>

    suspend fun supportMessage(token: String,message:String):ResultData<Support>
    suspend fun setDay(token:String,day:DaysPostRequest):ResultData<DayPostResponse>
    suspend fun forgetPassword(email: String): ResultData<MessageResetPassword>
}