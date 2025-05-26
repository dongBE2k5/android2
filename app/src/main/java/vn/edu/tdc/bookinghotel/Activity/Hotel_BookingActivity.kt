package vn.edu.tdc.bookinghotel.Activity

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
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
import vn.edu.tdc.bookinghotel.Model.Voucher
import vn.edu.tdc.bookinghotel.R
import vn.edu.tdc.bookinghotel.Repository.BookingRepository
import vn.edu.tdc.bookinghotel.Repository.CustomerRepository
import vn.edu.tdc.bookinghotel.Repository.RoomRepository
import vn.edu.tdc.bookinghotel.Repository.VoucherRepository
import vn.edu.tdc.bookinghotel.Response.BookingResponse
import vn.edu.tdc.bookinghotel.Response.CustomerResponse
import vn.edu.tdc.bookinghotel.Session.SessionManager
import vn.edu.tdc.bookinghotel.databinding.ActivityHotelBookkingBinding
import vn.edu.tdc.bookinghotel.databinding.BookingHotelBinding
import java.math.BigDecimal
import java.text.NumberFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.concurrent.TimeUnit

class Hotel_BookingActivity : AppCompatActivity() {
    private lateinit var binding: BookingHotelBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: Hotel_BookingViewAdapter
    private val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    private val calendar = Calendar.getInstance()
    private lateinit var price: BigDecimal
    private lateinit var totalPrice: BigDecimal
    private var voucher: Int = 1

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

        val roomRepository = RoomRepository()
        roomRepository.fetchRoomById(roomID!!.toLong(), onSuccess = { room: Room ->
            price = room.price
            Log.d("room", room.toString())
        }, onError = { error: Throwable ->
            Log.e("Room", "Error: ${error.message}")
        })

        Log.d("roomID", "${roomID}")
        Log.d("roomImagenhan", "${roomImage}")
        Glide.with(this)
            .load("${getString(R.string.localUpload)}${roomImage}") // hoặc roomImage trực tiếp nếu là URL đầy đủ
            .placeholder(R.drawable.khachsan)
            .error(R.drawable.ic_launcher_background)
            .into(binding.imgRoom)
        val customerRepository = CustomerRepository()
        customerRepository.fetchCustomer(
            session.getIdCustomer()!!.toLong(),
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
                binding.applyvoucher.setOnClickListener {
                    val voucherCode = binding.voucher.text.toString()
                    val voucherRepository = VoucherRepository()
                    voucherRepository.findVoucherByCode(voucherCode, onSuccess = { voucher: Voucher ->
                        if(voucher.quantity > 0) {
                            Log.d("voucher", "${voucher}")
                            Log.d("price", "${(voucher.percent / 100)}")
                            Log.d("discount", "${(totalPrice * (voucher.percent.toDouble() / 100).toBigDecimal())}")
                            totalPrice -= (totalPrice * (voucher.percent.toDouble() / 100).toBigDecimal())
                            val formatter = NumberFormat.getCurrencyInstance(Locale("vi", "VN"))
                            binding.price.text = formatter.format(totalPrice)
                        }else {
                            AlertDialog.Builder(this)
                                .setMessage("Voucher đã hết lượt sử dụng")
                                .setPositiveButton("OK") { dialog, _ ->
                                    dialog.dismiss()
                                }
                                .show()
                        }
                    }, onError = { error: Throwable ->
                        Log.e("Voucher", "Error: ${error.message}")
                    })
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
                            session.getIdCustomer()!!.toLong(),
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
                        val bookingRequest = BookingRequest(session.getIdCustomer()!!.toLong(), roomID!!.toLong(), totalPrice ,checkInDate, checkOutDate)
                        val days = getDaysBetween(checkInDate, checkOutDate)
                        Log.d("date", "${days}")
                        val priceResult = price * days.toBigDecimal()

                        bookingRepository.createBooking(
                            bookingRequest,
                            onSuccess = { booking: Booking ->
                                Log.d("booking", booking.toString())
                                Toast.makeText(this, "Booking is success", Toast.LENGTH_SHORT).show()
                                val intent = Intent(this, MainActivity::class.java)
                                startActivity(intent)
                            },
                            onError = { error: Throwable ->
                                Toast.makeText(this, "Ngày không hợp lệ", Toast.LENGTH_SHORT).show()

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
        binding.edtCheckInDate.setOnClickListener {
            showDatePicker(isCheckIn = true)
        }

        binding.edtCheckOutDate.setOnClickListener {
            showDatePicker(isCheckIn = false)
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

    private fun isCheckInBeforeCheckOut(checkIn: String, checkOut: String): Boolean {
        return try {
            val dateCheckIn = sdf.parse(checkIn)
            val dateCheckOut = sdf.parse(checkOut)
            dateCheckIn != null && dateCheckOut != null && dateCheckIn.before(dateCheckOut)
        } catch (e: ParseException) {
            false
        }
    }

    private fun showDatePicker(isCheckIn: Boolean) {
        val currentDate = Calendar.getInstance()
        val listener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            val selectedDate = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth)

            if (isCheckIn) {

                val checkOutDate = binding.edtCheckOutDate.text.toString()
                if (checkOutDate.isNotEmpty() && !isCheckInBeforeCheckOut(selectedDate, checkOutDate)) {
                    Toast.makeText(this, "Check-in phải trước Check-out", Toast.LENGTH_SHORT).show()
                    return@OnDateSetListener
                }
                val days = getDaysBetween(selectedDate, checkOutDate)
                Log.d("date", "${days}")
                totalPrice = price * days.toBigDecimal()
                Log.d("totalPrice", "${totalPrice}")
                val formatter = NumberFormat.getCurrencyInstance(Locale("vi", "VN"))
                binding.price.text = formatter.format(totalPrice)
                binding.edtCheckInDate.setText(selectedDate)
            } else {
                val checkInDate = binding.edtCheckInDate.text.toString()
                if (checkInDate.isNotEmpty() && !isCheckInBeforeCheckOut(checkInDate, selectedDate)) {
                    Toast.makeText(this, "Check-out phải sau Check-in", Toast.LENGTH_SHORT).show()
                    return@OnDateSetListener
                }
                val days = getDaysBetween(checkInDate, selectedDate)
            
                totalPrice = (price * days.toBigDecimal())
                Log.d("totalPrice", "${totalPrice}")
                val formatter = NumberFormat.getCurrencyInstance(Locale("vi", "VN"))
                binding.price.text = formatter.format(totalPrice)
                binding.edtCheckOutDate.setText(selectedDate)
            }
        }

        val datePicker = DatePickerDialog(
            this,
            listener,
            currentDate.get(Calendar.YEAR),
            currentDate.get(Calendar.MONTH),
            currentDate.get(Calendar.DAY_OF_MONTH)
        )

        // Giới hạn không cho chọn ngày trong quá khứ
        datePicker.datePicker.minDate = System.currentTimeMillis()

        // Nếu đang chọn check-out, giới hạn minDate là ngày check-in (nếu có)
        if (!isCheckIn) {
            val checkInDateStr = binding.edtCheckInDate.text.toString()
            if (checkInDateStr.isNotEmpty()) {
                val checkInDate = sdf.parse(checkInDateStr)
                if (checkInDate != null) {
                    datePicker.datePicker.minDate = checkInDate.time
                }
            }
        }

        datePicker.show()
    }

    fun getDaysBetween(checkIn: String, checkOut: String): Long {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return try {
            val date1 = sdf.parse(checkIn)
            val date2 = sdf.parse(checkOut)
            if (date1 != null && date2 != null) {
                val diff = date2.time - date1.time
                TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)
            } else {
                0L
            }
        } catch (e: ParseException) {
            0L
        }
    }
}