package vn.edu.tdc.bookinghotel

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import vn.edu.tdc.bookinghotel.Adapters.ChiTietPhongRecyclerViewAdapter
import vn.edu.tdc.bookinghotel.Adapters.Hotel_BookingViewAdapter
import vn.edu.tdc.bookinghotel.Adapters.ListDetailRecyclerViewAdapter
import vn.edu.tdc.bookinghotel.Model.Hotel_Booking
import vn.edu.tdc.bookinghotel.databinding.ActivityHotelBookkingBinding
import vn.edu.tdc.bookinghotel.databinding.DetailRoomBinding
import vn.edu.tdc.bookinghotel.fragment.ChiTietPhong
import vn.edu.tdc.bookinghotel.fragment.ListDetail

class ChiTietKhachSan : AppCompatActivity(), ChiTietPhongRecyclerViewAdapter.OnItemClickListener {

    private lateinit var binding: DetailRoomBinding
    private lateinit var adapterListDetail: ListDetailRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DetailRoomBinding.inflate(layoutInflater)
        // Full màn hình
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.navigationBarColor = Color.TRANSPARENT
            window.statusBarColor = Color.TRANSPARENT
        }
        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                )
        setContentView(binding.root)

        // Nhận dữ liệu từ Intent
        val hotelName = intent.getStringExtra("hotel_name")
        binding.tvTenKhachSan.text = hotelName ?: "Tên khách sạn không có"

        // Dữ liệu phòng chi tiết
        val danhSachPhong = listOf(
            ChiTietPhong(
                nameDichVu = "Phòng Deluxe Hướng Biển",
                thongTin1 = "Miễn phí ăn sáng, Wifi tốc độ cao",
                thongTin2 = "2 người lớn, 1 trẻ em - Diện tích 35m²",
                hotelDeals = "Hotel Deals",
                giaTien = 1200000,
                tongGiaTien = 2400000,
                phongConLai = 3,
                maVoucher = "DELUXE20"
            ),
            ChiTietPhong(
                nameDichVu = "Phòng Superior City View",
                thongTin1 = "Miễn phí ăn sáng, Wifi miễn phí",
                thongTin2 = "2 người lớn - Diện tích 28m²",
                hotelDeals = "Hotel Deals",
                giaTien = 1000000,
                tongGiaTien = 2000000,
                phongConLai = 5,
                maVoucher = "SUPERIOR15"
            ),
            ChiTietPhong(
                nameDichVu = "Phòng Suite Hướng Biển",
                thongTin1 = "Miễn phí bữa tối, phòng tắm bồn nước nóng",
                thongTin2 = "2 người lớn, 2 trẻ em - Diện tích 50m²",
                hotelDeals = "Hotel Deals",
                giaTien = 1500000,
                tongGiaTien = 3000000,
                phongConLai = 2,
                maVoucher = "SUITE25"
            ),
            ChiTietPhong(
                nameDichVu = "Phòng Family Garden View",
                thongTin1 = "Bể bơi miễn phí, khu vui chơi trẻ em",
                thongTin2 = "4 người lớn, 2 trẻ em - Diện tích 60m²",
                hotelDeals = "Hotel Deals",
                giaTien = 1800000,
                tongGiaTien = 3600000,
                phongConLai = 4,
                maVoucher = "FAMILY30"
            ),
            ChiTietPhong(
                nameDichVu = "Phòng Executive Hướng Hồ",
                thongTin1 = "Miễn phí spa, phòng gym hiện đại",
                thongTin2 = "2 người lớn - Diện tích 45m²",
                hotelDeals = "Hotel Deals",
                giaTien = 2000000,
                tongGiaTien = 4000000,
                phongConLai = 1,
                maVoucher = "EXECUTIVE10"
            )
            // Thêm dữ liệu cho các phòng khác
        )

        // Dữ liệu list trung gian
        val detailPhong = arrayListOf(ListDetail("Phòng có sẵn", danhSachPhong))

        // Gán adapter cho RecyclerView và truyền listener
        val recyclerViewListDetail = findViewById<RecyclerView>(R.id.recyclerViewListDetail)
        recyclerViewListDetail.layoutManager = LinearLayoutManager(this)
        recyclerViewListDetail.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        adapterListDetail = ListDetailRecyclerViewAdapter(detailPhong, this) // Truyền this vào
        recyclerViewListDetail.adapter = adapterListDetail
    }

    // Xử lý click khi người dùng nhấn "Đặt"
    override fun onDatClick(position: Int) {
        // Xử lý sự kiện click vào "Đặt" tại vị trí item
        val intent = Intent(this@ChiTietKhachSan, Hotel_BookingActivity::class.java)
        val selectedItem = intent.getIntExtra("selected_nav", R.id.nav_store)
        startActivity(intent)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }
}
