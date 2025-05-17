package vn.edu.tdc.bookinghotel.Response

import vn.edu.tdc.bookinghotel.Model.Location
import vn.edu.tdc.bookinghotel.Model.Room

class RoomRespose (
    val headers: Map<String, Any>,
    val body: List<Room>,
    val statusCode: String,
    val statusCodeValue: Int
)

