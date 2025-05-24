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
import java.text.DecimalFormat

class HotelDaDatAdapter(
    val context: Context,
    private val bookingList: ArrayList<Booking>
) : RecyclerView.Adapter<HotelDaDatAdapter.BookingViewHolder>() {


    //khai bao nut huy phong
    var onCancelClick: ((Booking, Int) -> Unit)? = null


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
        val hotel = room?.hotel  // Lấy hotel từ room

        val b = holder.binding

        b.tenKS.text = hotel?.name ?: "Khách sạn không xác định"
        b.tvRoomName.text = "Phòng: ${room?.roomNumber ?: "?"}"
        b.price.text = "Giá: ${room?.price?.toInt()?.let { formatCurrencyVND(it) } ?: "0"} VND"
        b.tvRoomCapacity.text = "Sức chứa: ${room?.capacity ?: "?"} người"
        b.tvRoomDescription.text = room?.description ?: "Chưa có mô tả"
        b.tvCustomerId.text = "Khách hàng: ${customer?.fullName ?: "Không xác định"}"
        b.tvCheckInDate.text = "Check-in: ${booking.checkinDate ?: "?"}"
        b.tvCheckOutDate.text = "Check-out: ${booking.checkoutDate ?: "?"}"
        b.tvStatus.text = "Trạng thái: ${booking.status ?: "?"}"

        Glide.with(b.imgRoom.context)
            .load(room?.image?.let { "${context.getString(R.string.localUpload)}$it" } ?: R.drawable.ic_launcher_background)
            .placeholder(R.drawable.ic_launcher_background)
            .into(b.imgRoom)

        b.btnHuy.setOnClickListener {
            onCancelClick?.invoke(booking, position)
        }
    }


    override fun getItemCount(): Int = bookingList.size

    fun formatCurrencyVND(amount: Int): String {
        val formatter = DecimalFormat("#,###")
        return formatter.format(amount).replace(',', '.')
    }

    fun updateItem(position: Int, newBooking: Booking) {
        bookingList[position] = newBooking
        notifyItemChanged(position)
    }


}