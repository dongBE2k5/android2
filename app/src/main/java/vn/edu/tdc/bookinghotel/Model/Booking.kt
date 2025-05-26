package vn.edu.tdc.bookinghotel.Model

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

class Booking
    (  @SerializedName("bookingId")   
       val id: Long,

       @SerializedName("customer")
       val customer: Customer,

       @SerializedName("room")
       val room: Room,

       @SerializedName("price")
       val price: BigDecimal,

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
    @SerializedName("customerId")
    val customerId: Long,

    @SerializedName("roomId")
    val roomId: Long,

    @SerializedName("price")
    val price: BigDecimal,

    @SerializedName("checkinDate")
    val checkinDate: String,  // định dạng yyyy-MM-dd

    @SerializedName("checkoutDate")
    val checkoutDate: String  // định dạng yyyy-MM-dd
)
