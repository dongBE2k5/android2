package vn.edu.tdc.bookinghotel.CallAPI

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import vn.edu.tdc.bookinghotel.Model.RegisterResponse
import vn.edu.tdc.bookinghotel.Model.UserRegister
interface RegisterAPI {
    // Dinh nghia API de Retrofit lay du lieu ve tu Webservice
    companion object {
        const val BASE_URL = "https://hotel-manager-production-2cc4.up.railway.app/api/"
    }
    @POST("register")
    fun register(@Body request: UserRegister): Call<RegisterResponse>
}