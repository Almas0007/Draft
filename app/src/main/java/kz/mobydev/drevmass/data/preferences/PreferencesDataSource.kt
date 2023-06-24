package kz.mobydev.drevmass.data.preferences

interface PreferencesDataSource {
    fun setToken(token: String)
    fun getToken(): String

    fun setPassword(password: String)
    fun getPassword(): String

    fun getEmail(): String
    fun setEmail(email: String)

    fun getName(): String
    fun setName(names: String)
}