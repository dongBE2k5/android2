package vn.edu.tdc.bookinghotel.Model

import com.google.gson.annotations.SerializedName

class Booking
    (  @SerializedName("bookingId")   
       val id: Long,

       @SerializedName("customer")
       val customer: Customer,

       @SerializedName("room")
       val room: Room,

       @SerializedName("checkInDate")
       val checkinDate: String,

       @SerializedName("checkOutDate")
       val checkoutDate: String,

       @SerializedName("status")
       val status: String,
            ){
    override fun toString(): String {
        return "${id} - ${customer} - ${room} - ${checkinDate} - ${checkoutDate} - ${status}"
    }
}

data class BookingRequest(
    val customerId: Long,
    val roomId: Long,
    val checkinDate: String,
    val checkoutDate: String,
)
