package vn.edu.tdc.bookinghotel

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import vn.edu.tdc.bookinghotel.Adapters.AdapterRoom
import vn.edu.tdc.bookinghotel.Model.Room
import vn.edu.tdc.bookinghotel.databinding.ChiTietHotelListBinding

class RoomActivity : AppCompatActivity() {
    private lateinit var binding:ChiTietHotelListBinding
    private lateinit var listRoom: ArrayList<Room>
    private lateinit var adapter: AdapterRoom

    private var itemSelected = -1
    private var oldColor: Int = 0
    private lateinit var oldView: View


    val rooms = arrayListOf(
        Room("Khách sạn A", "Còn phòng", "Gần trung tâm, tiện nghi đầy đủ","Phòng rộng rãi, có ban công nhìn ra biển, phòng tắm riêng, điều hòa, minibar, wifi miễn phí...","Tiện nghi:","• Wifi miễn phí • Hồ bơi • Nhà hàng • Bãi đỗ xe • Dịch vụ đưa đón sân bay","Giá: 1.200.000 VNĐ / đêm", R.drawable.baseline_co_present_24),
        Room("Khách sạn B", "Còn phòng", "Gần trung tâm, tiện nghi đầy đủ","","","","", R.drawable.baseline_co_present_24),
        Room("Khách sạn C", "Còn phòng", "Gần trung tâm, tiện nghi đầy đủ","","","","", R.drawable.baseline_co_present_24),
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        binding=ChiTietHotelListBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)

        setContentView(binding.root)

        val recyclerView = findViewById<RecyclerView>(R.id.listRoom)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val adapter = AdapterRoom(this, rooms)
        recyclerView.adapter = adapter

        adapter.setOnItemClick(object : AdapterRoom.OnRecyclerViewItemClickListener {
            override fun onImageClickListener(item: View?, position: Int) {
                Toast.makeText(this@RoomActivity, "Ảnh: ${rooms[position].name}", Toast.LENGTH_SHORT).show()
            }

            override fun onMyItemClickListener(item: View?, position: Int) {
                Toast.makeText(this@RoomActivity, "Item: ${rooms[position].name}", Toast.LENGTH_SHORT).show()
            }
        })

    }
}
