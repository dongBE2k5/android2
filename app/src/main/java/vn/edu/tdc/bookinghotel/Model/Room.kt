package vn.edu.tdc.bookinghotel.Model

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

data class Room (
    @SerializedName("roomId")
    val id: Long,

    @SerializedName("capacity")
    val capacity: Int,

    @SerializedName("description")
    val description: String,

    @SerializedName("image")
    val image: String, // Đường dẫn hoặc URL ảnh

    @SerializedName("price")
    val price: Double,

    @SerializedName("roomNumber")
    val roomNumber: String,

    @SerializedName("status")
    val status: String,

    @SerializedName("hotel")
    val hotel: Hotel? = null,

    @SerializedName("roomType")
    val roomType: RoomType? = null



){
    override fun toString(): String {
        return "${id} - ${roomNumber} - ${price} - ${capacity} - ${description} - ${image} - ${status}"
    }
}
