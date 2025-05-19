package vn.edu.tdc.bookinghotel.Model

import com.google.gson.annotations.SerializedName

class Hotel
    (  @SerializedName("hotelId")
       val id: Long,

       @SerializedName("name")
       val name: String,

       @SerializedName("address")
       val address: String,

       @SerializedName("status")
       val status: String,

       @SerializedName("image")
       val image: String,

       @SerializedName("location")
       val locations: Location,

//       @SerializedName("hotelierId")
//       val hotelier: Hotelier
            ){
    override fun toString(): String {
        return "${id} - ${name} - ${address} - ${image} - ${status}"
    }
}