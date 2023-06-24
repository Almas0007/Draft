package kz.mobydev.drevmass.data.remote

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kz.mobydev.drevmass.api.ApiService
import kz.mobydev.drevmass.di.IoDispatcher
import kz.mobydev.drevmass.model.Lesson
import kz.mobydev.drevmass.model.Logout
import kz.mobydev.drevmass.model.User
import kz.mobydev.drevmass.model.UserInfoGet
import kz.mobydev.drevmass.model.UserInfoPostRequest
import kz.mobydev.drevmass.model.UserInfoPostResponse
import kz.mobydev.drevmass.model.common.ResultData

class RemoteDataSourceImpl(
    private val api: ApiService,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : RemoteDataSource {
    override suspend fun getRegister(
        name: String,
        email: String,
        password: String,
        password_confirmation: String
    ): ResultData<User> =
        withContext(ioDispatcher) {
            val request =
                api.register(name, email, password, password_confirmation)
            ResultData.Success(request)
        }

    override suspend fun getLogin(email: String, password: String): ResultData<User> =
        withContext(ioDispatcher) {
            val request = api.login(email, password)
            ResultData.Success(request)
        }

    override suspend fun getLogout(token: String): ResultData<Logout> = withContext(ioDispatcher) {
        val request = api.logout("Bearer $token")
        ResultData.Success(request)
    }

    override suspend fun getUserInformation(token: String): ResultData<UserInfoGet> =
        withContext(ioDispatcher) {
            val request = api.getUserInformation("Bearer $token")
            ResultData.Success(request)
        }

    override suspend fun updateUserInfo(
        token: String,
        userInfoPostRequest: UserInfoPostRequest
    ): ResultData<UserInfoPostResponse> {
        return withContext(ioDispatcher) {
            val request = api.updateUserInfo("Bearer $token",userInfoPostRequest )
            ResultData.Success(request)
        }
    }

    override suspend fun getLessons(token: String): ResultData<List<Lesson>> =
        withContext(ioDispatcher) {
            val request = api.getLessons("Bearer $token")
            ResultData.Success(request)
        }

    override suspend fun getLessonById(token: String, id: Int): ResultData<Lesson> =
        withContext(ioDispatcher) {
            val request = api.getLessonById("Bearer $token", id)
            ResultData.Success(request)
        }

}