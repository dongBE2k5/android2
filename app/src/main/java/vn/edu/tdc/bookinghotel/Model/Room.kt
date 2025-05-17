package vn.edu.tdc.bookinghotel.Model

import com.google.gson.annotations.SerializedName

data class Room (
    @SerializedName("roomId")
    val id: String,

    @SerializedName("capacity")
    val capacity: Int = 0,

    @SerializedName("description")
    val description: String = "",

    @SerializedName("image")
    val image: String = "", // Đường dẫn hoặc URL ảnh

    @SerializedName("price")
    val price: Int = 0,

    @SerializedName("room_number")
    val roomNumber: String = "",

    @SerializedName("status")
    val status: String = "",

    @SerializedName("hotel_id")
    val hotelId: String = "",
    @SerializedName("roomType")
    val roomType: RoomType,



){
    override fun toString(): String {
        return "${id} - ${capacity} - ${description} - ${image} - ${status}"
    }
}
