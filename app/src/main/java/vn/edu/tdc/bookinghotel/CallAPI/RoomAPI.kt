package vn.edu.tdc.bookinghotel.CallAPI

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import vn.edu.tdc.bookinghotel.Model.RoomType
import vn.edu.tdc.bookinghotel.Response.RoomResponse
import vn.edu.tdc.bookinghotel.Response.RoomSingleResponse


interface RoomAPI {
    companion object {
        //        const val BASE_URL = "https://hotel-manager-production-b051.up.railway.app/api/"
//        const val BASE_URL = "http://192.168.1.4:8080/api/"
            const val BASE_URL = ApiConfig.BASE_URL
    }

    @GET("rooms")
    fun getRooms(): Call<RoomResponse>

    @GET("rooms/{roomId}")
    fun getRoomsById(@Path("roomId") roomId: Long): Call<RoomSingleResponse>

    @GET("rooms/hotel/{hotelId}")
    fun getRoomByHotel(@Path("hotelId") hotelId: Long): Call<RoomResponse>

    @GET("room-types")
    fun getRoomTypes(): Call<List<RoomType>>



    @Multipart
    @POST("rooms")
    fun addRoom(
        @Part("roomNumber") roomNumber: RequestBody,
        @Part("roomTypeId") roomTypeId: RequestBody,
        @Part("price") price: RequestBody,
        @Part("capacity") capacity: RequestBody,
        @Part("description") description: RequestBody,
        @Part("hotelId") hotelId: RequestBody,
        @Header("Authorization") token: String,
        @Part image: MultipartBody.Part? = null
    ): Call<Void>



    @Multipart
    @PUT("rooms/{roomId}")
    fun updateRoom(
        @Path("roomId") roomId: Long,
        @Part("roomNumber") roomNumber: RequestBody,
        @Part("roomTypeId") roomTypeId: RequestBody,
        @Part("price") price: RequestBody,
        @Part("capacity") capacity: RequestBody,
        @Part("description") description: RequestBody,
        @Part("status") status: RequestBody,
        @Part("hotelId") hotelId: RequestBody,
        @Header("Authorization") token: String,
        @Part image: MultipartBody.Part? = null
    ): Call<Void>

}