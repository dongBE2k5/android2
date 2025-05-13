package vn.edu.tdc.bookinghotel.Activity

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import vn.edu.tdc.bookinghotel.Adapters.AdminRecycleViewAdapter
import vn.edu.tdc.bookinghotel.AdminQLDatKS
import vn.edu.tdc.bookinghotel.Model.UserDatHang
import vn.edu.tdc.bookinghotel.R
import vn.edu.tdc.bookinghotel.databinding.AdminLayoutBinding

class AdminActivity: AppCompatActivity() {

    private lateinit var binding: AdminLayoutBinding
    private lateinit var adapter: AdminRecycleViewAdapter

    private val datHangKhachSan = arrayListOf(
        UserDatHang("Nguyễn Văn A", "Khách sạn Grand Riverside", 4000000),
        UserDatHang("Trần Thị B", "Khách sạn Biển Xanh", 2500000),
        UserDatHang("Lê Văn C", "Khách sạn Sài Gòn Center", 3700000),
        UserDatHang("Phạm Thị D", "Khách sạn The Light", 5200000),
        UserDatHang("Hoàng Văn E", "Khách sạn Royal View", 3100000)
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        binding = AdminLayoutBinding.inflate(layoutInflater)
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


        window.setDecorFitsSystemWindows(false)

        window.insetsController?.let { controller ->
            controller.hide(
                android.view.WindowInsets.Type.statusBars() or android.view.WindowInsets.Type.navigationBars()
            )
            controller.systemBarsBehavior = android.view.WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }

        // RecyclerViews: nguoi dung dat hang
        val recyclerViewKSDaDat = findViewById<RecyclerView>(R.id.recycleKsDaDat)
        recyclerViewKSDaDat.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        adapter = AdminRecycleViewAdapter(this, datHangKhachSan)
        recyclerViewKSDaDat.adapter = adapter


        adapter.setOnItemClick(object : AdminRecycleViewAdapter.OnRecyclerViewItemClickListener {
            override fun onImageClickListener(item: View?, position: Int) {
                // không cần thiết ở đây
            }

            override fun onMyItemClickListener(item: View?, position: Int) {
                val intent = Intent(this@AdminActivity, AdminQLDatKS::class.java)
                startActivity(intent)
            }
        })


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