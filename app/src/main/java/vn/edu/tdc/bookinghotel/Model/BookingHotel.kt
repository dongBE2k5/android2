package vn.edu.tdc.bookinghotel.Model

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import vn.edu.tdc.bookinghotel.Adapters.Hotel_BookingViewAdapter
import vn.edu.tdc.bookinghotel.databinding.BookingHotelBinding

class BookingHotel: AppCompatActivity() {
    private lateinit var binding: BookingHotelBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapterBooking: Hotel_BookingViewAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        binding = BookingHotelBinding.inflate(layoutInflater)
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
        super.onCreate(savedInstanceState)

    }
}