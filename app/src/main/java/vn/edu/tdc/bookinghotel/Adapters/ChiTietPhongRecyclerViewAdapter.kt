package vn.edu.tdc.bookinghotel.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

import vn.edu.tdc.bookinghotel.R
import vn.edu.tdc.bookinghotel.fragment.ChiTietPhong
import java.text.DecimalFormat

class ChiTietPhongRecyclerViewAdapter(
    private val listChiTietPhong: List<ChiTietPhong>,
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
        val maVoucher: TextView = itemView.findViewById(R.id.maVoucher)
        val btnDat: TextView = itemView.findViewById(R.id.btnDat) // btnDat
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChiTietPhongViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.chi_tiet_phong, parent, false)
        return ChiTietPhongViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChiTietPhongViewHolder, position: Int) {
        val phongs = listChiTietPhong[position]

        // Liên kết dữ liệu cho các trường cũ
        holder.nameDichVu.text = phongs.nameDichVu
        holder.thongTin1.text = phongs.thongTin1
        holder.thongTin2.text = phongs.thongTin2
        holder.hotelDeals.text = phongs.hotelDeals
        val formattedGiaTien = formatCurrency(phongs.giaTien)
        holder.giaTien.text = "$formattedGiaTien VND"

        // Định dạng tổng giá tiền
        val formattedTongGiaTien = formatCurrency(phongs.tongGiaTien)
        holder.tongGiaTien.text = "Tổng giá $formattedTongGiaTien VND bao gồm thuế và phí"
        holder.phongConLai.text = "Chỉ còn ${phongs.phongConLai} phòng"
        holder.maVoucher.text = "Mã ${phongs.maVoucher} giảm đến 1 triệu"

        // Xử lý click cho btnDat
        holder.btnDat.setOnClickListener {
            listener.onDatClick(position)
        }
    }

    override fun getItemCount(): Int {
        return listChiTietPhong.size
    }

    fun formatCurrency(amount: Int): String {
        val format = DecimalFormat("#,###")
        return format.format(amount)
    }
}
