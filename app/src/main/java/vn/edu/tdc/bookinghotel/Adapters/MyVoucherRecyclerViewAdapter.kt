package vn.edu.tdc.bookinghotel.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import vn.edu.tdc.bookinghotel.Model.Hotel
import vn.edu.tdc.bookinghotel.Model.Voucher
import vn.edu.tdc.bookinghotel.R

import vn.edu.tdc.bookinghotel.databinding.CardRecyclerListVoucherBinding

class MyVoucherRecyclerViewAdapter(
    private val context: Context,
    private val list: ArrayList<Voucher>
) : RecyclerView.Adapter<MyVoucherRecyclerViewAdapter.MyViewHolderVoucher>()  {


    private var selectedPosition = -1
    private var onItemClick: OnRecyclerViewItemClickListener? = null

    fun setOnItemClick(listener: OnRecyclerViewItemClickListener) {
        onItemClick = listener
    }

    interface OnRecyclerViewItemClickListener {
        fun onImageClickListener(item: View?, position: Int)
        fun onMyItemClickListener(item: View?, position: Int)
    }

    inner class MyViewHolderVoucher(val binding: View, var itemPosition: Int = 0) : RecyclerView.ViewHolder(binding) {
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolderVoucher {
        val binding = CardRecyclerListVoucherBinding.inflate(LayoutInflater.from(context), parent, false)
        return MyViewHolderVoucher(binding.root)
    }
    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: MyViewHolderVoucher, position: Int) {
        val voucher = list[position]
        holder.itemPosition = position

        val binding = CardRecyclerListVoucherBinding.bind(holder.binding)
        binding.tvTitleVoucher.text = voucher.title
        binding.tvDetailVoucher.text = voucher.detail
        binding.tvCodeVoucher.text = voucher.code
    }


}
