package vn.edu.tdc.bookinghotel.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import vn.edu.tdc.bookinghotel.Model.Room
import vn.edu.tdc.bookinghotel.R
import java.math.BigDecimal
import java.text.DecimalFormat

class BookingRoomDetailsAdapter(
    private val context: Context,
    private val room: Room,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<BookingRoomDetailsAdapter.RoomViewHolder>() {

    interface OnItemClickListener {
        fun onDatClick()
    }

    inner class RoomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val roomImage: ImageView = itemView.findViewById(R.id.roomImage)
        val nameDichVu: TextView = itemView.findViewById(R.id.nameDichVu)
        val thongTin1: TextView = itemView.findViewById(R.id.thongTin1)
        val thongTin2: TextView = itemView.findViewById(R.id.thongTin2)
        val hotelDeals: TextView = itemView.findViewById(R.id.hotelDeals)
        val giaTien: TextView = itemView.findViewById(R.id.giaTien)
        val phongConLai: TextView = itemView.findViewById(R.id.phongConLai)
        val tongGiaTien: TextView = itemView.findViewById(R.id.tongGiaTien)
        val btnDat: Button = itemView.findViewById(R.id.btnDat)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.chi_tiet_phong, parent, false)
        return RoomViewHolder(view)
    }

    override fun onBindViewHolder(holder: RoomViewHolder, position: Int) {
        // Load ảnh phòng (nếu có URL hoặc resource)
        Glide.with(context)
            .load(room.image)  // room.image cần là URL hoặc resource phù hợp
            .placeholder(R.drawable.khachsan)
            .error(R.drawable.ic_launcher_background)
            .into(holder.roomImage)

        holder.nameDichVu.text = room.roomType?.name ?: "Phòng không rõ"
        holder.thongTin1.text = room.description ?: "Không có mô tả"
        holder.thongTin2.text = buildRoomDetails(room)

        holder.hotelDeals.text = when (room.status) {
            "AVAILABLE" -> "Còn phòng"
            "MAINTENANCE" -> "Đang bảo trì"
            else -> "Đã đặt"
        }

        holder.giaTien.text = "${formatCurrency(room.price)} VND/đêm"
        holder.tongGiaTien.text = "Tổng giá: ${formatCurrency(room.price)} VND"
        holder.phongConLai.text = "Phòng cho ${room.capacity} người"

        holder.btnDat.setOnClickListener {
            when (room.status) {
                "AVAILABLE" -> listener.onDatClick()
                "MAINTENANCE" -> Toast.makeText(context, "Phòng đang bảo trì", Toast.LENGTH_SHORT).show()
                else -> Toast.makeText(context, "Phòng đã được đặt trước", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun getItemCount(): Int = 1

    private fun formatCurrency(amount: BigDecimal?): String {
        val format = DecimalFormat("#,###")
        return format.format(amount ?: BigDecimal.ZERO)
    }

    private fun buildRoomDetails(room: Room): String {
        val details = mutableListOf<String>()
        room.area?.let { details.add("Diện tích ${String.format("%.1f", it)}m²") }
        room.amenities?.let { details.addAll(it) }
        return if (details.isNotEmpty()) details.joinToString(", ") else "Không có thông tin chi tiết"
    }
}
