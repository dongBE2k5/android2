package vn.edu.tdc.bookinghotel.Response

import vn.edu.tdc.bookinghotel.Model.Hotel
import vn.edu.tdc.bookinghotel.Model.Voucher

data class VoucherReponse(
    val headers: Map<String, Any>,
    val body: Voucher,
    val statusCode: String,
    val statusCodeValue: Int
)
