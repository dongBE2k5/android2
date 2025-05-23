package vn.edu.tdc.bookinghotel.Activity

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import vn.edu.tdc.bookinghotel.Adapters.ChiTietPhongRecyclerViewAdapter
import vn.edu.tdc.bookinghotel.Adapters.ListDetailRecyclerViewAdapter
import vn.edu.tdc.bookinghotel.Model.Hotel
import vn.edu.tdc.bookinghotel.Model.Hotel_Booking
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
        setContentView(binding.root)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.setDecorFitsSystemWindows(false) // Cho phép layout vẽ tràn lên status/navigation
            window.insetsController?.apply {
                hide(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
                systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        } else {
            @Suppress("DEPRECATION")
            window.decorView.systemUiVisibility = (
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_FULLSCREEN
                            or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    )
        }

        binding.btnback.setOnClickListener {
            val intent= Intent(this,MainActivity::class.java)
            startActivity(intent)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }



        // Nhận dữ liệu từ Intent
        val hotelName = intent.getStringExtra("hotel_name")
        val hotelId = intent.getLongExtra("hotel_id", 0L)
        val hotelImage = intent.getStringExtra("hotel_image")
        Log.d("image hotels",hotelImage.toString())
        Log.d("Requet image hotels",getString(R.string.localUpload))

        binding.tvTenKhachSan.text = hotelName ?: "Tên khách sạn không có"

        Glide.with(this)
            .load("${getString(R.string.localUpload)}${hotelImage}")
            .placeholder(R.drawable.khachsan)
            .error(R.drawable.ic_launcher_background)
            .into(binding.imgHotel)
        Log.d("ID hotels", hotelId.toString())
        val repositoryRoom= RoomRepository()
        var rooms = ArrayList<Room>()
        repositoryRoom.fetchRoomByHotel(
            hotelId=hotelId,
            onSuccess = {roomList->
                rooms.clear()
                rooms.addAll(roomList)
                val detailPhong = arrayListOf(ListDetail("Danh sách phòng", rooms))
                Log.d("List room", rooms.toString())

                // Gán adapter cho RecyclerView và truyền listener
                val recyclerViewListDetail = findViewById<RecyclerView>(R.id.recyclerViewListDetail)
//        recyclerViewListDetail.layoutManager = LinearLayoutManager(this)
                recyclerViewListDetail.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
                adapterListDetail = ListDetailRecyclerViewAdapter(this, rooms) // Truyền this vào
                recyclerViewListDetail.adapter = adapterListDetail

                adapterListDetail.setOnItemClick(object : ListDetailRecyclerViewAdapter.onRecyclerViewItemClickListener {
                    override fun onButtonBookClick(item: View?, position: Int) {

//                         val intent = Intent(this@ChiTietKhachSan, ChiTietPhongActivity::class.java)

                        val roomSelected = rooms[position];
                        Log.d("IdRoom" , "${roomSelected.id}")


                        val intent = Intent(this@ChiTietKhachSan, ChiTietPhongActivity::class.java)

                        intent.putExtra("roomId", roomSelected.id)
                        Log.d("roomIdgui",roomSelected.id.toString())
                        intent.putExtra("roomImage", roomSelected.image)


                        val selectedItem = intent.getIntExtra("selected_nav", R.id.nav_store)
                        startActivity(intent)
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)

                    }
                    override fun onButtonViewClick(item: View?, position: Int) {


                        val roomSelected = rooms[position];
                        Log.d("IdRoom" , "${roomSelected.id}")


                        val intent = Intent(this@ChiTietKhachSan, Hotel_BookingActivity::class.java)
                        intent.putExtra("selected_nav", R.id.nav_store)
                        intent.putExtra("roomId", "${roomSelected.id}")
                        intent.putExtra("roomImage", roomSelected.image)



                        val selectedItem = intent.getIntExtra("selected_nav", R.id.nav_store)
                        startActivity(intent)
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)

                        // Xử lý sự kiện nút "Xem"
                        val room = adapterListDetail.list[position]
                        // Thực hiện hành động khi nhấn nút "Xem", ví dụ: mở chi tiết phòng
                    }

                })
            },
            onError = { error ->
                Log.e("API Room error", "Error: ${error.message}")
            }
        )


    }

}
