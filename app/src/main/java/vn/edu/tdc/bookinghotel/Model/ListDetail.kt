package vn.edu.tdc.bookinghotel.Model

data class ListDetail(
    val tvPhongCoSan: String = "",
    val danhSachPhong: List<Room> = listOf()
)
