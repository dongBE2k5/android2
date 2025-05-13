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

class Hotel_BookingViewAdapter(
    private val bookingList: List<Hotel_Booking>
) : RecyclerView.Adapter<Hotel_BookingViewAdapter.BookingViewHolder>() {

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

        // Các trường mới từ XML
        val edtContactInfo: EditText = itemView.findViewById(R.id.edtContactInfo)
        val etVoucher: EditText = itemView.findViewById(R.id.etVoucher)
        val checkboxCash: CheckBox = itemView.findViewById(R.id.checkboxCash)
        val checkboxTransfer: CheckBox = itemView.findViewById(R.id.checkboxTransfer)
        val tvTotalAmount: TextView = itemView.findViewById(R.id.tvTotalAmount)
        val btnBookRoom: Button = itemView.findViewById(R.id.btnBookRoom)
        val btnToggleContact: TextView = itemView.findViewById(R.id.btnToggleContact)
        val btnToggleVoucher: TextView = itemView.findViewById(R.id.btnToggleVoucher)
        val insuranceCheckBox: CheckBox = itemView.findViewById(R.id.insuranceCheckBox) // Checkbox bảo hiểm
        val tvInsurancePrice: TextView = itemView.findViewById(R.id.tvInsurancePrice) // Giá bảo hiểm
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookingViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_hotel_booking, parent, false)
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


        // Liên kết dữ liệu cho các trường mới
        holder.edtContactInfo.setText(booking.contactInfo)
        holder.etVoucher.setText(booking.voucherCode)
        holder.tvTotalAmount.text = booking.totalAmount
        holder.tvInsurancePrice.text = booking.insurancePrice
        holder.insuranceCheckBox.isChecked = booking.isInsuranceSelected

        // Xử lý phương thức thanh toán
        val paymentMethods = booking.paymentMethods.split(",").map { it.trim() }
        holder.checkboxCash.isChecked = paymentMethods.contains("Tiền mặt")
        holder.checkboxTransfer.isChecked = paymentMethods.contains("Chuyển khoản")
        // Xử lý toggle thông tin liên hệ
        holder.btnToggleContact.setOnClickListener {
            holder.edtContactInfo.visibility = if (holder.edtContactInfo.visibility == View.VISIBLE) {
                View.GONE
            } else {
                View.VISIBLE
            }
        }

        // Xử lý toggle voucher
        holder.btnToggleVoucher.setOnClickListener {
            holder.etVoucher.visibility = if (holder.etVoucher.visibility == View.VISIBLE) {
                View.GONE
            } else {
                View.VISIBLE
            }
        }

        // Xử lý nút đặt phòng (có thể thêm logic tại đây)
        holder.btnBookRoom.setOnClickListener {
            // TODO: Thêm logic đặt phòng (ví dụ: gửi dữ liệu lên server)
        }
    }

    override fun getItemCount(): Int = bookingList.size
}