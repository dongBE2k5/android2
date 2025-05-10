package vn.edu.tdc.bookinghotel.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import vn.edu.tdc.bookinghotel.Model.Hotel

import vn.edu.tdc.bookinghotel.Model.Hotel_Booking
import vn.edu.tdc.bookinghotel.R
import vn.edu.tdc.bookinghotel.databinding.BookingHotelBinding
import vn.edu.tdc.bookinghotel.databinding.CardRecyclerListHotelBinding

class Hotel_BookingViewAdapter(
    private val context: Context,
    private val list: ArrayList<Hotel_Booking>
) : RecyclerView.Adapter<Hotel_BookingViewAdapter.MyViewHolder>() {

    private var selectedPosition = -1
    private var onItemClick: OnRecyclerViewItemClickListener? = null

    fun setOnItemClick(listener: OnRecyclerViewItemClickListener) {
        onItemClick = listener
    }

    interface OnRecyclerViewItemClickListener {
        fun onImageClickListener(item: View?, position: Int)
        fun onMyItemClickListener(item: View?, position: Int)
    }

    inner class MyViewHolder(val binding: View, var itemPosition: Int = 0) : RecyclerView.ViewHolder(binding) {
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

            val imageView = binding.findViewById<ImageView?>(R.id.imageRoom)
            imageView?.setOnClickListener {
                itemPosition = adapterPosition
                selectedPosition = itemPosition
                notifyDataSetChanged()
                onItemClick?.onImageClickListener(binding, itemPosition)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == selectedPosition) 1 else 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = BookingHotelBinding.inflate(LayoutInflater.from(context), parent, false)
        return MyViewHolder(binding.root)
    }
    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val hotelbooking = list[position]
        holder.itemPosition = position

        val binding = BookingHotelBinding.bind(holder.binding)
        binding.imgRoom.setImageResource(hotelbooking.image)
        binding.tvRoomName.text = hotelbooking.roomName
        binding.tvBookingId.text = hotelbooking.bookingId.toString()
        binding.tvCustomerId.text = hotelbooking.customerId.toString()
        binding.tvRoomId.text = hotelbooking.roomId.toString()
        binding.tvCheckInDate.text = hotelbooking.checkInDate
        binding.tvCheckOutDate.text = hotelbooking.checkOutDate
        binding.tvStatus.text = hotelbooking.status
        binding.tvTotalAmount.text = hotelbooking.totalAmount



    }
    fun updateData(newList: List<Hotel_Booking>) {
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }

}
