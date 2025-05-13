package vn.edu.tdc.bookinghotel.fragment

data class ListDetail(
    val tvPhongCoSan: String = "",
    val danhSachPhong: List<ChiTietPhong> = listOf()
)
