package vn.edu.tdc.bookinghotel.Response

import vn.edu.tdc.bookinghotel.Model.Room

class RoomResponse (
    val headers: Map<String, Any>,
    val body: List<Room>,
    val statusCode: String,
    val statusCodeValue: Int
)
class RoomSingleResponse (
    val headers: Map<String, Any>,
    val body: Room,              // body là object Room, không phải list
    val statusCode: String,
    val statusCodeValue: Int
)

