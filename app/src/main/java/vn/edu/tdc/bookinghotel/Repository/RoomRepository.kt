package vn.edu.tdc.bookinghotel.Repository

import android.content.Context
import android.net.Uri
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import vn.edu.tdc.bookinghotel.CallAPI.RoomAPI
import vn.edu.tdc.bookinghotel.Model.Room
import vn.edu.tdc.bookinghotel.Model.RoomType
import vn.edu.tdc.bookinghotel.Response.RoomResponse
import vn.edu.tdc.bookinghotel.Response.RoomSingleResponse
import java.io.File

class RoomRepository {
    private val retrofit = Retrofit.Builder()
        .baseUrl(RoomAPI.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val roomAPI = retrofit.create(RoomAPI::class.java)

    fun fetchRoom(
        onSuccess: (List<Room>) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        val call = roomAPI.getRooms()
        call.enqueue(object : Callback<RoomResponse> {
            override fun onResponse(call: Call<RoomResponse>, response: Response<RoomResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        onSuccess(it.body)
                    } ?: onError(Exception("Response null"))
                } else {
                    onError(Exception("Error code: ${response.code()}"))
                }
            }

            override fun onFailure(call: Call<RoomResponse>, t: Throwable) {
                onError(t)
            }
        })
    }

    fun fetchRoomByHotel(
        hotelId: Long,
        onSuccess: (List<Room>) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        val call = roomAPI.getRoomByHotel(hotelId)
        call.enqueue(object : Callback<RoomResponse> {
            override fun onResponse(call: Call<RoomResponse>, response: Response<RoomResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        onSuccess(it.body)
                    } ?: onError(Exception("Response null"))
                } else {
                    onError(Exception("Error code: ${response.code()}"))
                }
            }

            override fun onFailure(call: Call<RoomResponse>, t: Throwable) {
                onError(t)
            }
        })
    }

    fun fetchRoomById(
        roomId: Long,
        onSuccess: (Room) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        val call = roomAPI.getRoomsById(roomId)
        call.enqueue(object : Callback<RoomSingleResponse> {
            override fun onResponse(call: Call<RoomSingleResponse>, response: Response<RoomSingleResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        onSuccess(it.body)
                    } ?: onError(Exception("Response null"))
                } else {
                    onError(Exception("Error ${response.code()}: ${response.message()}"))
                }
            }

            override fun onFailure(call: Call<RoomSingleResponse>, t: Throwable) {
                onError(t)
            }
        })
    }

    fun fetchRoomTypes(
        onSuccess: (List<RoomType>) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        val call = roomAPI.getRoomTypes()
        call.enqueue(object : Callback<List<RoomType>> {
            override fun onResponse(call: Call<List<RoomType>>, response: Response<List<RoomType>>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        onSuccess(it)
                    } ?: onError(Exception("Response null"))
                } else {
                    onError(Exception("Error code: ${response.code()}"))
                }
            }

            override fun onFailure(call: Call<List<RoomType>>, t: Throwable) {
                onError(t)
            }
        })
    }

    fun addRoom(
        context: Context,
        roomNumber: String,
        roomTypeId: Long,
        price: String,
        capacity: String,
        description: String,
        hotelId: Long,
        token: String,
        imageUri: Uri?,
        onSuccess: () -> Unit,
        onError: (Throwable) -> Unit
    ) {
        val roomNumberBody = roomNumber.toRequestBody("text/plain".toMediaTypeOrNull())
        val roomTypeIdBody = roomTypeId.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val priceBody = price.toRequestBody("text/plain".toMediaTypeOrNull())
        val capacityBody = capacity.toRequestBody("text/plain".toMediaTypeOrNull())
        val descriptionBody = description.toRequestBody("text/plain".toMediaTypeOrNull())
        val hotelIdBody = hotelId.toString().toRequestBody("text/plain".toMediaTypeOrNull())

        val imagePart = imageUri?.let {
            val filePath = getRealPathFromURI(context, it)
            val file = File(filePath)
            val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
            MultipartBody.Part.createFormData("image", file.name, requestFile)
        }

        val call = roomAPI.addRoom(
            roomNumberBody,
            roomTypeIdBody,
            priceBody,
            capacityBody,
            descriptionBody,
            hotelIdBody,
            "Bearer $token",
            imagePart
        )
        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    onSuccess()
                } else {
                    onError(Exception("Error code: ${response.code()}"))
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                onError(t)
            }
        })
    }

    // Hàm lấy đường dẫn thực từ Uri (cần quyền đọc storage)
    fun getRealPathFromURI(context: Context, contentUri: Uri): String {
        var result: String? = null
        val proj = arrayOf(android.provider.MediaStore.Images.Media.DATA)
        val cursor = context.contentResolver.query(contentUri, proj, null, null, null)
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                val columnIndex = cursor.getColumnIndexOrThrow(android.provider.MediaStore.Images.Media.DATA)
                result = cursor.getString(columnIndex)
            }
            cursor.close()
        }
        if (result == null) {
            result = contentUri.path
        }
        return result ?: ""
    }
}
