package vn.edu.tdc.bookinghotel.Activity

import android.R
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import vn.edu.tdc.bookinghotel.Model.Hotel
import vn.edu.tdc.bookinghotel.Model.Room
import vn.edu.tdc.bookinghotel.Model.RoomType
import vn.edu.tdc.bookinghotel.Repository.HotelRepository
import vn.edu.tdc.bookinghotel.Repository.RoomRepository
import vn.edu.tdc.bookinghotel.databinding.ActivityAddRoomBinding

class AddRoomActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddRoomBinding
    private val hotels = mutableListOf<Hotel>()
    private val roomTypes = mutableListOf<RoomType>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddRoomBinding.inflate(layoutInflater)

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
        setContentView(binding.root)

        setupSpinners()

        binding.btnSubmit.setOnClickListener {
            submitRoom()
        }
    }

    private fun setupSpinners() {
        val hotelRepo = HotelRepository()
        val roomTypeRepo = RoomRepository() // Hoặc RoomTypeRepository nếu bạn tách riêng

        // Lấy danh sách khách sạn
        hotelRepo.fetchHotel(
            onSuccess = { hotelList ->
                hotels.clear()
                hotels.addAll(hotelList)

                val hotelNames = hotels.map { it.name }
                val hotelAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, hotelNames)
                hotelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.spinnerHotel.adapter = hotelAdapter
            },
            onError = { error ->
                Toast.makeText(this, "Lỗi tải khách sạn: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        )

        // Lấy danh sách loại phòng
        val roomRepo = RoomRepository()

        roomRepo.fetchRoomTypes(
            onSuccess = { typeList ->
                roomTypes.clear()
                roomTypes.addAll(typeList)

                val typeNames = roomTypes.map { it.name }
                val typeAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, typeNames)
                typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.spinnerRoomType.adapter = typeAdapter
            },
            onError = { error ->
                Toast.makeText(this, "Lỗi tải loại phòng: ${error.message}", Toast.LENGTH_SHORT).show()
                Log.e("FetchRoomTypes", "Lỗi: ${error.message}", error)
            }
        )


    }


    private fun submitRoom() {
        val hotelPosition = binding.spinnerHotel.selectedItemPosition
        val roomTypePosition = binding.spinnerRoomType.selectedItemPosition

        if (hotelPosition == -1 || roomTypePosition == -1) {
            Toast.makeText(this, "Vui lòng chọn khách sạn và loại phòng", Toast.LENGTH_SHORT).show()
            return
        }

        val selectedHotel = hotels[hotelPosition]
        val selectedRoomType = roomTypes[roomTypePosition]

        val roomNumber = binding.edtRoomNumber.text.toString().trim()
        val capacityStr = binding.edtCapacity.text.toString().trim()
        val description = binding.edtDescription.text.toString().trim()
        val priceStr = binding.edtPrice.text.toString().trim()
        val status = binding.edtStatus.text.toString().trim()
        val imageUrl = binding.edtImage.text.toString().trim()

        if (roomNumber.isEmpty() || priceStr.isEmpty() || capacityStr.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đủ thông tin", Toast.LENGTH_SHORT).show()
            return
        }

        val newRoom = Room(
            id = 0L,
            capacity = capacityStr.toInt(),
            description = description,
            image = imageUrl,
            price = priceStr.toBigDecimal(),
            roomNumber = roomNumber,
            status = status,
            hotel = selectedHotel,
            roomType = selectedRoomType,
            area = null,
            amenities = null,
            soPhong = 1
        )

        Toast.makeText(this, "Đã tạo phòng: $newRoom", Toast.LENGTH_LONG).show()
    }


}
