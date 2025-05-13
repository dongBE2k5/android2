package vn.edu.tdc.bookinghotel.Repository


import android.util.Log
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import vn.edu.tdc.bookinghotel.CallAPI.LocationAPI
import vn.edu.tdc.bookinghotel.Model.Location
import vn.edu.tdc.bookinghotel.Response.LocationResponse


class LocationRepository {

    private val retrofit = Retrofit.Builder()
        .baseUrl(LocationAPI.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val locationAPI = retrofit.create(LocationAPI::class.java)

    fun fetchLocations(
        onSuccess: (List<Location>) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        val call = locationAPI.getLocations()
        call.enqueue(object : Callback<LocationResponse> {
            override fun onResponse(call: Call<LocationResponse>, response: Response<LocationResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        onSuccess(it.body)
                    } ?: onError(Exception("Response null"))
                } else {
                    onError(Exception("Error code: ${response.code()}"))
                }
            }

            override fun onFailure(call: Call<LocationResponse>, t: Throwable) {
                onError(t)
            }
        })
    }
}