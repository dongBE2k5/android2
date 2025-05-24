package vn.edu.tdc.bookinghotel.Model

import com.google.gson.annotations.SerializedName
class Hotel(
    @SerializedName("hotelId")
    val id: Long = 0L,

    @SerializedName("name")
    val name: String = "",

    @SerializedName("address")
    val address: String = "",

    @SerializedName("status")
    val status: String = "",

    @SerializedName("image")
    val image: String = "",

    @SerializedName("location")
    val locations: Location = Location(),

    var roomCount: Int? = null
)
