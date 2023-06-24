package kz.mobydev.drevmass.data.preferences

import android.content.Context
import android.content.SharedPreferences
import android.util.Log

class PreferencesDataSourceImpl(
    private val context: Context
):PreferencesDataSource {
    companion object{
        private const val APP_TOKEN = "token"
        private const val APP_PASSWORD = "password"
        private const val APP_EMAIL = "email"
        private const val APP_NAME = "name"
    }
    private lateinit var shPr: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    init {
        shPr = context.getSharedPreferences("PreferencesImpl", Context.MODE_PRIVATE)
        editor = shPr.edit()
    }

    override fun setToken(token: String) {
        editor.putString(APP_TOKEN, token)
        Log.d("AAA", "setToken: $token")
        editor.apply()
    }

    override fun getToken(): String {
        return shPr.getString(APP_TOKEN, "") ?: ""
    }

    override fun setPassword(password: String) {
        editor.putString(APP_PASSWORD, password)
        Log.d("AAA", "Password: $password")
        editor.apply()
    }

    override fun getPassword(): String {
        return shPr.getString(APP_PASSWORD, "") ?: ""
    }

    override fun getEmail(): String {
        return shPr.getString(APP_EMAIL, "") ?: ""
    }

    override fun setEmail(email: String) {
        editor.putString(APP_EMAIL, email)
        Log.d("AAA", "Email: $email")
        editor.apply()
    }

    override fun getName(): String {
        return shPr.getString(APP_NAME, "") ?: ""
    }

    override fun setName(names: String) {
        editor.putString(APP_NAME, names)
        Log.d("AAA", "Name: $names")
        editor.apply()
    }

}