package vn.edu.tdc.bookinghotel.CallAPI

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import vn.edu.tdc.bookinghotel.Response.HotelResponse
import vn.edu.tdc.bookinghotel.Response.RoomRespose

interface RoomAPI {
    companion object {
        //        const val BASE_URL = "https://hotel-manager-production-b051.up.railway.app/api/"
        const val BASE_URL = "http://192.168.1.4:8080/api/"
    }

    @GET("rooms")
    fun getRooms(): Call<RoomRespose>

    @GET("rooms/hotel/{hotelId}")
    fun getRoomByHotel(@Path("hotelId") hotelId: Long): Call<RoomRespose>
}