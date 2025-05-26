package vn.edu.tdc.bookinghotel.Model

import com.google.gson.annotations.SerializedName

data class Voucher(
    @SerializedName("id")
    val id: Long,
    @SerializedName("code")
    val code: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("percent")
    val percent: Int,
    @SerializedName("quantity")
    val quantity: Int,
)
