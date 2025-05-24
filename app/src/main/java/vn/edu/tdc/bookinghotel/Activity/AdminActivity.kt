package vn.edu.tdc.bookinghotel.Activity

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import vn.edu.tdc.bookinghotel.Adapters.AdminRecycleViewAdapter
import vn.edu.tdc.bookinghotel.Model.UserDatHang
import vn.edu.tdc.bookinghotel.R
import vn.edu.tdc.bookinghotel.View.BottomNavHelper
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
        super.onCreate(savedInstanceState)
        binding = AdminLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Full màn hình và ẩn thanh status/navigation bar
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
            val intent= Intent(this,AddRoomActivity::class.java)
            startActivity(intent)
        }


        // Khởi tạo Spinner lọc theo tên khách sạn
        val hotelNames = datHangKhachSan.map { it.nameKS }.distinct()
        val spinnerItems = arrayListOf("Tất cả")
        spinnerItems.addAll(hotelNames)

        val adapterSpinner = ArrayAdapter(this, android.R.layout.simple_spinner_item, spinnerItems)
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.citySpinner.adapter = adapterSpinner
        binding.citySpinner.setSelection(0)

        // Khởi tạo RecyclerView
        binding.recycleKsDaDat.layoutManager = LinearLayoutManager(this)
        adapter = AdminRecycleViewAdapter(this, datHangKhachSan)
        binding.recycleKsDaDat.adapter = adapter

        // Lắng nghe sự kiện chọn Spinner để lọc dữ liệu
        binding.citySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedHotel = spinnerItems[position]
                val filteredList = if (selectedHotel == "Tất cả") {
                    datHangKhachSan
                } else {
                    datHangKhachSan.filter { it.nameKS == selectedHotel }
                }
                adapter.updateData(filteredList)
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        adapter.setOnItemClick(object : AdminRecycleViewAdapter.OnRecyclerViewItemClickListener {
            override fun onImageClickListener(item: View?, position: Int) {
                // Không cần thiết ở đây
            }

            override fun onMyItemClickListener(item: View?, position: Int) {
                val intent = Intent(this@AdminActivity, AdminQLDatKS::class.java)
                startActivity(intent)
            }
        })

        // Bottom Navigation xử lý chuyển activity
        val selectedItem = intent.getIntExtra("selected_nav", R.id.nav_home)
        BottomNavHelper.setup(this, binding.bottomNav, selectedItem)
    }
}
