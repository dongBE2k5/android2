package vn.edu.tdc.bookinghotel.Activity

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import vn.edu.tdc.bookinghotel.Adapters.Hotel_BookingViewAdapter
import vn.edu.tdc.bookinghotel.Model.Booking
import vn.edu.tdc.bookinghotel.Model.BookingRequest
import vn.edu.tdc.bookinghotel.Model.Customer
import vn.edu.tdc.bookinghotel.Model.CustomerUpdated
import vn.edu.tdc.bookinghotel.Model.Room
import vn.edu.tdc.bookinghotel.Model.Voucher
import vn.edu.tdc.bookinghotel.R
import vn.edu.tdc.bookinghotel.Repository.BookingRepository
import vn.edu.tdc.bookinghotel.Repository.CustomerRepository
import vn.edu.tdc.bookinghotel.Repository.RoomRepository
import vn.edu.tdc.bookinghotel.Repository.VoucherRepository
import vn.edu.tdc.bookinghotel.Session.SessionManager
import vn.edu.tdc.bookinghotel.View.BottomNavHelper
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
    private lateinit var price: BigDecimal
    private lateinit var totalPrice: BigDecimal
    private var isVoucherApplied = false

    private var discountedPrice: BigDecimal? = null
    private var currentBookingId: Long? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = BookingHotelBinding.inflate(layoutInflater)
        val session = SessionManager(this)
        Log.d("IDMain" , "${session.getIdUser()}")
        // Fullscreen Immersive Mode
        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_FULLSCREEN
                )

        // Transparent bars
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.navigationBarColor = Color.TRANSPARENT
            window.statusBarColor = Color.TRANSPARENT
        }


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
                    if (isVoucherApplied) {
                        Toast.makeText(this, "Bạn đã áp dụng voucher rồi", Toast.LENGTH_SHORT).show()
                        return@setOnClickListener
                    }

                    val voucherCode = binding.voucher.text.toString()
                    if (voucherCode.isEmpty()) {
                        Toast.makeText(this, "Vui lòng nhập mã voucher", Toast.LENGTH_SHORT).show()
                        return@setOnClickListener
                    }

                    val voucherRepository = VoucherRepository()
                    voucherRepository.findVoucherByCode(voucherCode, onSuccess = { voucher: Voucher ->
                        if (voucher.quantity > 0) {
                            val formatter = NumberFormat.getCurrencyInstance(Locale("vi", "VN"))

                            // Hiển thị giá gốc bị gạch
                            binding.originalPrice.apply {
                                text = formatter.format(totalPrice)
                                visibility = View.VISIBLE
                                paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                            }

                            // Tính giảm giá
                            val discount = totalPrice * (voucher.percent.toDouble() / 100).toBigDecimal()
                            discountedPrice = totalPrice - discount

                            // Hiển thị giá sau giảm
                            binding.price.text = formatter.format(discountedPrice)

                            // Giảm lượt voucher
                            voucherRepository.decrementVoucherQuantity(
                                voucher.id,
                                onSuccess = {
                                    Toast.makeText(this, "Đã áp dụng voucher", Toast.LENGTH_SHORT).show()

                                    // ✅ Chỉ khi thành công thì mới không cho nhập nữa
                                    isVoucherApplied = true
                                    binding.applyvoucher.isEnabled = false
                                    binding.voucher.isEnabled = false
                                },
                                onGone = {
                                    Toast.makeText(this, "Voucher đã hết lượt và bị xoá khỏi hệ thống", Toast.LENGTH_SHORT).show()
                                    binding.voucher.setText("")
                                },
                                onError = { error ->
                                    Toast.makeText(this, "Áp dụng voucher thành công nhưng cập nhật số lượt thất bại", Toast.LENGTH_SHORT).show()
                                    Log.e("VoucherUpdate", "Error: ${error.message}")
                                }
                            )
                        } else {
                            AlertDialog.Builder(this)
                                .setMessage("Voucher đã hết lượt sử dụng")
                                .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
                                .show()
                        }
                    }, onError = { error: Throwable ->
                        Toast.makeText(this, "Không tìm thấy voucher", Toast.LENGTH_SHORT).show()
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

                    // Update Customer nếu có thay đổi
                    if ((fullName != "" && fullName != customer.fullName) ||
                        (phone != "" && phone != customer.phone) ||
                        (cccd != "" && cccd != customer.cccd)
                    ) {
                        val customerUpdate = CustomerUpdated(fullName, cccd, phone)
                        customerRepository.updateCustomer(
                            session.getIdCustomer()!!.toLong(),
                            customerUpdate,
                            onSuccess = { customer: Customer -> },
                            onError = { error: Throwable -> }
                        )
                    }

                    if (checkInDate != "" && checkOutDate != "") {
                        val priceToBook = discountedPrice ?: totalPrice // ưu tiên giá giảm nếu có
                        val bookingRequest = BookingRequest(
                            session.getIdCustomer()!!.toLong(),
                            roomID!!.toLong(),
                            priceToBook,
                            checkInDate,
                            checkOutDate
                        )

                        bookingRepository.createBooking(
                            bookingRequest,
                            onSuccess = { booking: Booking ->
                                currentBookingId = booking.id
                                Toast.makeText(this, "Booking thành công", Toast.LENGTH_SHORT).show()
                                val intent = Intent(this, MainActivity::class.java)
                                startActivity(intent)
                            },
                            onError = { error: Throwable ->
                                Toast.makeText(this, "Ngày không hợp lệ", Toast.LENGTH_SHORT).show()
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
                binding.originalPrice.visibility = View.GONE  // Thêm dòng này
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
                binding.originalPrice.visibility = View.GONE  // Thêm dòng này
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
