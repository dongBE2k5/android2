package vn.edu.tdc.bookinghotel.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import vn.edu.tdc.bookinghotel.Adapters.ListDetailRecyclerViewAdapter.MyViewHolder
import vn.edu.tdc.bookinghotel.Model.Booking

import vn.edu.tdc.bookinghotel.Model.Hotel_Booking
import vn.edu.tdc.bookinghotel.R
import vn.edu.tdc.bookinghotel.databinding.ChiTietPhongBinding
import vn.edu.tdc.bookinghotel.databinding.KhachSanDaDatRecyleviewBinding

class HotelDaDatAdapter(
    val context: Context,
    private val bookingList: ArrayList<Booking>
) : RecyclerView.Adapter<HotelDaDatAdapter.BookingViewHolder>() {

    inner class BookingViewHolder(val binding: KhachSanDaDatRecyleviewBinding) :
        RecyclerView.ViewHolder(binding.root)

    inner class MyViewHolder(val binding: ChiTietPhongBinding) : RecyclerView.ViewHolder(binding.root) {
}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookingViewHolder {
        val binding = KhachSanDaDatRecyleviewBinding.inflate(LayoutInflater.from(context), parent, false)
        return BookingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BookingViewHolder, position: Int) {
        val booking = bookingList[position]
        val room = booking.room
        val customer = booking.customer

        val b = holder.binding

        b.tvRoomName.text = "Phòng: ${room.roomNumber}"
        b.price.text = "Giá: ${room.price.toInt()}đ"
        b.tvRoomCapacity.text = "Sức chứa: ${room.capacity} người"
        b.tvRoomDescription.text = room.description
        b.tvCustomerId.text = "Khách hàng: ${customer.fullName}"
        b.tvCheckInDate.text = "Check-in: ${booking.checkinDate}"
        b.tvCheckOutDate.text = "Check-out: ${booking.checkoutDate}"
        b.tvStatus.text = "Trạng thái: ${booking.status}"

        
        Glide.with(b.imgRoom.context)
            .load("${context.getString(R.string.localUpload)}${room.image}")
            .placeholder(R.drawable.ic_launcher_background)
            .into(b.imgRoom)


    }

    override fun getItemCount(): Int = bookingList.size
}