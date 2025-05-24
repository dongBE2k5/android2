package vn.edu.tdc.bookinghotel.Repository

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


class RoomRepository {
    private val retrofit = Retrofit.Builder()
        .baseUrl(RoomAPI.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val roomAPI = retrofit.create(RoomAPI::class.java)
    fun fetchRoom(
        onSuccess:(List<Room>)->Unit,
        onError: (Throwable)->Unit
    ){
        val call = roomAPI.getRooms()
        call.enqueue(object : Callback<RoomResponse> {
            override fun onResponse(call: Call<RoomResponse>, response: Response<RoomResponse>) {
                if (response.isSuccessful){
                    response.body()?.let {
                        onSuccess(it.body)
                    } ?: onError(Exception("Response null"))
            }else{
                    onError(Exception("Error code: ${response.code()}"))
                }
            }

            override fun onFailure(call: Call<RoomResponse>, t: Throwable) {
              onError(t)
            }

        })
    }


    fun fetchRoomByHotel(
        hotelId:Long,
        onSuccess:(List<Room>)->Unit,
        onError: (Throwable)->Unit
    ){
        val call = roomAPI.getRoomByHotel(hotelId)
        call.enqueue(object : Callback<RoomResponse> {
            override fun onResponse(call: Call<RoomResponse>, response: Response<RoomResponse>) {
                if (response.isSuccessful){
                    response.body()?.let {
                        onSuccess(it.body)
                    } ?: onError(Exception("Response null"))
                }else{
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






}



