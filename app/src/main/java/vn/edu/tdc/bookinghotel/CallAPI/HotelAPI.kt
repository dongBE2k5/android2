package vn.edu.tdc.bookinghotel.CallAPI

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import vn.edu.tdc.bookinghotel.Response.HotelResponse
import vn.edu.tdc.bookinghotel.Response.LocationResponse

interface HotelAPI {
    // Dinh nghia API de Retrofit lay du lieu ve tu Webservice
    companion object {
        //        const val BASE_URL = "https://hotel-manager-production-b051.up.railway.app/api/"
//        const val BASE_URL = "http://192.168.1.4:8080/api/"
            const val BASE_URL = "http://192.168.1.56:8080/api/"
    }

    @GET("hotels")
    fun getHotels(): Call<HotelResponse>

    @GET("hotels/location/{locationId}")
    fun getHotelsByLocation(@Path("locationId") locationId: Long): Call<HotelResponse>
}