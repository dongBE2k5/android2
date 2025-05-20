package vn.edu.tdc.bookinghotel.Repository

import android.util.Log
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import vn.edu.tdc.bookinghotel.CallAPI.CustomerAPI
import vn.edu.tdc.bookinghotel.CallAPI.LocationAPI
import vn.edu.tdc.bookinghotel.Model.Hotel
import vn.edu.tdc.bookinghotel.Model.Customer
import vn.edu.tdc.bookinghotel.Model.CustomerUpdate
import vn.edu.tdc.bookinghotel.Response.CustomerResponse
import vn.edu.tdc.bookinghotel.Response.HotelResponse


class CustomerRepository {

    private val retrofit = Retrofit.Builder()
        .baseUrl(CustomerAPI.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val customerAPI = retrofit.create(CustomerAPI::class.java)

    fun fetchCustomer(
        id: Long,
        onSuccess: (Customer) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        val call = customerAPI.getCustomerById(id)
        call.enqueue(object : Callback<CustomerResponse> {
            override fun onResponse(call: Call<CustomerResponse>, response: Response<CustomerResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        Log.d("customer", "${it.body}")

                        onSuccess(it.body)
                    } ?: onError(Exception("Response null"))
                } else {
                    onError(Exception("Error code: ${response.code()}"))
                }
            }

            override fun onFailure(call: Call<CustomerResponse>, t: Throwable) {
                onError(t)
            }
        })
    }

    fun updateCustomer(
        id: Long,
        customer: CustomerUpdate,
        onSuccess: (Customer) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        val call = customerAPI.updateCustomer(id, customer)
        call.enqueue(object : Callback<CustomerResponse> {
            override fun onResponse(call: Call<CustomerResponse>, response: Response<CustomerResponse>) {
                Log.d("customerRepo", "${customer.toString()}")
                Log.d("customerRepo", "${response}")

                if (response.isSuccessful) {
                    response.body()?.let {

                        onSuccess(it.body)
                    } ?: onError(Exception("Response null"))
                } else {
                    onError(Exception("Error code: ${response.code()}"))
                }
            }

            override fun onFailure(call: Call<CustomerResponse>, t: Throwable) {
                onError(t)
            }
        })
    }
    fun fetchCustomerByUser(
        id: Long,
        onSuccess: (Customer) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        val call = customerAPI.getCustomerByUser(id)
        call.enqueue(object : Callback<CustomerResponse> {
            override fun onResponse(call: Call<CustomerResponse>, response: Response<CustomerResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        Log.d("customer", "${it.body}")

                        onSuccess(it.body)
                    } ?: onError(Exception("Response null"))
                } else {
                    val errorJson = response.errorBody()?.string()
                    try {
                        val jsonObject = JSONObject(errorJson)
                        val message = jsonObject.getString("body") // Lấy trường "body"
                        onError(Exception(message))
                    } catch (e: Exception) {
                        onError(Exception("Error parsing error response: $errorJson"))
                    }

                }
            }

            override fun onFailure(call: Call<CustomerResponse>, t: Throwable) {
                onError(t)
            }
        })
    }

    fun fetchCreatedByUser(
        id: Long,
        onSuccess: (Customer) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        val call = customerAPI.createByUser(id)
        call.enqueue(object : Callback<CustomerResponse> {
            override fun onResponse(call: Call<CustomerResponse>, response: Response<CustomerResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        Log.d("customer", "${it.body}")

                        onSuccess(it.body)
                    } ?: onError(Exception("Response null"))
                } else {
                    onError(Exception("Error code: ${response.code()}"))
                }
            }

            override fun onFailure(call: Call<CustomerResponse>, t: Throwable) {
                onError(t)
            }
        })
    }





}