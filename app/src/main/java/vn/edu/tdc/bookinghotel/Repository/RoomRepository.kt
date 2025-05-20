package vn.edu.tdc.bookinghotel.Repository

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

import vn.edu.tdc.bookinghotel.CallAPI.RoomAPI
import vn.edu.tdc.bookinghotel.Model.Room
import vn.edu.tdc.bookinghotel.Response.RoomRespose
import java.lang.Error

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
        call.enqueue(object : Callback<RoomRespose> {
            override fun onResponse(call: Call<RoomRespose>, response: Response<RoomRespose>) {
                if (response.isSuccessful){
                    response.body()?.let {
                        onSuccess(it.body)
                    } ?: onError(Exception("Response null"))
            }else{
                    onError(Exception("Error code: ${response.code()}"))
                }
            }

            override fun onFailure(call: Call<RoomRespose>, t: Throwable) {
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
        call.enqueue(object : Callback<RoomRespose> {
            override fun onResponse(call: Call<RoomRespose>, response: Response<RoomRespose>) {
                if (response.isSuccessful){
                    response.body()?.let {
                        onSuccess(it.body)
                    } ?: onError(Exception("Response null"))
                }else{
                    onError(Exception("Error code: ${response.code()}"))
                }
            }

            override fun onFailure(call: Call<RoomRespose>, t: Throwable) {
                onError(t)
            }

        })
    }
    fun fetchRoomById(
        roomId:Long,
        onSuccess:(List<Room>)->Unit,
        onError: (Throwable)->Unit
    ){
        val call = roomAPI.getRoomsById(roomId)
        call.enqueue(object : Callback<RoomRespose> {
            override fun onResponse(call: Call<RoomRespose>, response: Response<RoomRespose>) {
                if (response.isSuccessful){
                    response.body()?.let {
                        onSuccess(it.body)
                    } ?: onError(Exception("Response null"))
                }else{
                    onError(Exception("Error code: ${response.code()}"))
                }
            }

            override fun onFailure(call: Call<RoomRespose>, t: Throwable) {
                onError(t)
            }

        })
    }

}



