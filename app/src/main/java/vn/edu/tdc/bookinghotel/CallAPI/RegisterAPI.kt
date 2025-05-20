package vn.edu.tdc.bookinghotel.CallAPI

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import vn.edu.tdc.bookinghotel.Model.RegisterResponse
import vn.edu.tdc.bookinghotel.Model.UserRegister
interface RegisterAPI {
    // Dinh nghia API de Retrofit lay du lieu ve tu Webservice
    companion object {
//        const val BASE_URL = "https://hotel-manager-production-b051.up.railway.app/api/"
        const val BASE_URL = ApiConfig.BASE_URL
    }
    @POST("register")
    fun register(@Body request: UserRegister): Call<RegisterResponse>
}