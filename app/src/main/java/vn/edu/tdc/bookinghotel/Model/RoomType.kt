package vn.edu.tdc.bookinghotel.Model

import com.google.gson.annotations.SerializedName

data class RoomType(
    @SerializedName("roomTypeId")
    val id: Long = 0L,

    @SerializedName("name")
    val name: String = ""
)
