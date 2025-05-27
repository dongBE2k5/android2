package vn.edu.tdc.bookinghotel.CallAPI

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query
import vn.edu.tdc.bookinghotel.Model.BookingPriceUpdateRequest
import vn.edu.tdc.bookinghotel.Model.BookingRequest
import vn.edu.tdc.bookinghotel.Model.BookingStatusUpdateRequest
import vn.edu.tdc.bookinghotel.Response.BookingListResponse
import vn.edu.tdc.bookinghotel.Response.BookingResponse
import vn.edu.tdc.bookinghotel.Response.CustomerResponse
import vn.edu.tdc.bookinghotel.Response.HotelResponse
import vn.edu.tdc.bookinghotel.Response.LocationResponse

interface BookingAPI {
    // Dinh nghia API de Retrofit lay du lieu ve tu Webservice
    companion object {
        //        const val BASE_URL = "https://hotel-manager-production-b051.up.railway.app/api/"
//        const val BASE_URL = "http://192.168.1.4:8080/api/"
            const val BASE_URL = ApiConfig.BASE_URL
    }

    @GET("bookings/{bookingId}")
    fun getBookingById(@Path("bookingId") bookingId: Long): Call<BookingResponse>

    @POST("bookings/booking-new")
    fun createBooking(@Body booking: BookingRequest): Call<BookingResponse>

    @GET("bookings/customerId/{customerId}")
    fun getBookingByCustomerId(@Path("customerId") customerId: Long): Call<BookingListResponse>

    @GET("bookings/hotelier")
    fun getBookingByRoom(@Header("Authorization") token: String): Call<BookingListResponse>

    @PUT("bookings/{id}/status")
    fun updateBookingStatus(
        @Path("id") bookingId: Long,
        @Query("action") action: String
    ): Call<Void>

    @PUT("bookings/{id}/price")
    fun updateBookingPrice(
        @Path("id") bookingId: Long,
        @Body priceUpdateRequest: BookingPriceUpdateRequest
    ): Call<Void>


}