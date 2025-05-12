package vn.edu.tdc.bookinghotel.Model

data class ChiTietDatHangAdmin (
    // Các trường cũ được giữ nguyên
    val bookingId: Int = 0,
    val userName:String="",
    val roomId: Int = 0,
    val tongTien: Int = 0,
    val checkInDate: String = "",
    val checkOutDate: String = "",
    val status: String = "",
    val roomName: String = "",
    val imageUrl:Int = 0,
)