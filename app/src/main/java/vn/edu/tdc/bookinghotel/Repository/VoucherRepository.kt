package vn.edu.tdc.bookinghotel.Repository

import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import vn.edu.tdc.bookinghotel.CallAPI.VoucherAPI
import vn.edu.tdc.bookinghotel.Model.Voucher
import vn.edu.tdc.bookinghotel.Response.VoucherReponse


class VoucherRepository {

    private val retrofit = Retrofit.Builder()
        .baseUrl(VoucherAPI.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val voucherAPI = retrofit.create(VoucherAPI::class.java)

    fun findVoucherByCode(
        code: String,
        onSuccess: (Voucher) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        val call = voucherAPI.getVouchersByCode(code)
        call.enqueue(object : Callback<VoucherReponse> {
            override fun onResponse(call: Call<VoucherReponse>, response: Response<VoucherReponse>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        it?.body?.let { it1 -> onSuccess(it1) }
                    } ?: onError(Exception("Response null"))
                } else {
                    Log.d("Error code", "Error")
                    onError(Exception("Error code: ${response.code()}"))
                }
            }

            override fun onFailure(call: Call<VoucherReponse>, t: Throwable) {
                onError(t)
            }
        })
    }

}