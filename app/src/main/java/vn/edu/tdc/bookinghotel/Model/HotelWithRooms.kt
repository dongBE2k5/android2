package vn.edu.tdc.bookinghotel.Model

data class HotelWithRooms(
    val hotel: Hotel,
    val rooms: List<Room>
)