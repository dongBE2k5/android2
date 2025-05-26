package vn.edu.tdc.bookinghotel.CallAPI

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import vn.edu.tdc.bookinghotel.Model.Voucher
import vn.edu.tdc.bookinghotel.Response.VoucherReponse


interface VoucherAPI {
    companion object {
        //        const val BASE_URL = "https://hotel-manager-production-b051.up.railway.app/api/"
//        const val BASE_URL = "http://192.168.1.4:8080/api/"
            const val BASE_URL = ApiConfig.BASE_URL
    }



    @GET("voucher/findByCode/{code}")
    fun getVouchersByCode(@Path("code") code: String): Call<VoucherReponse>


}