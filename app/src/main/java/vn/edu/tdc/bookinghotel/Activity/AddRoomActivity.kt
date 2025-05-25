package vn.edu.tdc.bookinghotel.Activity

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import vn.edu.tdc.bookinghotel.Model.Hotel
import vn.edu.tdc.bookinghotel.Model.Room
import vn.edu.tdc.bookinghotel.Model.RoomType
import vn.edu.tdc.bookinghotel.Repository.HotelRepository
import vn.edu.tdc.bookinghotel.Repository.RoomRepository
import vn.edu.tdc.bookinghotel.Session.SessionManager
import vn.edu.tdc.bookinghotel.databinding.ActivityAddRoomBinding

class AddRoomActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddRoomBinding
    private val hotels = mutableListOf<Hotel>()
    private val roomTypes = mutableListOf<RoomType>()
    private var selectedImageUri: Uri? = null
    private lateinit var pickImageLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddRoomBinding.inflate(layoutInflater)

        // Ẩn thanh status/navigation bar
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
        setupImagePicker()

        binding.imageView.setOnClickListener {
            pickImageFromGallery()
        }

        binding.btnSubmit.setOnClickListener {
            submitRoom()
        }
    }

    private fun setupSpinners() {
        val session = SessionManager(this)
        val hotelRepo = HotelRepository()

        // Danh sách khách sạn
        hotelRepo.getHotelByHotelier(
            token = session.getToken().toString(),
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

        // Danh sách loại phòng
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

    private fun setupImagePicker() {
        pickImageLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK) {
                val data = result.data
                selectedImageUri = data?.data
                selectedImageUri?.let {
                    binding.imageView.setImageURI(it)
                    // Nếu có EditText lưu ảnh thì setText, nhưng bạn đang dùng ImageView nên bỏ dòng này
                    // binding.edtImage.setText(it.toString())
                }
            }
        }
    }

    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        pickImageLauncher.launch(intent)
    }

    private fun submitRoom() {
        val session=SessionManager(this)
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

        if (roomNumber.isEmpty() || priceStr.isEmpty() || capacityStr.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đủ thông tin", Toast.LENGTH_SHORT).show()
            return
        }

        if (selectedImageUri == null) {
            Toast.makeText(this, "Vui lòng chọn ảnh phòng", Toast.LENGTH_SHORT).show()
            return
        }

        val repositoryRoom = RoomRepository()
        repositoryRoom.addRoom(
            context = this,
            roomNumber = roomNumber,
            roomTypeId = selectedRoomType.id,
            price = priceStr,
            capacity = capacityStr,
            description = description,
            hotelId = selectedHotel.id,
            token=session.getToken().toString(),
            imageUri = selectedImageUri,
            onSuccess = {
                Toast.makeText(this, "Thêm phòng thành công", Toast.LENGTH_SHORT).show()
                finish() // hoặc reset form nếu muốn
            },
            onError = { error ->
                Toast.makeText(this, "Lỗi thêm phòng: ${error.message}", Toast.LENGTH_LONG).show()
            }
        )
    }

}
