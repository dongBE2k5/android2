package vn.edu.tdc.bookinghotel.Response

import vn.edu.tdc.bookinghotel.Model.Hotel


data class HotelResponse(
    val headers: Map<String, Any>,
    val body: List<Hotel>,
    val statusCode: String,
    val statusCodeValue: Int
)