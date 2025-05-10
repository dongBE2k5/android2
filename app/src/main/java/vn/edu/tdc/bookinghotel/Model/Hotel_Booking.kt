package vn.edu.tdc.bookinghotel.Model

data class Hotel_Booking (
    // Các trường cũ được giữ nguyên
    val bookingId: Int = 0,
    val customerId: Int = 0,
    val roomId: Int = 0,
    val checkInDate: String = "",
    val checkOutDate: String = "",
    val status: String = "",
    val roomName: String = "",
    val imageUrl:Int = 0,

    // Các trường mới được bổ sung
    val contactInfo: String = "", // Số điện thoại hoặc email
    val voucherCode: String = "", // Mã voucher
    val paymentMethods: String = "", // Phương thức thanh toán, ví dụ: "Cash,Transfer"
    val totalAmount: String = "0 VNĐ", // Tổng giá tiền
    val isInsuranceSelected: Boolean = false, // Trạng thái chọn bảo hiểm
    val insurancePrice: String = "0 VNĐ" // Giá bảo hiểm
)