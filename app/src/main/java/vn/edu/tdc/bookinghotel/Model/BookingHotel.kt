package vn.edu.tdc.bookinghotel.Model

import android.os.Bundle
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
        super.onCreate(savedInstanceState)

    }
}