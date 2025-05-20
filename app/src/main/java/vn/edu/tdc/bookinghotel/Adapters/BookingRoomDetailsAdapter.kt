
package vn.edu.tdc.bookinghotel.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import vn.edu.tdc.bookinghotel.Model.Room
import vn.edu.tdc.bookinghotel.R
import java.text.DecimalFormat
import java.math.BigDecimal

class BookingRoomDetailsAdapter(
    private val context: Context,
    private val listRoom: List<Room>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<BookingRoomDetailsAdapter.BookingRoomViewHolder>() {

    interface OnItemClickListener {
        fun onDatClick(position: Int)
    }

    inner class BookingRoomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameDichVu: TextView = itemView.findViewById(R.id.nameDichVu)
        val thongTin1: TextView = itemView.findViewById(R.id.thongTin1)
        val thongTin2: TextView = itemView.findViewById(R.id.thongTin2)
        val hotelDeals: TextView = itemView.findViewById(R.id.hotelDeals)
        val giaTien: TextView = itemView.findViewById(R.id.giaTien)
        val tongGiaTien: TextView = itemView.findViewById(R.id.tongGiaTien)
        val phongConLai: TextView = itemView.findViewById(R.id.phongConLai)
        val btnDat: Button = itemView.findViewById(R.id.btnDat)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookingRoomViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.chi_tiet_phong, parent, false)
        return BookingRoomViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookingRoomViewHolder, position: Int) {
        val room = listRoom[position]

        holder.nameDichVu.text = room.roomType?.name ?: "Phòng không rõ"
        holder.thongTin1.text = room.description ?: "Không có mô tả"

        // Tạo thông tin chi tiết phòng
        holder.thongTin2.text = buildRoomDetails(room)

        holder.hotelDeals.text = when (room.status) {
            "AVAILABLE" -> "Còn phòng"
            "MAINTENANCE" -> "Đang bảo trì"
            else -> "Đã đặt"
        }
        holder.giaTien.text = "${formatCurrency(room.price)} VND/đêm"

        // Tính tổng giá (giả sử 1 đêm)
        val totalPrice = room.price
        holder.tongGiaTien.text = "Tổng: ${formatCurrency(totalPrice)} VND"

        holder.phongConLai.text = "Phòng cho ${room.capacity} người"

        holder.btnDat.setOnClickListener {
            when (room.status) {
                "AVAILABLE" -> listener.onDatClick(position)
                "MAINTENANCE" -> Toast.makeText(context, "Phòng đang bảo trì", Toast.LENGTH_SHORT).show()
                else -> Toast.makeText(context, "Phòng đã có người đặt trước", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun getItemCount(): Int = listRoom.size

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
