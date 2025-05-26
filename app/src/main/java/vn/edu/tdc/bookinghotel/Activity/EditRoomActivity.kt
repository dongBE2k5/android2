package vn.edu.tdc.bookinghotel.Activity

import android.content.Context
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
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import vn.edu.tdc.bookinghotel.Model.Hotel
import vn.edu.tdc.bookinghotel.Model.RoomType
import vn.edu.tdc.bookinghotel.R
import vn.edu.tdc.bookinghotel.Repository.HotelRepository
import vn.edu.tdc.bookinghotel.Repository.RoomRepository
import vn.edu.tdc.bookinghotel.Session.SessionManager
import vn.edu.tdc.bookinghotel.databinding.EditRoomLayoutBinding

class EditRoomActivity : AppCompatActivity() {

    private lateinit var binding: EditRoomLayoutBinding
    private val hotels = mutableListOf<Hotel>()
    private val roomTypes = mutableListOf<RoomType>()
    private var spinnersLoaded = 0 // Đếm spinner đã load xong
    private var roomId: Long = 0L

    private lateinit var pickImageLauncher: ActivityResultLauncher<Intent>
    private var selectedImageUri: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = EditRoomLayoutBinding.inflate(layoutInflater)
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


        window.setDecorFitsSystemWindows(false)

        window.insetsController?.let { controller ->
            controller.hide(
                android.view.WindowInsets.Type.statusBars() or android.view.WindowInsets.Type.navigationBars()
            )
            controller.systemBarsBehavior = android.view.WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }


        val session = SessionManager(this)
        roomId = intent.getLongExtra("roomId", 0L)
        Log.d("roomId", roomId.toString())

        setupStatusSpinner()
        setupSpinners()
        setupImagePicker()
        binding.imageView.setOnClickListener {
            pickImageFromGallery()
        }
        binding.btnSaveRoom.setOnClickListener {
            val roomNumber = binding.edtRoomNumber.text.toString()
            val roomTypeId = binding.edtRoomTypeId.text.toString()
            val price = binding.edtPrice.text.toString()
            val capacity = binding.edtCapacity.text.toString()
            val description = binding.edtDescription.text.toString()
            val imageUri =selectedImageUri
            val selectedStatus = binding.spinnerStatus.selectedItem?.toString() ?: ""
            val selectedHotel = binding.spinnerHotel.selectedItemPosition.let { pos ->
                if (pos in hotels.indices) hotels[pos] else null
            }
            val selectedRoomType = binding.spinnerRoomType.selectedItemPosition.let { pos ->
                if (pos in roomTypes.indices) roomTypes[pos] else null
            }

            if (selectedHotel == null || selectedRoomType == null) {
                Toast.makeText(this, "Vui lòng chọn khách sạn và loại phòng", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val repositoryRoom = RoomRepository()
            repositoryRoom.updateRoom(
                roomId = roomId.toLong(),
                context = this,
                roomNumber = roomNumber,
                roomTypeId = selectedRoomType.id,
                price = price,
                capacity = capacity,
                description = description,
                status = selectedStatus.toString(),
                hotelId = selectedHotel.id,
                token = session.getToken().toString(),
                imageUri = selectedImageUri,
                onSuccess = {
                    Toast.makeText(this, "Cập nhật phòng thành công", Toast.LENGTH_SHORT).show()
                    finish()
                },
                onError = { error ->
                    Toast.makeText(this, "Cập nhật phòng fall", Toast.LENGTH_SHORT).show()
                }
            )


        }

        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun setupStatusSpinner() {
        val statuses = listOf("Trống", "Đã thuê", "Đang bảo trì")
        val statusAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, statuses)
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerStatus.adapter = statusAdapter
    }

    private fun setupSpinners() {
        val session = SessionManager(this)
        val hotelRepo = HotelRepository()

        // Hotel spinner
        hotelRepo.getHotelByHotelier(
            token = session.getToken().toString(),
            onSuccess = { hotelList ->
                hotels.clear()
                hotels.addAll(hotelList)
                val hotelNames = hotels.map { it.name }
                val hotelAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, hotelNames)
                hotelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.spinnerHotel.adapter = hotelAdapter

                checkSpinnersLoadedAndFetchRoom()
            },
            onError = { error ->
                Toast.makeText(this, "Lỗi tải khách sạn: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        )

        // RoomType spinner
        val roomRepo = RoomRepository()
        roomRepo.fetchRoomTypes(
            onSuccess = { typeList ->
                roomTypes.clear()
                roomTypes.addAll(typeList)
                val typeNames = roomTypes.map { it.name }
                val typeAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, typeNames)
                typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.spinnerRoomType.adapter = typeAdapter

                checkSpinnersLoadedAndFetchRoom()
            },
            onError = { error ->
                Toast.makeText(this, "Lỗi tải loại phòng: ${error.message}", Toast.LENGTH_SHORT).show()
                Log.e("FetchRoomTypes", "Lỗi: ${error.message}", error)
            }
        )
    }

    private fun checkSpinnersLoadedAndFetchRoom() {
        spinnersLoaded++
        if (spinnersLoaded >= 2) {
            fetchRoomData()
        }
    }

    private fun fetchRoomData() {
        val repositoryRoom = RoomRepository()
        repositoryRoom.fetchRoomById(
            roomId = roomId,
            onSuccess = { fetchedRoom ->


                runOnUiThread {
                    binding.edtRoomNumber.setText(fetchedRoom.roomNumber)
                    binding.edtRoomTypeId.setText(fetchedRoom.roomType?.id.toString())
                    binding.edtPrice.setText(fetchedRoom.price.toString())
                    binding.edtCapacity.setText(fetchedRoom.capacity.toString())
                    binding.edtDescription.setText(fetchedRoom.description)

//                    binding.edtImageUri.setText(fetchedRoom.image)

                    // Trạng thái
                    val statusMap = mapOf(
                        "AVAILABLE" to "Trống",
                        "OCCUPIED" to "Đã thuê",
                        "MAINTENANCE" to "Đang bảo trì"
                    )
                    val translatedStatus = statusMap[fetchedRoom.status] ?: "Trống"
                    val statusIndex = (binding.spinnerStatus.adapter as ArrayAdapter<String>)
                        .getPosition(translatedStatus)
                    binding.spinnerStatus.setSelection(statusIndex)

                    // Hotel
                    val hotelIndex = hotels.indexOfFirst { it.id == fetchedRoom.hotel?.id}
                    if (hotelIndex >= 0) {
                        binding.spinnerHotel.setSelection(hotelIndex)
                    }

                    // RoomType
                    val roomTypeIndex = roomTypes.indexOfFirst { it.id == fetchedRoom.roomType?.id }
                    if (roomTypeIndex >= 0) {
                        binding.spinnerRoomType.setSelection(roomTypeIndex)
                    }
                }
                var imageURL="${getString(R.string.localUpload)}${fetchedRoom.image}"
                Log.d("Link anh",imageURL)
                Glide.with(this@EditRoomActivity)
                    .load(imageURL.toString())
                    .placeholder(R.drawable.khachsan)
                    .error(R.drawable.ic_launcher_background)
                    .into(binding.imageView)
            },
            onError = {
                runOnUiThread {
                    Toast.makeText(this, "Lỗi tải thông tin phòng", Toast.LENGTH_SHORT).show()
                }
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
    fun showErrorDialog(message: String) {
        AlertDialog.Builder(this)
            .setTitle("Thông báo")
            .setMessage(message)
            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
            .show()
    }
}
