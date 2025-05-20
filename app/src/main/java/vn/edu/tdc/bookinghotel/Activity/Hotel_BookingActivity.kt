package vn.edu.tdc.bookinghotel.Activity

import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import vn.edu.tdc.bookinghotel.Adapters.Hotel_BookingViewAdapter
import vn.edu.tdc.bookinghotel.Model.Booking
import vn.edu.tdc.bookinghotel.Model.BookingRequest
import vn.edu.tdc.bookinghotel.Model.Customer
import vn.edu.tdc.bookinghotel.Model.CustomerUpdate
import vn.edu.tdc.bookinghotel.Model.Hotel_Booking
import vn.edu.tdc.bookinghotel.Model.Room
import vn.edu.tdc.bookinghotel.R
import vn.edu.tdc.bookinghotel.Repository.BookingRepository
import vn.edu.tdc.bookinghotel.Repository.CustomerRepository
import vn.edu.tdc.bookinghotel.Response.BookingResponse
import vn.edu.tdc.bookinghotel.Response.CustomerResponse
import vn.edu.tdc.bookinghotel.Session.SessionManager
import vn.edu.tdc.bookinghotel.databinding.ActivityHotelBookkingBinding
import vn.edu.tdc.bookinghotel.databinding.BookingHotelBinding
import java.util.Calendar

class Hotel_BookingActivity : AppCompatActivity() {
    private lateinit var binding: BookingHotelBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: Hotel_BookingViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = BookingHotelBinding.inflate(layoutInflater)
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
        setContentView(binding.root)
        val roomID = intent.getStringExtra("roomId")
        val roomImage = intent.getStringExtra("roomImage")
        Log.d("roomID", "${roomID}")
        Log.d("roomImage", "${roomImage}")
        Glide.with(this)
            .load("${getString(R.string.localUpload)}${roomImage}") // hoặc roomImage trực tiếp nếu là URL đầy đủ
            .placeholder(R.drawable.khachsan)
            .error(R.drawable.ic_launcher_background)
            .into(binding.imgRoom)
        val customerRepository = CustomerRepository()
        customerRepository.fetchCustomer(
            session.getIdUser()!!.toLong(),
            onSuccess = { customer: Customer ->
                // Xử lý khi lấy thành công customer
                Log.d("Customer", "Name: ${customer.fullName}")
                customer.fullName?.let {
                    binding.edtFullName.setText(it)
                }
                customer.phone?.let {
                    binding.edtPhoneNumber.setText(it)
                }
                customer.cccd?.let {
                    binding.edtCCCD.setText(it)
                }
                binding.btnBookRoom.setOnClickListener {

                    val bookingRepository = BookingRepository()
                    val customerRepository = CustomerRepository()
                    val fullName = binding.edtFullName.text.toString()
                    val phone = binding.edtPhoneNumber.text.toString()
                    val cccd = binding.edtCCCD.text.toString()
                    val checkInDate = binding.edtCheckInDate.text.toString()
                    val checkOutDate = binding.edtCheckOutDate.text.toString()
                    // Update Customer
                    if ((fullName != "" && fullName != customer.fullName) ||
                        (phone != "" && phone != customer.phone) ||
                        (cccd != "" && cccd != customer.cccd)
                    ) {
                        val customerUpdate = CustomerUpdate(fullName, cccd, phone)
                        customerRepository.updateCustomer(
                            session.getIdUser()!!.toLong(),
                            customerUpdate,
                            onSuccess = { customer: Customer ->
                                Log.d("customer", customer.toString())
                            },
                            onError = { error: Throwable ->
                                Log.e("Customer", "Error: ${error.message}")
                            }
                        )
                    }
                    if( checkInDate != "" && checkOutDate != "") {
                        val bookingRequest = BookingRequest(session.getIdUser()!!.toLong(), roomID!!.toLong(), checkInDate, checkOutDate)

                        bookingRepository.createBooking(
                            bookingRequest,
                            onSuccess = { booking: Booking ->
                                Log.d("booking", booking.toString())
                                val intent = Intent(this, MainActivity::class.java)
                                startActivity(intent)
                            },
                            onError = { error: Throwable ->
                                Log.e("Booking", "Error: ${error.message}")
                            }
                        )
                    }
                }

            },
            onError = { error: Throwable ->
                // Xử lý khi có lỗi
                Log.e("Customer", "Error: ${error.message}")
            }
        )
        val calendar = Calendar.getInstance()
        val datePickerCheckIn = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                val selectedDate = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth)
                binding.edtCheckInDate.setText(selectedDate)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        binding.edtCheckInDate.setOnClickListener {
            datePickerCheckIn.show()
        }

        val datePickerCheckout = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                val selectedDate = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth)
                binding.edtCheckOutDate.setText(selectedDate)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        binding.edtCheckOutDate.setOnClickListener {
            datePickerCheckout.show()
        }

        // Bottom Navigation xử lý chuyển activity
//        val selectedItem = intent.getIntExtra("selected_nav", R.id.nav_home)
//        binding.bottomNav.selectedItemId = selectedItem
//        binding.bottomNav.setOnItemSelectedListener { item ->
//            if (item.itemId != selectedItem) {
//                val intent = when (item.itemId) {
//                    R.id.nav_home -> Intent(this, MainActivity::class.java)
//                    R.id.nav_store -> Intent(this, StoreActivity::class.java)
//                    R.id.nav_profile -> Intent(this, AcountActivity::class.java)
//                    R.id.nav_admin -> Intent(this, AdminActivity::class.java)
//                    else -> null
//                }
//                intent?.let {
//                    it.putExtra("selected_nav", item.itemId)
//                    startActivity(it)
//                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
//                    finish()
//                }
//                true
//            } else {
//                true
//            }
//        }


//        recyclerView = binding.recyclerViewBookings
//        recyclerView.layoutManager = LinearLayoutManager(this)

//        val dummyData = generateDummyBookings()
//        adapter = Hotel_BookingViewAdapter(dummyData)
//        recyclerView.adapter = adapter
    }

//    private fun generateDummyBookings(): List<Hotel_Booking> {
//        return listOf(
//            Hotel_Booking(
//                bookingId = 1,
//                customerId = 1001,
//                roomId = 201,
//                checkInDate = "2025-05-01",
//                checkOutDate = "2025-05-05",
//                status = "Đã đặt",
//                roomName = "Phòng Deluxe Hướng Biển",
//                imageUrl = R.drawable.khachsan,
//                contactInfo = "user1@example.com",
//                voucherCode = "SUMMER2025",
//                paymentMethods = "Tiền mặt,Chuyển khoản",
//                totalAmount = "5,000,000 VNĐ",
//                isInsuranceSelected = true,
//                insurancePrice = "43,500 VNĐ"
//            )
//        )
//    }
}