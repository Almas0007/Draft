package kz.mobydev.drevmass.api

const val IO_TIMEOUT = 30L
const val BASE_URL = "http://45.12.74.158/api/"
const val DEFAULT_USER_TOKEN = "10|FwxNGQCYB2PBQoNQM10AYKyoSrsVNIw9IMCxQkBW"

//REQUESTS
//USER
const val LOGIN_POST = "login"
const val REGISTER_POST = "register"
const val RESET_PASSWORD = "forget-password"
const val LOGOUT_GET = "logout"
const val USER_POST = "user" // ДЛЯ ЧЕГО ЭТОТ ЗАПРОС?
const val USER_INFORMATION_GET = "user/information"

//CONTENT
const val LESSONS_GET = "lessons"
