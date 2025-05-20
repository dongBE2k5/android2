package vn.edu.tdc.bookinghotel.Activity

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import vn.edu.tdc.bookinghotel.Adapters.MyHotelRecyclerViewAdapter
import vn.edu.tdc.bookinghotel.Adapters.MyVoucherRecyclerViewAdapter
import vn.edu.tdc.bookinghotel.CallAPI.LocationAPI
import vn.edu.tdc.bookinghotel.Model.Hotel
import vn.edu.tdc.bookinghotel.Model.Location
import vn.edu.tdc.bookinghotel.Repository.LocationRepository
import vn.edu.tdc.bookinghotel.Model.Voucher
import vn.edu.tdc.bookinghotel.R
import vn.edu.tdc.bookinghotel.Repository.HotelRepository
import vn.edu.tdc.bookinghotel.Session.SessionManager
import vn.edu.tdc.bookinghotel.View.BottomNavHelper
import vn.edu.tdc.bookinghotel.databinding.HomePageLayoutBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: HomePageLayoutBinding
    private lateinit var adapter: MyHotelRecyclerViewAdapter
    private lateinit var adapterVoucher: MyVoucherRecyclerViewAdapter
    private lateinit var locationAPI: LocationAPI
    private lateinit var locationName: ArrayList<String>



//    private val originalHotels = arrayListOf(
//        Hotel("TP HCM", "22Land Residence Hotel 71 Hang Bong", "⭐⭐⭐⭐⭐", "8.6/10", "864.000 VND", R.drawable.khachsan),
//        Hotel("Đà Lạt", "22Land Residence Hotel 71 Hang Bong", "⭐⭐⭐⭐⭐", "8.6/10", "864.000 VND", R.drawable.khachsan),
//        Hotel("Vũng Tàu", "22Land Residence Hotel 71 Hang Bong", "⭐⭐⭐⭐⭐", "8.6/10", "864.000 VND", R.drawable.khachsan),
//        Hotel("TP HCM", "22Land Residence Hotel 71 Hang Bong", "⭐⭐⭐⭐⭐", "8.6/10", "864.000 VND", R.drawable.khachsan),
//        Hotel("TP HCM", "22Land Residence Hotel 71 Hang Bong", "⭐⭐⭐⭐⭐", "8.6/10", "864.000 VND", R.drawable.khachsan)
//    )

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

        setContentView(binding.root)

        val session = SessionManager(this)
        Log.d("IDMain" , "${session.getIdUser()}")
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


        window.setDecorFitsSystemWindows(false)

        window.insetsController?.let { controller ->
            controller.hide(
                android.view.WindowInsets.Type.statusBars() or android.view.WindowInsets.Type.navigationBars()
            )
            controller.systemBarsBehavior = android.view.WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
        val repositoryLocation = LocationRepository()
        // Spinner: thành phố
        var locations = ArrayList<Location>()
        locationName = ArrayList<String>()
//        locations.add("Đang tải...")
//        locations.add("Đang tải1...")
        val adapterSpinner = ArrayAdapter(this, android.R.layout.simple_spinner_item, locationName)
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        val citySpinner = binding.citySpinner
        citySpinner.adapter = adapterSpinner
        citySpinner.setSelection(0)

// Gọi API
        repositoryLocation.fetchLocations(
            onSuccess = { locationList ->
                locations.clear()
                locations.addAll(locationList)

                locationName.clear()
                locationName.addAll(locationList.map { it.name })
                adapterSpinner.notifyDataSetChanged()
            },
            onError = { error ->
                Log.e("API Error", "Error: ${error.message}")
            }
        )

        // Gọi hàm lấy dữ lệu location va thay doi spinner
//        getLocations(locations, adapterSpinner)


        // Khởi tạo danh sách hotels hiện tại
        // val hotels = ArrayList(originalHotels)



//        getHotelByLocation(locations, hotels)





        // RecyclerView: Vouchers
        val recyclerViewVoucher = findViewById<RecyclerView>(R.id.recycleListVoucher)
        recyclerViewVoucher.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        adapterVoucher = MyVoucherRecyclerViewAdapter(this, vouchers)
        recyclerViewVoucher.adapter = adapterVoucher



        val repositoryHotel= HotelRepository()
        var hotels = ArrayList<Hotel>()

        // Spinner selection handling
        citySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedLocation = locations[position]
                hotels.clear()

                repositoryHotel.fetchHotelByLocation(
                    locationId = selectedLocation.id,  // <-- truyền ID vào
                    onSuccess = { hotelsList ->
                        hotels.addAll(hotelsList)
                        Log.d("hotels",hotels.toString() )
                        Log.d("hotelsImg",hotels.toString() )
                        // Cập nhật RecyclerView sau khi có dữ liệu
                        binding.recycleListHotel.layoutManager = LinearLayoutManager(
                            this@MainActivity,
                            LinearLayoutManager.HORIZONTAL,
                            false
                        )
                        adapter = MyHotelRecyclerViewAdapter(this@MainActivity, hotels, selectedLocation.name)
                        binding.recycleListHotel.adapter = adapter
                        adapter.setOnItemClick(object : MyHotelRecyclerViewAdapter.OnRecyclerViewItemClickListener {
                            override fun onImageClickListener(item: View?, position: Int) {
                                val hotel = adapter.getItem(position)
                                // Tạo Intent mới rồi mới startActivity
                                val intent = Intent(this@MainActivity, ChiTietKhachSan::class.java)
                                intent.putExtra("hotel_name", hotel.name) // nếu cần truyền dữ liệu
                                intent.putExtra("hotel_id", hotel.id)
                                Log.d("Check image",hotel.image )
                                intent.putExtra("hotel_image", hotel.image)

                                startActivity(intent)
                            }

                            override fun onMyItemClickListener(item: View?, position: Int) {
                                val hotel = adapter.getItem(position)

                                val intent = Intent(this@MainActivity, ChiTietKhachSan::class.java)
                                intent.putExtra("hotel_name", hotel.name)
                                intent.putExtra("hotel_id", hotel.id)
                                intent.putExtra("hotel_image", hotel.image)
                                startActivity(intent)
                            }
                        })

                    },
                    onError = { error ->
                        Log.e("API Hotel error", "Error: ${error.message}")
                    }
                )
            }



        override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        // Bottom Navigation xử lý chuyển activity
        val selectedItem = intent.getIntExtra("selected_nav", R.id.nav_home)
        BottomNavHelper.setup(this, binding.bottomNav, selectedItem)

        // Click vào item Hotel
//
//        adapter.setOnItemClick(object : MyHotelRecyclerViewAdapter.OnRecyclerViewItemClickListener {
//            override fun onImageClickListener(item: View?, position: Int) {
//                val hotel = adapter.getItem(position)
////                Toast.makeText(this@MainActivity, "Ảnh: ${hotel.name}", Toast.LENGTH_SHORT).show()
//                // Chuyển đến ChiTietKhachSan
//                Intent(this@MainActivity, ChiTietKhachSan::class.java)
//                startActivity(intent)
//            }
//
//            override fun onMyItemClickListener(item: View?, position: Int) {
//                val hotel = adapter.getItem(position)
//
//                // Chuyển đến ChiTietKhachSan
//                val intent = Intent(this@MainActivity, ChiTietKhachSan::class.java)
//
//                // Optional: Nếu cần truyền thêm dữ liệu (ví dụ: thông tin hotel)
//                intent.putExtra("hotel_name", hotel.name)  // Bạn có thể thay "hotel_name" bằng bất kỳ key nào bạn muốn
//                startActivity(intent)
//
////                Toast.makeText(this@MainActivity, "Item: ${hotel.name}", Toast.LENGTH_SHORT).show()
//            }
//        })


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

    override fun onResume() {

        super.onResume()
    }
//    private fun getLocations(locations:ArrayList<Location>, adapter: ArrayAdapter<String> ) {
//        locations.clear()
//        //B2. Dinh nghia doi tuong Retrofit
//        val retrofit = Retrofit.Builder()
//            .baseUrl(LocationAPI.BASE_URL)
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//        //B3. Dinh nghia doi tuong weatherAPI
//            locationAPI = retrofit.create(LocationAPI::class.java)
//        //B4. Goi ham doc du lieu tu Webservice
//        val call = locationAPI.getLocations()
//
//        //B5. Xu li bat dong bo va doc du lieu ve ListView
//        call.enqueue(object : Callback<List<Location>> {
//            override fun onResponse(call: Call<List<Location>>, result: Response<List<Location>>) {
//                // Xu li du lieu doc ve tu Webservice
//                // Neu co du lieu moi xu li
//                if(result.isSuccessful) {
//                    val locationList = result.body()
//                    Log.d("LocationList", locationList.toString())
//                    // Xu li nullable
//                    locationList?.let {
//                        locations.addAll(it.map { location -> location })
//                        locationName.addAll(it.map { location -> location.name })
//                        adapter.notifyDataSetChanged()
//                    }
//                }
//            }
//
//            override fun onFailure(p0: Call<List<Location>>, p1: Throwable) {
//                Log.e("LocationError", "Lỗi: ${p1.message}")
//            }
//
//        })
//    }
//
//    private fun getHotelByLocation(locations:ArrayList<String>, hotels:ArrayList<Hotel>) {
//        locations.clear()
//        //B2. Dinh nghia doi tuong Retrofit
//        val retrofit = Retrofit.Builder()
//            .baseUrl(LocationAPI.BASE_URL)
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//        //B3. Dinh nghia doi tuong weatherAPI
//            locationAPI = retrofit.create(LocationAPI::class.java)
//        //B4. Goi ham doc du lieu tu Webservice
//        val call = locationAPI.getLocations()
//
//        //B5. Xu li bat dong bo va doc du lieu ve ListView
//        call.enqueue(object : Callback<List<Location>> {
//            override fun onResponse(call: Call<List<Location>>, result: Response<List<Location>>) {
//                // Xu li du lieu doc ve tu Webservice
//                // Neu co du lieu moi xu li
//                if(result.isSuccessful) {
//                    val locationList = result.body()
//                    Log.d("LocationList", locationList.toString())
//                    // Xu li nullable
//                    locationList?.let { locations.addAll(it.map { location -> location.name })
//                        adapter.notifyDataSetChanged()
//                    }
//                }
//            }
//
//            override fun onFailure(p0: Call<List<Location>>, p1: Throwable) {
//                Log.e("LocationError", "Lỗi: ${p1.message}")
//            }
//
//        })
//    }
}


