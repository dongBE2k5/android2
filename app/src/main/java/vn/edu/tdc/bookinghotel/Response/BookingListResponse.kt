package vn.edu.tdc.bookinghotel.Response

import vn.edu.tdc.bookinghotel.Model.Booking
import vn.edu.tdc.bookinghotel.Model.Customer


data class BookingListResponse(
    val headers: Map<String, Any>,
    val body: List<Booking>,
    val statusCode: String,
    val statusCodeValue: Int
)