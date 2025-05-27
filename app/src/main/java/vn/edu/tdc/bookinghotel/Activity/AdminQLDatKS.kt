package vn.edu.tdc.bookinghotel.Activity

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import vn.edu.tdc.bookinghotel.Adapters.ChiTietDatHangAdminRecyclerViewAdapter
import vn.edu.tdc.bookinghotel.Model.Booking
import vn.edu.tdc.bookinghotel.Model.ChiTietDatHangAdmin
import vn.edu.tdc.bookinghotel.Model.Location
import vn.edu.tdc.bookinghotel.R
import vn.edu.tdc.bookinghotel.Repository.BookingRepository
import vn.edu.tdc.bookinghotel.Repository.LocationRepository
import vn.edu.tdc.bookinghotel.View.BottomNavHelper
import vn.edu.tdc.bookinghotel.databinding.AdminChiTietDatHangBinding

class AdminQLDatKS: AppCompatActivity() {

    private lateinit var binding: AdminChiTietDatHangBinding
    private lateinit var adapter: ChiTietDatHangAdminRecyclerViewAdapter
//

//        ChiTietDatHangAdmin(
//            bookingId = 1,
//            userName = "Nguyen Van A",
//            customerId = 421312,
//            roomId = 101,
//            tongTien = 1500000,
//            checkInDate = "2025-06-01",
//            checkOutDate = "2025-06-05",
//            status = "Đã xác nhận",
//            roomName = "Phòng Deluxe",
//            imageUrl = R.drawable.khachsan
//        )
//    )



    override fun onCreate(savedInstanceState: Bundle?) {
        binding = AdminChiTietDatHangBinding.inflate(layoutInflater)
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

        // Bottom Navigation setup
        val selectedItem = intent.getIntExtra("selected_nav", R.id.nav_admin)
        BottomNavHelper.setup(this, binding.bottomNav, selectedItem)

        window.setDecorFitsSystemWindows(false)

        window.insetsController?.let { controller ->
            controller.hide(
                android.view.WindowInsets.Type.statusBars() or android.view.WindowInsets.Type.navigationBars()
            )
            controller.systemBarsBehavior = android.view.WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
        val idBooking=intent.getLongExtra("idBooking",0L);
        Log.d("idBooking",idBooking.toString())
        val repositoryBooking=BookingRepository()
        var chiTietDatKS =ArrayList<ChiTietDatHangAdmin>()
        repositoryBooking.getBookingById(
            idBooking,
            onSuccess = onSuccess@{ booking ->
                if (booking == null) {
                    Log.d("Bookingi", "Booking is null")
                    return@onSuccess
                }
                Log.d("bookingPrice", "${booking.price}")
                var listBooking=ChiTietDatHangAdmin(
                    booking.id,booking.customer.fullName,booking.customer.id,booking.room.id, booking.price ,booking.checkinDate,booking.checkoutDate,booking.status,
                    booking.room.roomNumber,booking.room.image)

                chiTietDatKS.add(listBooking)
                // RecyclerViews: nguoi dung dat hang
                val recyclerViewChiTietKSDaDat = findViewById<RecyclerView>(R.id.recycleChiTietKsDaDat)
                recyclerViewChiTietKSDaDat.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
                adapter = ChiTietDatHangAdminRecyclerViewAdapter(this, chiTietDatKS)
                recyclerViewChiTietKSDaDat.adapter = adapter
            },
            onError = {

            }
        )



    }
}