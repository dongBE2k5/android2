package vn.edu.tdc.bookinghotel

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import vn.edu.tdc.bookinghotel.Adapters.Hotel_BookingViewAdapter
import vn.edu.tdc.bookinghotel.Model.Hotel_Booking
import vn.edu.tdc.bookinghotel.databinding.ActivityHotelBookkingBinding

class Hotel_BookingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHotelBookkingBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: Hotel_BookingViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHotelBookkingBinding.inflate(layoutInflater)

        // full màn hình
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

        // Bottom Navigation xử lý chuyển activity
        val selectedItem = intent.getIntExtra("selected_nav", R.id.nav_home)
        binding.bottomNav.selectedItemId = selectedItem
        binding.bottomNav.setOnItemSelectedListener { item ->
            if (item.itemId != selectedItem) {
                val intent = when (item.itemId) {
                    R.id.nav_home -> Intent(this, MainActivity::class.java)
                    R.id.nav_store -> Intent(this, StoreActivity::class.java)
                    R.id.nav_profile -> Intent(this, AcountActivity::class.java)
                    R.id.nav_admin -> Intent(this, AdminActivity::class.java)
                    else -> null
                }
                intent?.let {
                    it.putExtra("selected_nav", item.itemId)
                    startActivity(it)
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                    finish()
                }
                true
            } else {
                true
            }
        }


        recyclerView = binding.recyclerViewBookings
        recyclerView.layoutManager = LinearLayoutManager(this)

        val dummyData = generateDummyBookings()
        adapter = Hotel_BookingViewAdapter(dummyData)
        recyclerView.adapter = adapter
    }

    private fun generateDummyBookings(): List<Hotel_Booking> {
        return listOf(
            Hotel_Booking(
                bookingId = 1,
                customerId = 1001,
                roomId = 201,
                checkInDate = "2025-05-01",
                checkOutDate = "2025-05-05",
                status = "Đã đặt",
                roomName = "Phòng Deluxe Hướng Biển",
                imageUrl = R.drawable.khachsan,
                contactInfo = "user1@example.com",
                voucherCode = "SUMMER2025",
                paymentMethods = "Tiền mặt,Chuyển khoản",
                totalAmount = "5,000,000 VNĐ",
                isInsuranceSelected = true,
                insurancePrice = "43,500 VNĐ"
            )
        )
    }
}