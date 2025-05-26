package vn.edu.tdc.bookinghotel.Model

import java.math.BigDecimal

data class ChiTietDatHangAdmin (
    // Các trường cũ được giữ nguyên
    val bookingId: Long = 0,
    val userName:String="",
    val customerId: Long=0,
    val roomId: Long = 0,
    val tongTien: BigDecimal,
    val checkInDate: String = "",
    val checkOutDate: String = "",
    val status: String = "",
    val roomName: String = "",
    val imageUrl:String = "",
)