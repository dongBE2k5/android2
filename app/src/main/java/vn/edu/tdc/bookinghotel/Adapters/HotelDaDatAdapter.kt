package vn.edu.tdc.bookinghotel.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

import vn.edu.tdc.bookinghotel.Model.Hotel_Booking
import vn.edu.tdc.bookinghotel.R

class HotelDaDatAdapter(
    private val bookingList: List<Hotel_Booking>
) : RecyclerView.Adapter<HotelDaDatAdapter.BookingViewHolder>() {

    inner class BookingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Các trường cũ
        val tvBookingId: TextView = itemView.findViewById(R.id.tvBookingId)
        val tvCustomerId: TextView = itemView.findViewById(R.id.tvCustomerId)
        val tvRoomId: TextView = itemView.findViewById(R.id.tvRoomId)
        val tvCheckInDate: TextView = itemView.findViewById(R.id.tvCheckInDate)
        val tvCheckOutDate: TextView = itemView.findViewById(R.id.tvCheckOutDate)
        val tvStatus: TextView = itemView.findViewById(R.id.tvStatus)
        val tvRoomName: TextView = itemView.findViewById(R.id.tvRoomName)
        val imgRoom: ImageView = itemView.findViewById(R.id.imgRoom)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookingViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.khach_san_da_dat_recyleview, parent, false)
        return BookingViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookingViewHolder, position: Int) {
        val booking = bookingList[position]

        // Liên kết dữ liệu cho các trường cũ
        holder.tvBookingId.text = "Booking ID: ${booking.bookingId}"
        holder.tvCustomerId.text = "Customer ID: ${booking.customerId}"
        holder.tvRoomId.text = "Room ID: ${booking.roomId}"
        holder.tvCheckInDate.text = "Check-In: ${booking.checkInDate}"
        holder.tvCheckOutDate.text = "Check-Out: ${booking.checkOutDate}"
        holder.tvStatus.text = "Status: ${booking.status}"
        holder.tvRoomName.text = booking.roomName
        holder.imgRoom.setImageResource(R.drawable.khachsan)

    }

    override fun getItemCount(): Int = bookingList.size
}