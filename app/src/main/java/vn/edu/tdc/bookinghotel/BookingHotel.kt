package vn.edu.tdc.bookinghotel

import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import vn.edu.tdc.bookinghotel.Adapters.Hotel_BookingViewAdapter
import vn.edu.tdc.bookinghotel.databinding.BookingHotelBinding
import vn.edu.tdc.bookinghotel.databinding.EditProfileAccountBinding

class BookingHotel: AppCompatActivity() {
    private lateinit var binding: BookingHotelBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapterBooking: Hotel_BookingViewAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        binding = BookingHotelBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)

    }
}