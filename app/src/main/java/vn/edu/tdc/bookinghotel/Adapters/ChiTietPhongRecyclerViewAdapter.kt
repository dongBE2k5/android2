package vn.edu.tdc.bookinghotel.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

import vn.edu.tdc.bookinghotel.R
import vn.edu.tdc.bookinghotel.Model.Room
import java.math.BigDecimal
import java.text.DecimalFormat

class ChiTietPhongRecyclerViewAdapter(
    private val context: Context,
    private val listRoom: List<Room>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<ChiTietPhongRecyclerViewAdapter.ChiTietPhongViewHolder>() {

    interface OnItemClickListener {
        fun onDatClick(position: Int)
    }

    inner class ChiTietPhongViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Các trường cũ
        val nameDichVu: TextView = itemView.findViewById(R.id.nameDichVu)
        val thongTin1: TextView = itemView.findViewById(R.id.thongTin1)
        val thongTin2: TextView = itemView.findViewById(R.id.thongTin2)
        val hotelDeals: TextView = itemView.findViewById(R.id.hotelDeals)
        val giaTien: TextView = itemView.findViewById(R.id.giaTien)
        val tongGiaTien: TextView = itemView.findViewById(R.id.tongGiaTien)
        val phongConLai: TextView = itemView.findViewById(R.id.phongConLai)
//        val maVoucher: TextView = itemView.findViewById(R.id.maVoucher)
        val btnDat: TextView = itemView.findViewById(R.id.btnDat) // btnDat
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChiTietPhongViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.chi_tiet_phong, parent, false)
        return ChiTietPhongViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChiTietPhongViewHolder, position: Int) {
        val phongs = listRoom[position]

        // Liên kết dữ liệu cho các trường cũ
        holder.nameDichVu.text = phongs.roomType!!.name
        holder.thongTin1.text = phongs.description
        if (phongs.roomType.id.equals(1L)){
            holder.thongTin2.text = "Có bàn làm việc và Wi-Fi miễn phí,diện tích 20m²"
        }
        else if (phongs.roomType.id.equals(2L)){
            holder.thongTin2.text = "Bao gồm TV, bàn làm việc và Wi-Fi,diện tích 28m²"

        }
        else{
            holder.thongTin2.text = "Có sofa, bàn ăn và tiện nghi đầy đủ,diện tích 40m²"
        }

        holder.hotelDeals.text = phongs.status
        val formattedGiaTien = formatCurrency(phongs.price)
        holder.giaTien.text = "$formattedGiaTien VND/đêm "

        // Định dạng tổng giá tiền
//        val formattedTongGiaTien = formatCurrency(phongs.tongGiaTien)
//        holder.tongGiaTien.text = "Tổng giá $formattedTongGiaTien VND bao gồm thuế và phí"
        holder.tongGiaTien.text = ""
        holder.phongConLai.text = "Phòng ${phongs.capacity} người"
//        holder.maVoucher.text = "Mã ${phongs.maVoucher} giảm đến 1 triệu"

        // Xử lý click cho btnDat
        holder.btnDat.setOnClickListener {
            if (phongs.status == "AVAILABLE") {
                listener.onDatClick(position)
            }
            else if (phongs.status == "MAINTENANCE"){
                Toast.makeText(context,"Phòng đang bảo trì ",Toast.LENGTH_SHORT).show()
            }

            else {
                Toast.makeText(context,"Phòng đã có người đặt trước",Toast.LENGTH_SHORT).show()
            }

        }
    }

    override fun getItemCount(): Int {
        return listRoom.size
    }

    fun formatCurrency(amount: Double): String {
        val format = DecimalFormat("#,###")
        return format.format(amount)
    }
}
