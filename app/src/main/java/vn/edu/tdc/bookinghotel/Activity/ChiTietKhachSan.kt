package vn.edu.tdc.bookinghotel.Activity

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import vn.edu.tdc.bookinghotel.Adapters.ChiTietPhongRecyclerViewAdapter
import vn.edu.tdc.bookinghotel.Adapters.ListDetailRecyclerViewAdapter
import vn.edu.tdc.bookinghotel.Model.Hotel
import vn.edu.tdc.bookinghotel.R
import vn.edu.tdc.bookinghotel.databinding.DetailRoomBinding
import vn.edu.tdc.bookinghotel.Model.Room
import vn.edu.tdc.bookinghotel.Model.ListDetail
import vn.edu.tdc.bookinghotel.Repository.HotelRepository
import vn.edu.tdc.bookinghotel.Repository.RoomRepository

class ChiTietKhachSan : AppCompatActivity() {

    private lateinit var binding: DetailRoomBinding
    private lateinit var adapterListDetail: ListDetailRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DetailRoomBinding.inflate(layoutInflater)
        // Full màn hình
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.navigationBarColor = Color.TRANSPARENT
            window.statusBarColor = Color.TRANSPARENT
        }
        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                )
        setContentView(binding.root)

        // Nhận dữ liệu từ Intent
        val hotelName = intent.getStringExtra("hotel_name")
        val hotelId = intent.getLongExtra("hotel_id", 0L)


        binding.tvTenKhachSan.text = hotelName ?: "Tên khách sạn không có"


        Log.d("ID hotels", hotelId.toString())
        val repositoryRoom= RoomRepository()
        var rooms = ArrayList<Room>()
        repositoryRoom.fetchRoomByHotel(
            hotelId=hotelId,
            onSuccess = {roomList->
                rooms.clear()
                rooms.addAll(roomList)
                val detailPhong = arrayListOf(ListDetail("Phòng có sẵn", rooms))
                Log.d("List room", rooms.toString())

                // Gán adapter cho RecyclerView và truyền listener
                val recyclerViewListDetail = findViewById<RecyclerView>(R.id.recyclerViewListDetail)
//        recyclerViewListDetail.layoutManager = LinearLayoutManager(this)
                recyclerViewListDetail.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
                adapterListDetail = ListDetailRecyclerViewAdapter(this, rooms) // Truyền this vào
                recyclerViewListDetail.adapter = adapterListDetail

                adapterListDetail.setOnItemClick(object : ListDetailRecyclerViewAdapter.onRecyclerViewItemClickListener {
                    override fun onButtonBookClick(item: View?, position: Int) {
                        val intent = Intent(this@ChiTietKhachSan, Hotel_BookingActivity::class.java)
                        val selectedItem = intent.getIntExtra("selected_nav", R.id.nav_store)
                        startActivity(intent)
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                    }

                })
            },
            onError = { error ->
                Log.e("API Room error", "Error: ${error.message}")
            }
        )


    }

}
