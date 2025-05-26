package vn.edu.tdc.bookinghotel.Activity

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import vn.edu.tdc.bookinghotel.Adapters.HotelDaDatAdapter
import vn.edu.tdc.bookinghotel.Model.Booking
import vn.edu.tdc.bookinghotel.R
import vn.edu.tdc.bookinghotel.Repository.BookingRepository
import vn.edu.tdc.bookinghotel.Session.SessionManager
import vn.edu.tdc.bookinghotel.View.BottomNavHelper
import vn.edu.tdc.bookinghotel.databinding.StoreBinding

class StoreActivity : AppCompatActivity() {

    private lateinit var binding: StoreBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: HotelDaDatAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = StoreBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)

        // Full màn hình
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
            controller.systemBarsBehavior =
                android.view.WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }

        // Bottom Navigation xử lý chuyển activity
        val selectedItem = intent.getIntExtra("selected_nav", R.id.nav_store)
        BottomNavHelper.setup(this, binding.bottomNav, selectedItem)

        val session = SessionManager(this)
        Log.d("IDMain", "${session.getIdCustomer()}")

        recyclerView = binding.recycleKsDaDat
        recyclerView.layoutManager = LinearLayoutManager(this)

        val bookedHotel = ArrayList<Booking>()
        val bookingRepository = BookingRepository()

        bookingRepository.getBookingByCustomerId(session.getIdCustomer()!!.toLong(), { bookings ->
            bookedHotel.addAll(bookings)
            Log.d("List", "$bookedHotel")

            adapter = HotelDaDatAdapter(this, bookedHotel)
            recyclerView.adapter = adapter

            // Chỉ gán onCancelClick sau khi adapter đã được khởi tạo
            adapter.onCancelClick = { booking, position ->
                val dialog = AlertDialog.Builder(this)
                    .setTitle("Xác nhận hủy")
                    .setMessage("Bạn có chắc chắn muốn hủy phòng này?")
                    .setPositiveButton("Có") { _, _ ->
                        // Gọi API cập nhật trạng thái hủy
                        bookingRepository.cancelBooking(booking.id, {
                            if (booking.status.equals("ĐÃ HỦY") || booking.status.equals("ĐÃ TRẢ PHÒNG") ){
                                showErrorDialog("Phòng đã ${booking.status} nên không thể thực hiện")
                            }
                            else{
                                // Cập nhật trạng thái local sau khi huỷ
                                val updatedBooking = Booking(
                                    id = booking.id,
                                    customer = booking.customer,
                                    room = booking.room,
                                    checkinDate = booking.checkinDate,
                                    checkoutDate = booking.checkoutDate,
                                    price = booking.price,
                                    status = "Đã hủy"
                                )

                                adapter.updateItem(position, updatedBooking)
                            }

                        }, { error ->
                            Log.e("StoreActivity", "Lỗi huỷ phòng: ${error.message}")
                        })

                    }
                    .setNegativeButton("Không", null)
                    .create()
                dialog.show()
            }

            adapter.notifyDataSetChanged()
        }, { error ->
            Log.e("StoreActivity", "Error fetching bookings: $error")
        })
    }
    fun showErrorDialog(message: String) {
        AlertDialog.Builder(this)
            .setTitle("Thông báo")
            .setMessage(message)
            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
            .show()
    }
}
