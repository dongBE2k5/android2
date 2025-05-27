package vn.edu.tdc.bookinghotel.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat

import androidx.recyclerview.widget.RecyclerView

import vn.edu.tdc.bookinghotel.Model.Room
import vn.edu.tdc.bookinghotel.R
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
        fun onButtonViewClick(item: View?, position: Int) // Thêm phương thức này

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


//     override fun onBindViewHolder(holder: ListDetailViewHolder, position: Int) {
//         val listDetails = listDetail[position]
//         holder.tvPhonCoSan.text = listDetails.tvPhongCoSan
//         val context = holder.itemView.context
//         // Khởi tạo adapter con và truyền listener vào
//         val adapterChiTietPhong = ChiTietPhongRecyclerViewAdapter(context,listDetails.danhSachPhong, listener)
//         holder.recyclerViewChiTietPhong.layoutManager = LinearLayoutManager(holder.itemView.context)
//         holder.recyclerViewChiTietPhong.adapter = adapterChiTietPhong


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val room = list[position]

        holder.binding.nameDichVu.text = room.roomType?.name
        holder.binding.thongTin1.text = room.description
        holder.binding.phongConLai.text="Phòng "+room.capacity.toString()+" người"
        // Map mã trạng thái sang tiếng Việt
        val statusMapReverse = mapOf(
            "AVAILABLE" to "Trống",
            "RESERVED" to "Đã đặt trước",
            "OCCUPIED" to "Đã thuê",
            "MAINTENANCE" to "Đang bảo trì"
        )
        val statusDisplay = statusMapReverse[room.status] ?: room.status
        holder.binding.hotelDeals.text = statusDisplay

        val formattedGiaTien = formatCurrency(room.price)
        holder.binding.giaTien.text = "$formattedGiaTien VND"

        holder.binding.btnDat.setOnClickListener {
            onItemClick?.onButtonBookClick(it, position)
        }

        holder.binding.btnXem.setOnClickListener {
            onItemClick?.onButtonViewClick(it, position)
        }

        // Đặt màu nền theo trạng thái
        holder.binding.hotelDeals.setBackgroundColor(
            ContextCompat.getColor(
                context,
                when (room.status) {
                    "AVAILABLE" -> R.color.available_green
                    "RESERVED" -> R.color.reserved_orange     // 🔸 Màu riêng cho RESERVED
                    "OCCUPIED" -> R.color.booked_yellow       // Vẫn dùng màu vàng
                    "MAINTENANCE" -> R.color.maintenance_red
                    else -> R.color.gray                      // Mặc định nếu không khớp
                }
            )
        )
    }


    fun formatCurrency(amount: BigDecimal): String {
        val format = DecimalFormat("#,###")
        return format.format(amount)

    }


}



