package vn.edu.tdc.bookinghotel.Activity

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import vn.edu.tdc.bookinghotel.Adapters.HotelDaDatAdapter
import vn.edu.tdc.bookinghotel.Model.Booking
import vn.edu.tdc.bookinghotel.Model.Hotel_Booking
import vn.edu.tdc.bookinghotel.R
import vn.edu.tdc.bookinghotel.Repository.BookingRepository
import vn.edu.tdc.bookinghotel.Session.SessionManager
import vn.edu.tdc.bookinghotel.View.BottomNavHelper
import vn.edu.tdc.bookinghotel.databinding.StoreBinding

class StoreActivity : AppCompatActivity() {

    private lateinit var binding: StoreBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: HotelDaDatAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = StoreBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        //full màn hiình
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

        window.setDecorFitsSystemWindows(false)

        window.insetsController?.let { controller ->
            controller.hide(
                android.view.WindowInsets.Type.statusBars() or android.view.WindowInsets.Type.navigationBars()
            )
            controller.systemBarsBehavior = android.view.WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
        // Bottom Navigation xử lý chuyển activity
        val selectedItem = intent.getIntExtra("selected_nav", R.id.nav_home)
        BottomNavHelper.setup(this, binding.bottomNav, selectedItem)

        val session = SessionManager(this)
        Log.d("IDMain" , "${session.getIdUser()}")

        recyclerView = binding.recycleKsDaDat
        recyclerView.layoutManager = LinearLayoutManager(this)
        val BookedHotel = ArrayList<Booking>()
        val bookingRepository = BookingRepository()
        bookingRepository.getBookingByCustomerId(session.getIdUser()!!.toLong(), { bookings ->
            BookedHotel.addAll(bookings)
            Log.d("List", "${BookedHotel.toString()}")
            adapter = HotelDaDatAdapter(this, BookedHotel)
            recyclerView.adapter = adapter
            adapter.notifyDataSetChanged()
        }, { error ->
            Log.e("StoreActivity", "Error fetching bookings: $error")
        })

//        val dummyData = generateDummyBookings()

//
    }

//    private fun generateDummyBookings(): List<Hotel_Booking> {
//        return listOf(
//            Hotel_Booking(
//                bookingId = 1,
//                customerId = 1001,
//                roomId = 201,
//                checkInDate = "2025-05-01",
//                checkOutDate = "2025-05-05",
//                status = "Đã đặt",
//                roomName = "Phòng Deluxe Hướng Biển",
//                imageUrl = R.drawable.khachsan,
//                contactInfo = "user1@example.com",
//                voucherCode = "SUMMER2025",
//                paymentMethods = "Tiền mặt,Chuyển khoản",
//                totalAmount = "5,000,000 VNĐ",
//                isInsuranceSelected = true,
//                insurancePrice = "43,500 VNĐ"
//            )
//        )
//    }
}

