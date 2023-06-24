package kz.mobydev.drevmass.data.remote

import kz.mobydev.drevmass.model.Lesson
import kz.mobydev.drevmass.model.Logout
import kz.mobydev.drevmass.model.User
import kz.mobydev.drevmass.model.UserInfoGet
import kz.mobydev.drevmass.model.UserInfoPostRequest
import kz.mobydev.drevmass.model.UserInfoPostResponse
import kz.mobydev.drevmass.model.common.ResultData

interface RemoteDataSource {
    suspend fun getRegister(name: String, email: String, password: String, password_confirmation: String): ResultData<User>
    suspend fun getLogin(email: String, password: String): ResultData<User>
    suspend fun getLogout(token: String): ResultData<Logout>
    suspend fun getUserInformation(token: String): ResultData<UserInfoGet>
    suspend fun updateUserInfo(token: String, userInfoPostRequest: UserInfoPostRequest): ResultData<UserInfoPostResponse>

    suspend fun getLessons(token: String): ResultData<List<Lesson>>
    suspend fun getLessonById(token: String, id: Int): ResultData<Lesson>

}