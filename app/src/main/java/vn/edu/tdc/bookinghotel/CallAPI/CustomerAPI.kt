package vn.edu.tdc.bookinghotel.CallAPI

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query
import vn.edu.tdc.bookinghotel.Model.CustomerUpdate
import vn.edu.tdc.bookinghotel.Response.CustomerResponse
import vn.edu.tdc.bookinghotel.Response.HotelResponse
import vn.edu.tdc.bookinghotel.Response.LocationResponse

interface CustomerAPI {
    // Dinh nghia API de Retrofit lay du lieu ve tu Webservice
    companion object {
        //        const val BASE_URL = "https://hotel-manager-production-b051.up.railway.app/api/"
//        const val BASE_URL = "http://192.168.1.4:8080/api/"
            const val BASE_URL = "http://192.168.1.56:8080/api/"
    }

    @GET("customers")
    fun getCustomers(): Call<CustomerResponse>

    @GET("customers/{customerId}")
    fun getCustomerById(@Path("customerId") customerId: Long): Call<CustomerResponse>

    @PUT("customers/customer-new/{customerId}")
    fun updateCustomer(@Path("customerId") customerId: Long, @Body customer: CustomerUpdate): Call<CustomerResponse>
}