// MyHotelRecyclerViewAdapter.kt
package vn.edu.tdc.bookinghotel.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import vn.edu.tdc.bookinghotel.Model.Hotel
import vn.edu.tdc.bookinghotel.R
import vn.edu.tdc.bookinghotel.databinding.CardRecyclerListHotelBinding
import vn.edu.tdc.bookinghotel.databinding.HomePageLayoutBinding


class MyHotelRecyclerViewAdapter(
    private val context: Context,
    private val list: ArrayList<Hotel>
) : RecyclerView.Adapter<MyHotelRecyclerViewAdapter.MyViewHolder>() {

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

            val imageView = binding.findViewById<ImageView?>(R.id.imageThumb)
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
        val binding = CardRecyclerListHotelBinding.inflate(LayoutInflater.from(context), parent, false)
        return MyViewHolder(binding.root)
    }
    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val hotel = list[position]
        holder.itemPosition = position

        val binding = CardRecyclerListHotelBinding.bind(holder.binding)
        binding.tvThanhPho.text = hotel.thanhpho
        binding.nameHotel.text = hotel.name
        binding.statusHotel.text = hotel.status
        binding.feedback.text = hotel.feedback
        binding.descriptionHotel.text = hotel.description
        binding.imageThumb.setImageResource(hotel.image)

    }
    fun updateData(newList: List<Hotel>) {
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }

    fun getItem(position: Int): Hotel = list[position]



}
