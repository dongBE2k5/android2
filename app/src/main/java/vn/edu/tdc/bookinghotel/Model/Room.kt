package vn.edu.tdc.bookinghotel.Model

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

data class  Room (
    @SerializedName("roomId")
    val id: Long,

    @SerializedName("capacity")
    val capacity: Int,

    @SerializedName("description")
    val description: String,

    @SerializedName("image")
    val image: String, // Đường dẫn hoặc URL ảnh

    @SerializedName("price")
    val price: BigDecimal, // Đổi sang BigDecimal để tính toán chính xác

    @SerializedName("roomNumber")
    val roomNumber: String,

    @SerializedName("status")
    val status: String,

    @SerializedName("hotel")
    val hotel: Hotel? = Hotel(),

    @SerializedName("roomType")
    val roomType: RoomType? = null,

    @SerializedName("area")
    val area: Double? = null, // Diện tích phòng (m²)

    @SerializedName("amenities")
    val amenities: List<String>? = null, // Danh sách tiện nghi (ví dụ: ["Wi-Fi", "Desk", "TV"])

    @SerializedName("soPhong")
    val soPhong: Int // Thêm thuộc tính soPhong
) {
    override fun toString(): String {
        return "${id} - ${roomNumber} - ${price} - ${capacity} - ${description} - ${image} - ${status} - ${area}m² - Amenities: ${amenities?.joinToString()} - SoPhong: $soPhong"
    }
}
