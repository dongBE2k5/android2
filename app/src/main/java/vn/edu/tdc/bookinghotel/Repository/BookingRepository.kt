package vn.edu.tdc.bookinghotel.Repository

import android.content.Context
import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import vn.edu.tdc.bookinghotel.CallAPI.BookingAPI
import vn.edu.tdc.bookinghotel.CallAPI.CustomerAPI
import vn.edu.tdc.bookinghotel.CallAPI.LocationAPI
import vn.edu.tdc.bookinghotel.Model.Booking
import vn.edu.tdc.bookinghotel.Model.BookingRequest
import vn.edu.tdc.bookinghotel.Model.BookingStatusUpdateRequest
import vn.edu.tdc.bookinghotel.Model.Hotel
import vn.edu.tdc.bookinghotel.Model.Customer
import vn.edu.tdc.bookinghotel.Response.BookingListResponse
import vn.edu.tdc.bookinghotel.Response.BookingResponse
import vn.edu.tdc.bookinghotel.Response.CustomerResponse
import vn.edu.tdc.bookinghotel.Response.HotelResponse
import vn.edu.tdc.bookinghotel.Session.SessionManager


class BookingRepository (){

    private val retrofit = Retrofit.Builder()
        .baseUrl(BookingAPI.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val bookingAPI = retrofit.create(BookingAPI::class.java)

    fun createBooking(
        booking: BookingRequest,
        onSuccess: (Booking) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        val call = bookingAPI.createBooking(booking)
        call.enqueue(object : Callback<BookingResponse> {
            override fun onResponse(call: Call<BookingResponse>, response: Response<BookingResponse>) {
                Log.d("bookingRepo", "${booking.toString()}")
                Log.d("bookingRepo", "${response}")

                if (response.isSuccessful) {
                    response.body()?.let {
                        Log.d("booking", "${it.body}")

                        onSuccess(it.body)
                    } ?: onError(Exception("Response null"))
                } else {
                    onError(Exception("Error code: ${response.code()}"))
                }
            }

            override fun onFailure(call: Call<BookingResponse>, t: Throwable) {
                onError(t)
            }
        })
    }

    fun getBookingById(
        bookingId: Long,
        onSuccess: (Booking) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        val call = bookingAPI.getBookingById(bookingId)
        call.enqueue(object : Callback<BookingResponse> {
            override fun onResponse(call: Call<BookingResponse>, response: Response<BookingResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        Log.d("booking", "${it.body}")
                        onSuccess(it.body)
                    } ?: onError(Exception("Response null"))
                } else {
                    onError(Exception("Error code: ${response.code()}"))
                }
            }

            override fun onFailure(call: Call<BookingResponse>, t: Throwable) {
                onError(t)
            }
        })
    }


    fun getBookingByCustomerId(
        customerId: Long,
        onSuccess: (List<Booking>) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        val call = bookingAPI.getBookingByCustomerId(customerId)
        call.enqueue(object : Callback<BookingListResponse> {
            override fun onResponse(call: Call<BookingListResponse>, response: Response<BookingListResponse>) {
                Log.d("List", "${response}")
                if (response.isSuccessful) {
                    response.body()?.let {
                        onSuccess(it.body)
                    } ?: onError(Exception("Response null"))
                } else {
                    onError(Exception("Error code: ${response.code()}"))
                }
            }

            override fun onFailure(call: Call<BookingListResponse>, t: Throwable) {
                onError(t)
            }
        })
    }

    fun cancelBooking(
        bookingId: Long,
        onSuccess: () -> Unit,
        onError: (Throwable) -> Unit
    ) {
        val call = bookingAPI.updateBookingStatus(bookingId, "cancel") // Gửi ?action=cancel
        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    onSuccess()
                } else {
                    onError(Exception("Lỗi server: ${response.code()}"))
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                onError(t)
            }
        })
    }
    fun getBookingByHotelier(
        token: String,
        onSuccess: (List<Booking>) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        val call = bookingAPI.getBookingByRoom("Bearer $token")
        call.enqueue(object : Callback<BookingListResponse> {
            override fun onResponse(call: Call<BookingListResponse>, response: Response<BookingListResponse>) {
                Log.d("List", "${response}")
                if (response.isSuccessful) {
                    response.body()?.let {
                        onSuccess(it.body)
                    } ?: onError(Exception("Response null"))
                } else {
                    onError(Exception("Error code: ${response.code()}"))
                }
            }

            override fun onFailure(call: Call<BookingListResponse>, t: Throwable) {
                onError(t)
            }
        })
    }



}