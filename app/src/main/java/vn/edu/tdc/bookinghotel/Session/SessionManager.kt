package vn.edu.tdc.bookinghotel.Session

import android.content.Context

class SessionManager(private val context: Context) {
    private val prefs = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
    fun saveIdUser(idUser: String) {
        prefs.edit().putString("idUser", idUser).apply()
    }
    fun saveIdCustomer(idCustomer: String) {
        prefs.edit().putString("idCustomer", idCustomer).apply()
    }
    fun saveUserName(username: String) {
        prefs.edit().putString("username", username).apply()
    }
    fun saveToken(token: String) {
        prefs.edit().putString("token", token).apply()
    }

    fun getToken(): String? = prefs.getString("token", null)
    fun getIdUser(): String? = prefs.getString("idUser", null)
    fun getIdCustomer(): String? = prefs.getString("idCustomer", null)
    fun getUserName(): String? = prefs.getString("username", null)

    fun isLoggedIn(): Boolean = getToken() != null

    fun logout() {
        prefs.edit().clear().apply()
    }
}