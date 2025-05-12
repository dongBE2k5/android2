package vn.edu.tdc.bookinghotel.CallAPI

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import vn.edu.tdc.bookinghotel.Model.LoginResponse
import vn.edu.tdc.bookinghotel.Model.UserLogin

interface LoginAPI {
    // Dinh nghia API de Retrofit lay du lieu ve tu Webservice
    companion object {
        const val BASE_URL = "https://hotel-manager-production-b051.up.railway.app/api/"
    }
    @POST("login")
    fun login(@Body request: UserLogin): Call<LoginResponse>
}