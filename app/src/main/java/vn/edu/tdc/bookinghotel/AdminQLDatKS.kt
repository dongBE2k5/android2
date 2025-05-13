package vn.edu.tdc.bookinghotel

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import vn.edu.tdc.bookinghotel.Activity.AcountActivity
import vn.edu.tdc.bookinghotel.Activity.AdminActivity
import vn.edu.tdc.bookinghotel.Activity.MainActivity
import vn.edu.tdc.bookinghotel.Activity.StoreActivity
import vn.edu.tdc.bookinghotel.Adapters.ChiTietDatHangAdminRecyclerViewAdapter
import vn.edu.tdc.bookinghotel.Model.ChiTietDatHangAdmin
import vn.edu.tdc.bookinghotel.databinding.AdminChiTietDatHangBinding

class AdminQLDatKS: AppCompatActivity() {

    private lateinit var binding: AdminChiTietDatHangBinding
    private lateinit var adapter: ChiTietDatHangAdminRecyclerViewAdapter

    private val chiTietDatKS = arrayListOf(
        ChiTietDatHangAdmin(
            bookingId = 1,
            userName = "Nguyen Van A",
            roomId = 101,
            tongTien = 1500000,
            checkInDate = "2025-06-01",
            checkOutDate = "2025-06-05",
            status = "Đã xác nhận",
            roomName = "Phòng Deluxe",
            imageUrl = R.drawable.khachsan
        )
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        binding = AdminChiTietDatHangBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        //full màn hiình
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


        // RecyclerViews: nguoi dung dat hang
        val recyclerViewChiTietKSDaDat = findViewById<RecyclerView>(R.id.recycleChiTietKsDaDat)
        recyclerViewChiTietKSDaDat.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        adapter = ChiTietDatHangAdminRecyclerViewAdapter(this, chiTietDatKS)
        recyclerViewChiTietKSDaDat.adapter = adapter

        // Bottom Navigation xử lý chuyển activity
        val selectedItem = intent.getIntExtra("selected_nav", R.id.nav_admin)
        binding.bottomNav.selectedItemId = selectedItem
        binding.bottomNav.setOnItemSelectedListener { item ->
            if (item.itemId != selectedItem) {
                val intent = when (item.itemId) {
                    R.id.nav_home -> Intent(this, MainActivity::class.java)
                    R.id.nav_store -> Intent(this, StoreActivity::class.java)
                    R.id.nav_profile -> Intent(this, AcountActivity::class.java)
                    R.id.nav_admin -> Intent(this, AdminActivity::class.java)

                    else -> null
                }
                intent?.let {
                    it.putExtra("selected_nav", item.itemId)
                    startActivity(it)
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                    finish()
                }
                true
            } else {
                true
            }
        }

    }
}