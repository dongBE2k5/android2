package vn.edu.tdc.bookinghotel.Response

import vn.edu.tdc.bookinghotel.Model.Customer


data class CustomerResponse(
    val headers: Map<String, Any>,
    val body: Customer,
    val statusCode: String,
    val statusCodeValue: Int
)