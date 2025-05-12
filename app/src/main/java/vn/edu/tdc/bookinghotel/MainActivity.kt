package vn.edu.tdc.bookinghotel

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import vn.edu.tdc.bookinghotel.Adapters.MyHotelRecyclerViewAdapter
import vn.edu.tdc.bookinghotel.Adapters.MyVoucherRecyclerViewAdapter
import vn.edu.tdc.bookinghotel.Model.Hotel
import vn.edu.tdc.bookinghotel.Model.Voucher
import vn.edu.tdc.bookinghotel.databinding.HomePageLayoutBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: HomePageLayoutBinding
    private lateinit var adapter: MyHotelRecyclerViewAdapter
    private lateinit var adapterVoucher: MyVoucherRecyclerViewAdapter

    private val originalHotels = arrayListOf(
        Hotel("TP HCM", "22Land Residence Hotel 71 Hang Bong", "⭐⭐⭐⭐⭐", "8.6/10", "864.000 VND", R.drawable.khachsan),
        Hotel("Đà Lạt", "22Land Residence Hotel 71 Hang Bong", "⭐⭐⭐⭐⭐", "8.6/10", "864.000 VND", R.drawable.khachsan),
        Hotel("Vũng Tàu", "22Land Residence Hotel 71 Hang Bong", "⭐⭐⭐⭐⭐", "8.6/10", "864.000 VND", R.drawable.khachsan),
        Hotel("TP HCM", "22Land Residence Hotel 71 Hang Bong", "⭐⭐⭐⭐⭐", "8.6/10", "864.000 VND", R.drawable.khachsan),
        Hotel("TP HCM", "22Land Residence Hotel 71 Hang Bong", "⭐⭐⭐⭐⭐", "8.6/10", "864.000 VND", R.drawable.khachsan)
    )

    private val vouchers = arrayListOf(
        Voucher("Giảm đến 1 triệu tất cả Khách Sạn", "Đặt khách sạn từ 2.5 triệu", "VNEPICTHANKYOU"),
        Voucher("Giảm đến 1 triệu tất cả Khách Sạn", "Đặt khách sạn từ 2.5 triệu", "VNEPICTHANKYOU"),
        Voucher("Giảm đến 1 triệu tất cả Khách Sạn", "Đặt khách sạn từ 2.5 triệu", "VNEPICTHANKYOU"),
        Voucher("Giảm đến 1 triệu tất cả Khách Sạn", "Đặt khách sạn từ 2.5 triệu", "VNEPICTHANKYOU"),
        Voucher("Giảm đến 1 triệu tất cả Khách Sạn", "Đặt khách sạn từ 2.5 triệu", "VNEPICTHANKYOU")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = HomePageLayoutBinding.inflate(layoutInflater)

        // full màn hình
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

        // Khởi tạo danh sách hotels hiện tại
        val hotels = ArrayList(originalHotels)

        // Spinner: thành phố
        val cities = arrayOf("Tất cả", "TP HCM", "Đà Lạt", "Vũng Tàu")
        val adapterSpinner = ArrayAdapter(this, android.R.layout.simple_spinner_item, cities)
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        val citySpinner = findViewById<Spinner>(R.id.citySpinner)
        citySpinner.adapter = adapterSpinner

        // RecyclerView: Hotels
        val recyclerView = findViewById<RecyclerView>(R.id.recycleListHotel)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        adapter = MyHotelRecyclerViewAdapter(this, hotels)
        recyclerView.adapter = adapter

        // RecyclerView: Vouchers
        val recyclerViewVoucher = findViewById<RecyclerView>(R.id.recycleListVoucher)
        recyclerViewVoucher.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        adapterVoucher = MyVoucherRecyclerViewAdapter(this, vouchers)
        recyclerViewVoucher.adapter = adapterVoucher

        // Spinner selection handling
        citySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedCity = cities[position]
                val filtered = if (selectedCity == "Tất cả") {
                    originalHotels
                } else {
                    originalHotels.filter { it.thanhpho.equals(selectedCity, ignoreCase = true) }
                }
                adapter.updateData(ArrayList(filtered))
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        // Bottom Navigation xử lý chuyển activity
        val selectedItem = intent.getIntExtra("selected_nav", R.id.nav_home)
        binding.bottomNav.selectedItemId = selectedItem
        binding.bottomNav.setOnItemSelectedListener { item ->
            if (item.itemId != selectedItem) {
                val intent = when (item.itemId) {
                    R.id.nav_home -> Intent(this, MainActivity::class.java)
                    R.id.nav_store -> Intent(this, StoreActivity::class.java)
                    R.id.nav_profile -> Intent(this, AcountActivity::class.java)
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

        // Click vào item Hotel
        adapter.setOnItemClick(object : MyHotelRecyclerViewAdapter.OnRecyclerViewItemClickListener {
            override fun onImageClickListener(item: View?, position: Int) {
                val hotel = adapter.getItem(position)
                Toast.makeText(this@MainActivity, "Ảnh: ${hotel.name}", Toast.LENGTH_SHORT).show()
                // Chuyển đến ChiTietKhachSan
                Intent(this@MainActivity, ChiTietKhachSan::class.java)
                startActivity(intent)
            }

            override fun onMyItemClickListener(item: View?, position: Int) {
                val hotel = adapter.getItem(position)

                // Chuyển đến ChiTietKhachSan
                val intent = Intent(this@MainActivity, ChiTietKhachSan::class.java)

                // Optional: Nếu cần truyền thêm dữ liệu (ví dụ: thông tin hotel)
                intent.putExtra("hotel_name", hotel.name)  // Bạn có thể thay "hotel_name" bằng bất kỳ key nào bạn muốn
                startActivity(intent)

                Toast.makeText(this@MainActivity, "Item: ${hotel.name}", Toast.LENGTH_SHORT).show()
            }
        })


        // Click Voucher Item
        adapterVoucher.setOnItemClick(object : MyVoucherRecyclerViewAdapter.OnRecyclerViewItemClickListener {
            override fun onImageClickListener(item: View?, position: Int) {
                Toast.makeText(this@MainActivity, "Ảnh: ${vouchers[position].title}", Toast.LENGTH_SHORT).show()
            }

            override fun onMyItemClickListener(item: View?, position: Int) {
                Toast.makeText(this@MainActivity, "Item: ${vouchers[position].title}", Toast.LENGTH_SHORT).show()
            }
        })
    }


}
