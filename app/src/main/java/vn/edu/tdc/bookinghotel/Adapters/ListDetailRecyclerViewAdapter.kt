package vn.edu.tdc.bookinghotel.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import vn.edu.tdc.bookinghotel.R

import vn.edu.tdc.bookinghotel.Model.ListDetail
import vn.edu.tdc.bookinghotel.Model.Room
import vn.edu.tdc.bookinghotel.databinding.ChiTietPhongBinding
import java.math.BigDecimal
import java.text.DecimalFormat


class ListDetailRecyclerViewAdapter(val context: Context, val list:ArrayList<Room>) :
RecyclerView.Adapter<ListDetailRecyclerViewAdapter.MyViewHolder>()  {
    // B1. Dinh nghia interface de thuuc hien uy quyen chuc nang
    interface onRecyclerViewItemClickListener {
        //        fun onImageClickListener(item:View?, position:Int);
//        fun onItemClickListener(item:View?, position:Int);
        fun onButtonBookClick(item: View?, position: Int) // 👉 Thêm dòng này
    }
    // B2. Dinh nghia doi tuong interface
    private var onItemClick:onRecyclerViewItemClickListener? = null

    // B3. Dinh nghia ham thiet lap cho co che uy quyen
    fun setOnItemClick(onItemClick: onRecyclerViewItemClickListener) {
        this.onItemClick = onItemClick;
    }



    // Dinh nghia lop ViewHolder cho RecyclerView
//    inner class MyViewHolder(val binding: ListviewItemBinding) : RecyclerView.ViewHolder(binding.root) {
    inner class MyViewHolder(val binding: ChiTietPhongBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ChiTietPhongBinding.inflate(LayoutInflater.from(context), parent, false)
        return MyViewHolder(binding)

    }

    override fun getItemCount(): Int {
        return list.size
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // Do du lieu vao binding
        val room = list.get(position)
        holder.binding.nameDichVu.text = room.roomType!!.name
        holder.binding.thongTin1.text = room.description
//        holder.thongTin2.text = phongs.thongTin2
        holder.binding.hotelDeals.text = room.status

        val formattedGiaTien = formatCurrency (room.price)
        holder.binding.giaTien.text = "$formattedGiaTien VND"
//        holder.binding.roomNumber.text = "Phòng ${room.roomNumber}"
//        holder.binding.capacity.text = "${room.capacity} người"
//        holder.binding.price.text = "${room.price} VND"
//        holder.binding.priceTotal.text = "Tổng giá ${room.price} VND bao gồm thuế và phí"
        holder.binding.btnDat.setOnClickListener {
            onItemClick?.onButtonBookClick(it, position)
        }
    }
    fun formatCurrency(amount: Double): String {
        val format = DecimalFormat("#,###")
        return format.format(amount)
    }


}



