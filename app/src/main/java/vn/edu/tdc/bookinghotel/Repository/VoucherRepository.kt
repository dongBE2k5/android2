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

    fun fetchAllVouchers(
        onSuccess: (List<Voucher>) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        val call = voucherAPI.getAllVouchers()
        call.enqueue(object : Callback<List<Voucher>> {
            override fun onResponse(call: Call<List<Voucher>>, response: Response<List<Voucher>>) {
                if (response.isSuccessful) {
                    val vouchers = response.body() ?: emptyList()
                    onSuccess(vouchers)
                } else {
                    onError(Exception("Response failed: ${response.code()}"))
                }
            }

            override fun onFailure(call: Call<List<Voucher>>, t: Throwable) {
                onError(t)
            }
        })
    }

    fun deleteVoucher(voucherId: Long, onSuccess: () -> Unit, onError: (Throwable) -> Unit) {
        val call = voucherAPI.deleteVoucher(voucherId)
        call.enqueue(object : retrofit2.Callback<Void> {
            override fun onResponse(call: Call<Void>, response: retrofit2.Response<Void>) {
                if (response.isSuccessful) {
                    onSuccess()
                } else {
                    onError(Throwable("Response code: ${response.code()}"))
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                onError(t)
            }
        })
    }
    fun decrementVoucherQuantity(
        voucherId: Long,
        onSuccess: () -> Unit,
        onGone: () -> Unit, // Voucher hết lượt và bị xóa
        onError: (Throwable) -> Unit
    ) {
        val call = voucherAPI.decrementVoucher(voucherId)
        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                when (response.code()) {
                    200 -> onSuccess()
                    410 -> {
                        // Voucher hết lượt -> xóa voucher
                        deleteVoucher(voucherId,
                            onSuccess = {
                                onGone()
                            },
                            onError = { error ->
                                onError(error)
                            }
                        )
                    }
                    else -> {
                        val errorBody = response.errorBody()?.string()
                        onError(Throwable("Unexpected response: ${response.code()}, $errorBody"))
                    }
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                onError(t)
            }
        })
    }

    //sắp xếp quantity taăng dần
    fun fetchVouchersSortedByQuantity(
        onSuccess: (List<Voucher>) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        val call = voucherAPI.getVouchersSortedByQuantity()
        call.enqueue(object : retrofit2.Callback<List<Voucher>> {
            override fun onResponse(
                call: retrofit2.Call<List<Voucher>>,
                response: retrofit2.Response<List<Voucher>>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let(onSuccess)
                } else {
                    onError(Throwable("Lỗi: ${response.code()}"))
                }
            }

            override fun onFailure(call: retrofit2.Call<List<Voucher>>, t: Throwable) {
                onError(t)
            }
        })
    }





}