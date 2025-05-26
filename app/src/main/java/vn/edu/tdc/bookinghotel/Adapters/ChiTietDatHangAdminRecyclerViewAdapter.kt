package vn.edu.tdc.bookinghotel.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import vn.edu.tdc.bookinghotel.Model.ChiTietDatHangAdmin
import vn.edu.tdc.bookinghotel.R


import vn.edu.tdc.bookinghotel.databinding.ChiTietKhachSanDaDatRecyleviewBinding
import java.math.BigDecimal
import java.text.DecimalFormat

class ChiTietDatHangAdminRecyclerViewAdapter(
    private val context: Context,
        private val list: ArrayList<ChiTietDatHangAdmin>
) : RecyclerView.Adapter<ChiTietDatHangAdminRecyclerViewAdapter.MyViewHolderUserDatHang>()  {


    private var selectedPosition = -1
    private var onItemClick: OnRecyclerViewItemClickListener? = null

    fun setOnItemClick(listener: OnRecyclerViewItemClickListener) {
        onItemClick = listener
    }

    interface OnRecyclerViewItemClickListener {
        fun onImageClickListener(item: View?, position: Int)
        fun onMyItemClickListener(item: View?, position: Int)
    }

    inner class MyViewHolderUserDatHang(val binding: View, var itemPosition: Int = 0) : RecyclerView.ViewHolder(binding) {
        init {
            binding.setOnClickListener {
                if (adapterPosition == RecyclerView.NO_POSITION) return@setOnClickListener
                itemPosition = adapterPosition

                if (selectedPosition == itemPosition) {
                    selectedPosition = -1
                } else {
                    selectedPosition = itemPosition
                }
                notifyDataSetChanged()
                onItemClick?.onMyItemClickListener(binding, itemPosition)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == selectedPosition) 1 else 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolderUserDatHang {
        val binding = ChiTietKhachSanDaDatRecyleviewBinding.inflate(LayoutInflater.from(context), parent, false)
        return MyViewHolderUserDatHang(binding.root)
    }
    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: MyViewHolderUserDatHang, position: Int) {
        val datHang = list[position]
        holder.itemPosition = position

        val binding = ChiTietKhachSanDaDatRecyleviewBinding.bind(holder.binding)
        binding.tvBookingId.text = "Booking ID: ${datHang.bookingId}"
        binding.tvTongTien.text = "Price: ${datHang.tongTien} VND"
        binding.tvCustomerId.text = "CustomerId: ${datHang.customerId}"
        binding.tvRoomId.text = "Room ID: ${datHang.roomId}"
        binding.tvCheckInDate.text = "Check-In: ${datHang.checkInDate}"
        binding.tvCheckOutDate.text = "Check-Out: ${datHang.checkOutDate}"
        binding.tvStatus.text = "Status: ${datHang.status}"
        binding.tvRoomName.text = datHang.roomName
        binding.imgRoom.setImageResource(R.drawable.khachsan)
        binding.tvUserDatHang.text="Username: ${datHang.userName}"

    }

    fun formatCurrency(amount: BigDecimal): String {
        val format = DecimalFormat("#,###")
        return format.format(amount)
    }

}
