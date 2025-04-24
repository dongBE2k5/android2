// MyHotelRecyclerViewAdapter.kt
package vn.edu.tdc.bookinghotel.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView

import vn.edu.tdc.bookinghotel.Model.Room
import vn.edu.tdc.bookinghotel.R
import vn.edu.tdc.bookinghotel.databinding.CardChiTietHotelBinding



class AdapterRoom(
    private val context: Context,
    private val list: ArrayList<Room>
) : RecyclerView.Adapter<AdapterRoom.MyViewHolder>() {

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
        val binding = CardChiTietHotelBinding.inflate(LayoutInflater.from(context), parent, false)
        return MyViewHolder(binding.root)
    }
    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val room = list[position]
        holder.itemPosition = position

        val binding = CardChiTietHotelBinding.bind(holder.binding)
        binding.tvRoomName.text = room.name
        binding.tvRoomStatus.text = room.status
        binding.tvShortDescriptionRoom.text = room.shortdescription
        binding.tvLongDescriptionRoom.text = room.longdescription
        binding.tvTienNghi.text = room.tiennghi
        binding.tvChiTiet.text = room.chitiet
        binding.tvPrice.text = room.price
        binding.imageRoom.setImageResource(room.image)
    }

}
