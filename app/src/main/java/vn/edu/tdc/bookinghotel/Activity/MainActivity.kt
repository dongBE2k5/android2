package vn.edu.tdc.bookinghotel.Activity

import MyVoucherRecyclerViewAdapter
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import vn.edu.tdc.bookinghotel.Adapters.MyHotelRecyclerViewAdapter
import vn.edu.tdc.bookinghotel.CallAPI.LocationAPI
import vn.edu.tdc.bookinghotel.Model.Hotel
import vn.edu.tdc.bookinghotel.Model.Location
import vn.edu.tdc.bookinghotel.Repository.LocationRepository
import vn.edu.tdc.bookinghotel.Model.Voucher
import vn.edu.tdc.bookinghotel.R
import vn.edu.tdc.bookinghotel.Repository.HotelRepository
import vn.edu.tdc.bookinghotel.Repository.RoomRepository
import vn.edu.tdc.bookinghotel.Repository.VoucherRepository
import vn.edu.tdc.bookinghotel.Session.SessionManager
import vn.edu.tdc.bookinghotel.View.BottomNavHelper
import vn.edu.tdc.bookinghotel.databinding.HomePageLayoutBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: HomePageLayoutBinding
    private lateinit var adapter: MyHotelRecyclerViewAdapter
    private lateinit var adapterVoucher: MyVoucherRecyclerViewAdapter
    private val vouchers = ArrayList<Voucher>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = HomePageLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val session = SessionManager(this)
        val username = session.getUserName()
        binding.userNameAccount.text = username ?: "Guest"

        Log.d("IDMain", "${session.getIdUser()}")

        // Ẩn status bar và navigation bar
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            WindowCompat.setDecorFitsSystemWindows(window, false)
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



        // Khởi tạo Spinner location
        // Khởi tạo RecyclerView Voucher
        val recyclerViewVoucher = binding.recycleListVoucher
        recyclerViewVoucher.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        adapterVoucher = MyVoucherRecyclerViewAdapter(this, vouchers)
        recyclerViewVoucher.adapter = adapterVoucher


        val voucherRepository = VoucherRepository()
        voucherRepository.fetchAllVouchers(
            onSuccess = { voucherList ->
                Log.d("VoucherAPI", "Fetched vouchers: $voucherList")
                runOnUiThread {
                    vouchers.clear()
                    vouchers.addAll(voucherList)
                    adapterVoucher.notifyDataSetChanged()
                }
            },
            onError = { error ->
                Log.e("VoucherAPI", "Error fetching vouchers: ${error.message}")
            }
        )

        //sap xep voucher tang dang theo quantity
//        val voucherRepository = VoucherRepository()
//        voucherRepository.fetchVouchersSortedByQuantity(
//            onSuccess = { sortedVoucherList ->
//                Log.d("VoucherAPI", "Sorted vouchers: $sortedVoucherList")
//                runOnUiThread {
//                    vouchers.clear()
//                    vouchers.addAll(sortedVoucherList)
//                    adapterVoucher.notifyDataSetChanged()
//                }
//            },
//            onError = { error ->
//                Log.e("VoucherAPI", "Error fetching sorted vouchers: ${error.message}")
//            }
//        )

        val repositoryLocation = LocationRepository()
        val locations = ArrayList<Location>()
        val locationNames = ArrayList<String>()
        val repositoryHotel = HotelRepository()

        val adapterSpinner = ArrayAdapter(this, android.R.layout.simple_spinner_item, locationNames)
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.citySpinner.adapter = adapterSpinner
        binding.citySpinner.setSelection(0)



        if (session.getRoleUserNamer().equals("ROLE_USER") || !session.isLoggedIn()){
            repositoryLocation.fetchLocations(
                onSuccess = { locationList ->
                    locations.clear()
                    locations.addAll(locationList)

                    locationNames.clear()
                    locationNames.addAll(locationList.map { it.name })
                    adapterSpinner.notifyDataSetChanged()
                },
                onError = { error ->
                    Log.e("API Error", "Error: ${error.message}")
                }
            )




            // Xử lý Spinner chọn location
            binding.citySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    if (locations.isEmpty()) return
                    val selectedLocation = locations[position]

                    repositoryHotel.fetchHotelByLocation(
                        locationId = selectedLocation.id,
                        onSuccess = { hotelsList ->
                            // Gọi hàm lấy số phòng từng khách sạn rồi sắp xếp
                            fetchHotelsSortedByRoomCount(hotelsList) { sortedHotels ->
                                runOnUiThread {
                                    // Lấy map số phòng để truyền cho adapter
                                    val roomCountMap = mutableMapOf<Long, Int>()
                                    sortedHotels.forEach { hotel ->
                                        roomCountMap[hotel.id] = hotel.roomCount ?: 0
                                    }

                                    // Tạo adapter với danh sách đã sắp xếp và map số phòng
                                    adapter = MyHotelRecyclerViewAdapter(this@MainActivity, sortedHotels, selectedLocation.name, roomCountMap)
                                    binding.recycleListHotel.layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
                                    binding.recycleListHotel.adapter = adapter

                                    adapter.setOnItemClick(object : MyHotelRecyclerViewAdapter.OnRecyclerViewItemClickListener {
                                        override fun onImageClickListener(item: View?, position: Int) {
                                            val hotel = adapter.getItem(position)
                                            val intent = Intent(this@MainActivity, ChiTietKhachSan::class.java)
                                            intent.putExtra("hotel_name", hotel.name)
                                            intent.putExtra("hotel_id", hotel.id)
                                            intent.putExtra("hotel_image", hotel.image)
                                            startActivity(intent)
                                            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                                        }

                                        override fun onMyItemClickListener(item: View?, position: Int) {
                                            val hotel = adapter.getItem(position)
                                            val intent = Intent(this@MainActivity, ChiTietKhachSan::class.java)
                                            intent.putExtra("hotel_name", hotel.name)
                                            intent.putExtra("hotel_id", hotel.id)
                                            intent.putExtra("hotel_image", hotel.image)
                                            startActivity(intent)
                                            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                                        }
                                    })
                                }
                            }
                        },
                        onError = { error ->
                            Log.e("API Hotel error", "Error: ${error.message}")
                        }
                    )
                }
                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }

        }
        else if (session.getRoleUserNamer().equals("ROLE_ADMIN")) {
            repositoryHotel.getHotelByHotelier(
                token = session.getToken().toString(),
                onSuccess = { hotelList ->

                    // Tạo danh sách location từ các hotel
                    locations.clear()
                    locations.addAll(
                        hotelList.map { it.locations }.distinctBy { it.id }
                    )

                    locationNames.clear()
                    locationNames.addAll(locations.map { it.name })
                    adapterSpinner.notifyDataSetChanged()

                    // Xử lý Spinner chọn location
                    binding.citySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                            if (locations.isEmpty()) return
                            val selectedLocation = locations[position]

                            val hotelsInSelectedLocation = hotelList.filter { it.locations.id == selectedLocation.id }

                            fetchHotelsSortedByRoomCount(hotelsInSelectedLocation) { sortedHotels ->
                                runOnUiThread {
                                    val roomCountMap = mutableMapOf<Long, Int>()
                                    sortedHotels.forEach { hotel ->
                                        roomCountMap[hotel.id] = hotel.roomCount ?: 0
                                    }

                                    adapter = MyHotelRecyclerViewAdapter(this@MainActivity, sortedHotels, selectedLocation.name, roomCountMap)
                                    binding.recycleListHotel.layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
                                    binding.recycleListHotel.adapter = adapter

                                    adapter.setOnItemClick(object : MyHotelRecyclerViewAdapter.OnRecyclerViewItemClickListener {
                                        override fun onImageClickListener(item: View?, position: Int) {
                                            val hotel = adapter.getItem(position)
                                            val intent = Intent(this@MainActivity, ChiTietKhachSan::class.java)
                                            intent.putExtra("hotel_name", hotel.name)
                                            intent.putExtra("hotel_id", hotel.id)
                                            intent.putExtra("hotel_image", hotel.image)
                                            startActivity(intent)
                                            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                                        }

                                        override fun onMyItemClickListener(item: View?, position: Int) {
                                            val hotel = adapter.getItem(position)
                                            val intent = Intent(this@MainActivity, ChiTietKhachSan::class.java)
                                            intent.putExtra("hotel_name", hotel.name)
                                            intent.putExtra("hotel_id", hotel.id)
                                            intent.putExtra("hotel_image", hotel.image)
                                            startActivity(intent)
                                            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                                        }
                                    })
                                }
                            }
                        }

                        override fun onNothingSelected(parent: AdapterView<*>?) {}
                    }
                },
                onError = { error ->
                    Log.e("AdminHotelFetch", "Lỗi: ${error.message}")
                }
            )
        }
        // Bottom Navigation setup
        val selectedItem = intent.getIntExtra("selected_nav", R.id.nav_home)
        BottomNavHelper.setup(this, binding.bottomNav, selectedItem)

    }

    // Hàm fetch phòng và sắp xếp khách sạn theo số phòng
    fun fetchHotelsSortedByRoomCount(
        hotels: List<Hotel>,
        onComplete: (List<Hotel>) -> Unit
    ) {
        val roomRepository = RoomRepository()
        val hotelRoomCountMap = mutableMapOf<Long, Int>()
        var completedCount = 0

        if (hotels.isEmpty()) {
            onComplete(emptyList())
            return
        }

        for (hotel in hotels) {
            roomRepository.fetchRoomByHotel(
                hotelId = hotel.id,
                onSuccess = { roomList ->
                    hotelRoomCountMap[hotel.id] = roomList.size
                    completedCount++
                    if (completedCount == hotels.size) {
                        // Gán số phòng vào thuộc tính hotel (bạn cần thêm thuộc tính này)
                        val updatedHotels = hotels.map { h ->
                            h.apply { roomCount = hotelRoomCountMap[h.id] ?: 0 }
                        }
                        // Sắp xếp
                        val sortedHotels = updatedHotels.sortedBy { it.roomCount }
                        onComplete(sortedHotels)
                    }
                },
                onError = {
                    hotelRoomCountMap[hotel.id] = 0
                    completedCount++
                    if (completedCount == hotels.size) {
                        val updatedHotels = hotels.map { h ->
                            h.apply { roomCount = hotelRoomCountMap[h.id] ?: 0 }
                        }
                        val sortedHotels = updatedHotels.sortedBy { it.roomCount }
                        onComplete(sortedHotels)
                    }
                }
            )
        }
    }
    private fun deleteVoucherAfterUse(voucherId: Long) {
        val voucherRepository = VoucherRepository()
        voucherRepository.deleteVoucher(
            voucherId,
            onSuccess = {
                runOnUiThread {
                    Toast.makeText(this, "Voucher đã được sử dụng và xoá.", Toast.LENGTH_SHORT).show()
                    refreshVoucherList()
                }
            },
            onError = { error ->
                runOnUiThread {
                    Toast.makeText(this, "Xoá voucher thất bại: ${error.message}", Toast.LENGTH_SHORT).show()
                }
            }
        )
    }

    private fun refreshVoucherList() {
        val voucherRepository = VoucherRepository()
        voucherRepository.fetchAllVouchers(
            onSuccess = { voucherList ->
                runOnUiThread {
                    vouchers.clear()
                    vouchers.addAll(voucherList)
                    adapterVoucher.notifyDataSetChanged()
                }
            },
            onError = { error ->
                Log.e("VoucherAPI", "Error fetching vouchers: ${error.message}")
            }
        )
    }

}