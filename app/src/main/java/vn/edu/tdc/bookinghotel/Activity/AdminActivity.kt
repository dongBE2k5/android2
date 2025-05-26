package vn.edu.tdc.bookinghotel.Activity

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import vn.edu.tdc.bookinghotel.Adapters.AdminRecycleViewAdapter
import vn.edu.tdc.bookinghotel.Model.Booking
import vn.edu.tdc.bookinghotel.Model.UserDatHang
import vn.edu.tdc.bookinghotel.R
import vn.edu.tdc.bookinghotel.Repository.BookingRepository
import vn.edu.tdc.bookinghotel.Repository.RoomRepository
import vn.edu.tdc.bookinghotel.Session.SessionManager
import vn.edu.tdc.bookinghotel.View.BottomNavHelper
import vn.edu.tdc.bookinghotel.databinding.AdminLayoutBinding

class AdminActivity : AppCompatActivity() {

    private lateinit var binding: AdminLayoutBinding
    private lateinit var adapter: AdminRecycleViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AdminLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Fullscreen setup
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.navigationBarColor = Color.TRANSPARENT
            window.statusBarColor = Color.TRANSPARENT
        }
        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                )
        window.setDecorFitsSystemWindows(false)
        window.insetsController?.let { controller ->
            controller.hide(
                android.view.WindowInsets.Type.statusBars() or android.view.WindowInsets.Type.navigationBars()
            )
            controller.systemBarsBehavior = android.view.WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }

        binding.btnThemPhong.setOnClickListener {
            startActivity(Intent(this, AddRoomActivity::class.java))
        }

        val session = SessionManager(this)
        val username = session.getUserName()
        binding.userNameAccount.text = username ?: "Guest"
        val datHangKhachSan = ArrayList<UserDatHang>()
        val bookingRepository = BookingRepository()
        val roomRepository = RoomRepository()
        val idBooking=ArrayList<Long>()
        bookingRepository.getBookingByHotelier(
            token = session.getToken().toString(),
            onSuccess = { bookingList ->
                if (bookingList.isEmpty()){

                }
                idBooking.addAll(bookingList.map { e->e.id })
                var loadedCount: Int = 0

                for (booking in bookingList) {
                    roomRepository.fetchRoomById(
                        roomId = booking.room.id,
                        onSuccess = { room ->

                            val user = UserDatHang(
                                nameUser = booking.customer.fullName,
                                nameKS = room.hotel?.name ?: "Không rõ",
                                tongTien = (booking.price ?: 0L).toInt(),
                                bookingId = booking.id
                            )

                            datHangKhachSan.add(user)
                            loadedCount++
                            if (loadedCount == bookingList.size) {
                                setupUI(datHangKhachSan)
                            }
                        },
                        onError = {
                            loadedCount++
                            if (loadedCount == bookingList.size) {
                                setupUI(datHangKhachSan)
                            }
                        }
                    )
                }
            },
            onError = { error ->
                Log.e("BookingError", error.message ?: "Lỗi gọi booking")
            }
        )

        val selectedItem = intent.getIntExtra("selected_nav", R.id.nav_home)
        BottomNavHelper.setup(this, binding.bottomNav, selectedItem)
    }

    private fun setupUI(data: ArrayList<UserDatHang>) {

        val spinnerItems = arrayListOf("Tất cả")
        spinnerItems.addAll(data.map { it.nameKS }.distinct())

        val adapterSpinner = ArrayAdapter(this, android.R.layout.simple_spinner_item, spinnerItems)
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.citySpinner.adapter = adapterSpinner
        binding.citySpinner.setSelection(0)

        adapter = AdminRecycleViewAdapter(this, data)
        binding.recycleKsDaDat.layoutManager = LinearLayoutManager(this)
        binding.recycleKsDaDat.adapter = adapter

        binding.citySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selected = spinnerItems[position]
                val filtered = if (selected == "Tất cả") data else data.filter { it.nameKS == selected }
                adapter.updateData(filtered)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        adapter.setOnItemClick(object : AdminRecycleViewAdapter.OnRecyclerViewItemClickListener {
            override fun onImageClickListener(item: View?, position: Int) {}
            override fun onMyItemClickListener(item: View?, position: Int) {
                val selected = data[position]
                val intent = Intent(this@AdminActivity, AdminQLDatKS::class.java)
                intent.putExtra("idBooking",selected.bookingId)
                startActivity(intent)
            }
        })
    }
}
