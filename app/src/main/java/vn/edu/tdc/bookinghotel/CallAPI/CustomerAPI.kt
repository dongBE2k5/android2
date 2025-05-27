package vn.edu.tdc.bookinghotel.CallAPI

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import vn.edu.tdc.bookinghotel.Model.CustomerUpdateUser
import vn.edu.tdc.bookinghotel.Model.CustomerUpdated
import vn.edu.tdc.bookinghotel.Response.CustomerResponse

interface CustomerAPI {
    // Dinh nghia API de Retrofit lay du lieu ve tu Webservice
    companion object {
        //        const val BASE_URL = "https://hotel-manager-production-b051.up.railway.app/api/"
//        const val BASE_URL = "http://192.168.1.4:8080/api/"
            const val BASE_URL = ApiConfig.BASE_URL
    }

    @GET("customers")
    fun getCustomers(): Call<CustomerResponse>

    @GET("customers/user/{userId}")
    fun getCustomerByUser(@Path("userId") userId: Long): Call<CustomerResponse>

    @GET("customers/{customerId}")
    fun getCustomerById(@Path("customerId") customerId: Long): Call<CustomerResponse>


    @POST("customers/created/{userId}")
    fun createByUser(@Path("userId") userId: Long): Call<CustomerResponse>

    @PUT("customers/customer-new/{customerId}")
    fun updateCustomer(@Path("customerId") customerId: Long, @Body customer: CustomerUpdated): Call<CustomerResponse>

    @PUT("customers/userNew/{userId}")
    fun updateCustomerByUser(@Path("userId") userId: Long, @Body customer: CustomerUpdateUser): Call<CustomerResponse>
}