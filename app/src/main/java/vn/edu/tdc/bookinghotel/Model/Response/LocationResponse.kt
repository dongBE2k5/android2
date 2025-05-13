package vn.edu.tdc.bookinghotel.Model.Response

import vn.edu.tdc.bookinghotel.Model.Location

data class LocationResponse(
    val headers: Map<String, Any>,
    val body: List<Location>,
    val statusCode: String,
    val statusCodeValue: Int
)