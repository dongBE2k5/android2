package vn.edu.tdc.bookinghotel.Activity

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import vn.edu.tdc.bookinghotel.Adapters.BookingRoomDetailsAdapter
import vn.edu.tdc.bookinghotel.Model.Room
import vn.edu.tdc.bookinghotel.R
import vn.edu.tdc.bookinghotel.Repository.RoomRepository
import vn.edu.tdc.bookinghotel.Session.SessionManager
import vn.edu.tdc.bookinghotel.View.BottomNavHelper
import vn.edu.tdc.bookinghotel.databinding.ActivityBookingRoomDetailsBinding
import vn.edu.tdc.bookinghotel.databinding.DetailRoomBinding
import java.math.BigDecimal
import java.text.DecimalFormat

class ChiTietPhongActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBookingRoomDetailsBinding
    private lateinit var adapterListDetail: BookingRoomDetailsAdapter
//    private var rooms = ArrayList<Room>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookingRoomDetailsBinding.inflate(layoutInflater)
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
        // Bottom Navigation setup


        window.insetsController?.let { controller ->
            controller.hide(
                android.view.WindowInsets.Type.statusBars() or android.view.WindowInsets.Type.navigationBars()
            )
            controller.systemBarsBehavior = android.view.WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }

        val session= SessionManager(this);
        val hotelName = intent.getStringExtra("hotel_name")
        val roomId = intent.getLongExtra("roomId", 0L)
        val roomImage = intent.getStringExtra("roomImage")

        Log.d("roomId",roomId.toString())

        Log.d("roomImage Nhan", roomImage.toString())


        val repositoryRoom = RoomRepository()
        repositoryRoom.fetchRoomById(
            roomId = roomId,
            onSuccess = { fetchedRoom ->
                Glide.with(this@ChiTietPhongActivity)
                    .load("${getString(R.string.localUpload)}${roomImage}")
                    .placeholder(R.drawable.khachsan)
                    .error(R.drawable.ic_launcher_background)
                    .into(binding.roomImage)

                binding.nameDichVu.text = fetchedRoom.roomType?.name ?: "Phòng không rõ"
                binding.thongTin1.text = fetchedRoom.description ?: "Không có mô tả"
                binding.thongTin2.text = buildRoomDetails(fetchedRoom)
                binding.hotelDeals.text = when (fetchedRoom.status) {
                    "AVAILABLE" -> "Còn phòng"
                    "MAINTENANCE" -> "Đang bảo trì"
                    else -> "Đã đặt"
                }



                binding.giaTien.text = "${formatCurrency(fetchedRoom.price)} VND/đêm"
                binding.tongGiaTien.text = "Tổng: ${formatCurrency(fetchedRoom.price)} VND"
                binding.phongConLai.text = "Phòng cho ${fetchedRoom.capacity} người"



                binding.btnDat.setOnClickListener {
                    if(session.isLoggedIn()){
                        when (fetchedRoom.status) {
                            "AVAILABLE" -> {
                                val intent = Intent(this, Hotel_BookingActivity::class.java)
                                intent.putExtra("selected_nav", R.id.nav_store)

                                intent.putExtra("roomId", "${fetchedRoom.id}")
                                intent.putExtra("roomImage", fetchedRoom.image)
                                intent.putExtra("roomPrice", fetchedRoom.price)
                                Log.d("roomImage", fetchedRoom.image)
                                startActivity(intent)
                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                            }
                            "MAINTENANCE" -> Toast.makeText(this, "Phòng đang bảo trì", Toast.LENGTH_SHORT).show()
                            "RESERVED" -> {
                                Toast.makeText(this, "Phòng đã đặt trước", Toast.LENGTH_SHORT).show()
                            }
                            else -> {
                                Toast.makeText(this, "Phòng đang hoạt động  ", Toast.LENGTH_SHORT).show()
                            }


                        }
                    }
                    else{
                        showErrorDialog("Vui lòng đăng nhập")
                    }

                }

            },
            onError = { error ->
                Log.e("API Room error", "Error: ${error.message}")
            }
        )



    }
    private fun formatCurrency(amount: BigDecimal?): String {
        val format = DecimalFormat("#,###")
        return format.format(amount ?: BigDecimal.ZERO)
    }

    private fun buildRoomDetails(room: Room): String {
        val details = mutableListOf<String>()
        room.area?.let { details.add("Diện tích ${String.format("%.1f", it)}m²") }
        room.amenities?.let { details.addAll(it) }
        return if (details.isNotEmpty()) details.joinToString(", ") else "Không có thông tin chi tiết"
    }
    fun showErrorDialog(message: String) {
        AlertDialog.Builder(this)
            .setTitle("Thông báo")
            .setMessage(message)
            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
            .show()
    }
}
