package vn.edu.tdc.bookinghotel.Repository

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import vn.edu.tdc.bookinghotel.CallAPI.HotelAPI
import vn.edu.tdc.bookinghotel.CallAPI.LocationAPI
import vn.edu.tdc.bookinghotel.Model.Hotel

import vn.edu.tdc.bookinghotel.Response.HotelResponse


class HotelRepository {

    private val retrofit = Retrofit.Builder()
        .baseUrl(HotelAPI.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val hotelAPI = retrofit.create(HotelAPI::class.java)

    fun fetchHotel(
        onSuccess: (List<Hotel>) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        val call = hotelAPI.getHotels()
        call.enqueue(object : Callback<HotelResponse> {
            override fun onResponse(call: Call<HotelResponse>, response: Response<HotelResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        onSuccess(it.body)
                    } ?: onError(Exception("Response null"))
                } else {
                    onError(Exception("Error code: ${response.code()}"))
                }
            }

            override fun onFailure(call: Call<HotelResponse>, t: Throwable) {
                onError(t)
            }
        })
    }

    fun fetchHotelByLocation(
        locationId:Long,
        onSuccess: (List<Hotel>) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        val call = hotelAPI.getHotelsByLocation(locationId)
        call.enqueue(object : Callback<HotelResponse> {
            override fun onResponse(call: Call<HotelResponse>, response: Response<HotelResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        onSuccess(it.body)
                    } ?: onError(Exception("Response null"))
                } else {
                    onError(Exception("Error code: ${response.code()}"))
                }
            }

            override fun onFailure(call: Call<HotelResponse>, t: Throwable) {
                onError(t)
            }
        })
    }


}