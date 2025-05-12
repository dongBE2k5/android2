package vn.edu.tdc.bookinghotel.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import vn.edu.tdc.bookinghotel.Model.UserDatHang


import vn.edu.tdc.bookinghotel.databinding.CardRecyclerListVoucherBinding
import vn.edu.tdc.bookinghotel.databinding.CardRecycleviewQuanLyNguoiDungBinding
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.Locale

class AdminRecycleViewAdapter(
    private val context: Context,
    private val list: ArrayList<UserDatHang>
) : RecyclerView.Adapter<AdminRecycleViewAdapter.MyViewHolderUserDatHang>()  {


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
        val binding = CardRecycleviewQuanLyNguoiDungBinding.inflate(LayoutInflater.from(context), parent, false)
        return MyViewHolderUserDatHang(binding.root)
    }
    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: MyViewHolderUserDatHang, position: Int) {
        val datHang = list[position]
        holder.itemPosition = position

        val binding = CardRecycleviewQuanLyNguoiDungBinding.bind(holder.binding)
        binding.userDatHang.text = datHang.nameUser
        binding.nameKSDatHang.text = datHang.nameKS
        binding.tongGiaTienDatHang.text = "Tổng: ${formatCurrency(datHang.tongTien)}"
    }

    fun formatCurrency(amount: Int): String {
        val format = DecimalFormat("#,###")
        return format.format(amount)
    }

}
