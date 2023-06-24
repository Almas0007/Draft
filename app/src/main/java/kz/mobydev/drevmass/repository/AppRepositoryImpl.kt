package kz.mobydev.drevmass.repository

import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kz.mobydev.drevmass.data.local.AppDao
import kz.mobydev.drevmass.data.remote.RemoteDataSource
import kz.mobydev.drevmass.di.IoDispatcher
import kz.mobydev.drevmass.model.Lesson
import kz.mobydev.drevmass.model.Logout
import kz.mobydev.drevmass.model.User
import kz.mobydev.drevmass.model.UserInfoGet
import kz.mobydev.drevmass.model.UserInfoPostRequest
import kz.mobydev.drevmass.model.UserInfoPostResponse
import kz.mobydev.drevmass.model.common.ResultData
import kz.mobydev.drevmass.utils.InternetUtil
import kz.mobydev.drevmass.utils.RemoteDataNotFoundException

class AppRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val appDao: AppDao,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) :AppRepository{

    private val isInternetOn = InternetUtil.isInternetOn()

    override suspend fun getRegisterApi(
        name: String,
        email: String,
        password: String,
        password_confirmation: String
    ): ResultData<User> {
        return when (val result = remoteDataSource.getRegister(name, email, password, password_confirmation)) {
            is ResultData.Success -> {
                ResultData.Success(result.data)
            }
            is ResultData.Error -> {
                ResultData.Error(RemoteDataNotFoundException())
            }
        }
    }

    override suspend fun getLoginApi(email: String, password: String): ResultData<User> {
        return when (val result = remoteDataSource.getLogin(email, password)) {
            is ResultData.Success -> {
                ResultData.Success(result.data)
            }
            is ResultData.Error -> {
                ResultData.Error(RemoteDataNotFoundException())
            }
        }
    }

    override suspend fun getLogoutApi(token: String): ResultData<Logout> {
       return when (val result = remoteDataSource.getLogout(token)) {
            is ResultData.Success -> {
                ResultData.Success(result.data)
            }
            is ResultData.Error -> {
                ResultData.Error(RemoteDataNotFoundException())
            }
        }
    }

    override suspend fun getUserInformationApi(token: String): ResultData<UserInfoGet> {
        return when (val result = remoteDataSource.getUserInformation(token)) {
            is ResultData.Success -> {
                ResultData.Success(result.data)
            }
            is ResultData.Error -> {
                ResultData.Error(RemoteDataNotFoundException())
            }
        }
    }

    override suspend fun updateUserInformationApi(
        token: String,
        userInfoPostRequest: UserInfoPostRequest
    ): ResultData<UserInfoPostResponse> {
        return when (val result = remoteDataSource.updateUserInfo(token, userInfoPostRequest = userInfoPostRequest)) {
            is ResultData.Success -> {
                ResultData.Success(result.data)
            }
            is ResultData.Error -> {
                ResultData.Error(RemoteDataNotFoundException())
            }
        }
    }

    override suspend fun getLessonsApi(token: String): ResultData<List<Lesson>> {
        return when (val result = remoteDataSource.getLessons(token)) {
            is ResultData.Success -> {
                val response = result.data
                withContext(ioDispatcher) {appDao.setLessonList(response)}
                ResultData.Success(response)
            }
            is ResultData.Error -> {
                ResultData.Error(RemoteDataNotFoundException())
            }
        }
    }

    override suspend fun getLessonsDb(): ResultData<List<Lesson>> =
        withContext(ioDispatcher) {
            ResultData.Success(appDao.getLessonList())
    }

    override suspend fun getLessonByIdApi(token: String, id: Int): ResultData<Lesson> {
        return when (val result = remoteDataSource.getLessonById(token, id)) {
            is ResultData.Success -> {
                val response = result.data
                withContext(ioDispatcher) {appDao.setLesson(response)}
                ResultData.Success(response)
            }
            is ResultData.Error -> {
                ResultData.Error(RemoteDataNotFoundException())
            }
        }
    }

    override suspend fun getLessonByIdDb(id: Int): ResultData<Lesson> =
        withContext(ioDispatcher) {
            ResultData.Success(appDao.getLessonById(id))
    }



}