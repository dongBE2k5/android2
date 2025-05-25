package vn.edu.tdc.bookinghotel.Activity

import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import vn.edu.tdc.bookinghotel.Model.Hotel
import vn.edu.tdc.bookinghotel.Model.RoomType
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = EditRoomLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        roomId = intent.getLongExtra("roomId", 0L)
        Log.d("roomId", roomId.toString())

        setupStatusSpinner()
        setupSpinners()

        binding.btnSaveRoom.setOnClickListener {
            val roomNumber = binding.edtRoomNumber.text.toString()
            val roomTypeId = binding.edtRoomTypeId.text.toString()
            val price = binding.edtPrice.text.toString()
            val capacity = binding.edtCapacity.text.toString()
            val description = binding.edtDescription.text.toString()
            val imageUri = binding.edtImageUri.text.toString()
            val selectedHotel = binding.spinnerHotel.selectedItem?.toString() ?: ""
            val selectedRoomType = binding.spinnerRoomType.selectedItem?.toString() ?: ""
            val selectedStatus = binding.spinnerStatus.selectedItem?.toString() ?: ""

            Toast.makeText(this, "Lưu phòng: $roomNumber, $selectedHotel", Toast.LENGTH_SHORT).show()
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
                    binding.edtImageUri.setText(fetchedRoom.image)

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
            },
            onError = {
                runOnUiThread {
                    Toast.makeText(this, "Lỗi tải thông tin phòng", Toast.LENGTH_SHORT).show()
                }
            }
        )
    }
}
