// MyHotelRecyclerViewAdapter.kt
package vn.edu.tdc.bookinghotel.Adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import vn.edu.tdc.bookinghotel.Model.Hotel
import vn.edu.tdc.bookinghotel.Model.Room
import vn.edu.tdc.bookinghotel.R
import vn.edu.tdc.bookinghotel.Repository.RoomRepository
import vn.edu.tdc.bookinghotel.databinding.CardRecyclerListHotelBinding
import vn.edu.tdc.bookinghotel.databinding.HomePageLayoutBinding


private const val s = "192.168.1.4:8080/upload/"

class MyHotelRecyclerViewAdapter(
    private val context: Context,
    private val list: List<Hotel>,
    private val locationName: String,
    private val roomCountMap: Map<Long, Int> = emptyMap()
) : RecyclerView.Adapter<MyHotelRecyclerViewAdapter.MyViewHolder>() {

    private var listener: OnRecyclerViewItemClickListener? = null

    interface OnRecyclerViewItemClickListener {
        fun onImageClickListener(item: View?, position: Int)
        fun onMyItemClickListener(item: View?, position: Int)
    }

    fun setOnItemClick(listener: OnRecyclerViewItemClickListener) {
        this.listener = listener
    }

    inner class MyViewHolder(val binding: CardRecyclerListHotelBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.imageThumb.setOnClickListener {
                listener?.onImageClickListener(it, adapterPosition)
            }
            binding.root.setOnClickListener {
                listener?.onMyItemClickListener(it, adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = CardRecyclerListHotelBinding.inflate(LayoutInflater.from(context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val hotel = list[position]
        val binding = holder.binding

        binding.tvThanhPho.text = locationName
        binding.nameHotel.text = hotel.name
        binding.statusHotel.text = if (hotel.status == "OPEN") "Còn phòng" else "Hết phòng"
        binding.feedback.text = hotel.name
        binding.descriptionHotel.text = hotel.address

        Glide.with(context)
            .load("${context.getString(R.string.localUpload)}${hotel.image}")
            .placeholder(R.drawable.khachsan)
            .error(R.drawable.ic_launcher_background)
            .into(binding.imageThumb)

        // Hiển thị số phòng
        val roomCount = roomCountMap[hotel.id] ?: 0
        binding.soPhong.text = roomCount.toString()
    }

    override fun getItemCount(): Int = list.size

    fun getItem(position: Int): Hotel = list[position]
}

