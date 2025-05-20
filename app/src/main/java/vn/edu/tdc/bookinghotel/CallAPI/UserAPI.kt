package vn.edu.tdc.bookinghotel.CallAPI

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import vn.edu.tdc.bookinghotel.Model.LoginResponse
import vn.edu.tdc.bookinghotel.Model.User
import vn.edu.tdc.bookinghotel.Model.UserLogin
import vn.edu.tdc.bookinghotel.Response.UserResponse

interface UserAPI {
    companion object {
        //        const val BASE_URL = "https://hotel-manager-production-b051.up.railway.app/api/"
        const val BASE_URL = ApiConfig.BASE_URL
    }

    @FormUrlEncoded
    @POST("auth/login")
    fun login(@Field("username") username: String,
                 @Field("password") password: String): Call<UserResponse>


    @FormUrlEncoded
     @POST("auth/register")
     fun register(@Field("username") username: String,
                  @Field("password") password: String): Call<UserResponse>

}