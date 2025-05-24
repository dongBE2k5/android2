package vn.edu.tdc.bookinghotel.Model

data class BookingStatusUpdateRequest(
    val bookingId: Long,
    val status: String
)
