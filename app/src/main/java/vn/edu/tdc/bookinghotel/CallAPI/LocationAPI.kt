package vn.edu.tdc.bookinghotel.CallAPI

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import vn.edu.tdc.bookinghotel.Model.Location
import vn.edu.tdc.bookinghotel.Model.LoginResponse
import vn.edu.tdc.bookinghotel.Model.UserLogin

interface LocationAPI {
    // Dinh nghia API de Retrofit lay du lieu ve tu Webservice
    companion object {
        const val BASE_URL = "https://hotel-manager-production-b051.up.railway.app/api/"
    }
    @GET("location")
    fun getLocations(): Call<List<Location>>
}