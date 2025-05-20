package vn.edu.tdc.bookinghotel.Repository

import android.content.Context
import android.util.Log
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import vn.edu.tdc.bookinghotel.CallAPI.RoomAPI
import vn.edu.tdc.bookinghotel.CallAPI.UserAPI
import vn.edu.tdc.bookinghotel.Model.Room
import vn.edu.tdc.bookinghotel.Model.User
import vn.edu.tdc.bookinghotel.Response.RoomRespose
import vn.edu.tdc.bookinghotel.Response.UserResponse
import vn.edu.tdc.bookinghotel.Session.SessionManager

class UserRepository(private val context: Context) {

    val session = SessionManager(context)

    val client = OkHttpClient.Builder().addInterceptor { chain ->
        val token = session.getToken()
        val request = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer $token")
            .build()
        chain.proceed(request)
    }.build()
    private val retrofit = Retrofit.Builder()
        .baseUrl(UserAPI.BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val userAPI = retrofit.create(UserAPI::class.java)
    fun fetchlogin(

        username: String,
        password: String,
        onSuccess:(UserResponse)->Unit,
        onError: (Throwable)->Unit
    ){
//        val user = User(username, password)
        val call = userAPI.login(username, password)
        call.enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        session.saveToken(it.token)
                        session.saveIdUser(it.id)
                        session.saveUserName(it.username)
                        val responseCustomer=CustomerRepository()

                        responseCustomer.fetchCustomerByUser(
                            it.id.toLong(),
                            onSuccess = { response ->
                                Log.d("Find", "Tìm thấy customer")
                            },
                            onError = { error ->
                                Log.d("Find", "Không tìm thấy customer")
                                Log.d("Find", error.message.toString())

                                if ( !error.message.toString().isEmpty()) {
                                    responseCustomer.fetchCreatedByUser(
                                        it.id.toLong(),
                                        onSuccess = { createdResponse ->
                                            Log.d("Find", "Tạo customer thành công")
                                        },
                                        onError = { createError ->
                                            Log.e("Find", "Tạo customer thất bại: ${createError.message}")
                                        }
                                    )
                                } else {
                                    Log.e("Find", "Lỗi khác: ${error.message}")
                                }
                            }
                        )



                        onSuccess(it)
                    } ?: onError(Exception("Null response body"))
                } else {
                    onError(Exception("Login failed: ${response.code()}"))
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                onError(t)
            }
        })
    }

    fun fetchregister(

        username: String,
        password: String,
        onSuccess:(UserResponse)->Unit,
        onError: (Throwable)->Unit
    ){
//        val user = User(username, password)
        val call = userAPI.register(username, password)
        call.enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let {

                        onSuccess(it)
                    } ?: onError(Exception("Null response body"))
                } else {
                    onError(Exception("Login failed: ${response.code()}"))
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                onError(t)
            }
        })
    }
}




