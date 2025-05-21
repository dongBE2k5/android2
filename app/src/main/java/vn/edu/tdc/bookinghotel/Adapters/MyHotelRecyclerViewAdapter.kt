// MyHotelRecyclerViewAdapter.kt
package vn.edu.tdc.bookinghotel.Adapters

import android.content.Context
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
    private val list: ArrayList<Hotel>,
    private val locationName: String
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

        // Cập nhật thông tin khách sạn
        binding.tvThanhPho.text = locationName
        binding.nameHotel.text = hotel.name
        binding.statusHotel.text = if (hotel.status == "OPEN") "Còn phòng" else "Hết phòng"
        binding.feedback.text = hotel.name
        binding.descriptionHotel.text = hotel.address

        // Load ảnh khách sạn
        Glide.with(holder.itemView.context)
            .load("${context.getString(R.string.localUpload)}${hotel.image}")
            .placeholder(R.drawable.khachsan)
            .error(R.drawable.ic_launcher_background)
            .into(binding.imageThumb)

        // Gọi fetchRoomByHotel 1 lần duy nhất và thêm sortedRooms tại đây
        val repositoryRoom = RoomRepository()

        repositoryRoom.fetchRoomByHotel(
            hotelId = hotel.id,
            onSuccess = { roomList ->
                // 👉 Sắp xếp phòng theo số phòng (soPhong) tăng dần
                val sortedRooms = roomList.sortedBy { it.soPhong }

                // Sau khi sắp xếp, cập nhật giao diện
                binding.soPhong.text = "${sortedRooms.size}" // Hiển thị số lượng phòng
            },
            onError = {
                binding.soPhong.text = "Lỗi tải phòng" // Thông báo lỗi nếu không tải được phòng
            }
        )
    }


    fun updateData(newList: List<Hotel>) {
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }

    fun getItem(position: Int): Hotel = list[position]



}
