package vn.edu.tdc.bookinghotel.Model

import com.google.gson.annotations.SerializedName

class Customer
    (  @SerializedName("customerId")   
       val id: Long,

       @SerializedName("fullname")
       val fullName: String,

       @SerializedName("email")
       val email: String,

       @SerializedName("phone")
       val phone: String,

       @SerializedName("cccd")
       val cccd: String?,

       @SerializedName("user")
       val user: UserNew,
            ){
    override fun toString(): String {
        return "${id} - ${fullName} - ${email} - ${phone} - ${cccd} - ${user}"
    }
}

data class CustomerUpdate(
    @SerializedName("fullName")
    val fullName: String,
    val cccd: String,
    val phone: String
    )

