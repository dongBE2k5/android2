package vn.edu.tdc.bookinghotel.Model

import com.google.gson.annotations.SerializedName

class Customer(
    @SerializedName("customerId")
    val id: Long,

    @SerializedName("fullname")
    val fullName: String,

    @SerializedName("email")
    val email: String,

    @SerializedName("phone")
    val phone: String,

    @SerializedName("cccd")
    val cccd: String?,

    @SerializedName("address")
    val diaChi: String?,

    @SerializedName("gender")
    val gioiTinh: String?,

    @SerializedName("dob")
    val ngaySinh: String?,

    @SerializedName("user")
    val user: UserNew
) {
    override fun toString(): String {
        return "$id - $fullName - $email - $phone - $cccd - $diaChi - $gioiTinh - $ngaySinh - $user"
    }
}
data class CustomerUpdate(
    @SerializedName("fullName")
    val fullName: String,

    val cccd: String,
    val phone: String,
    val diaChi: String,
    val gioiTinh: String,
    val ngaySinh: String
)

data class CustomerUpdated(
    @SerializedName("fullName")
    val fullName: String,

    val cccd: String,
    val phone: String
)


data class CustomerUpdateUser(
    @SerializedName("fullName")
    val fullName: String,

    val cccd: String,
    val phone: String,
    val email: String,
    @SerializedName("address")
    val diaChi: String,
    @SerializedName("gender")
    val gioiTinh: String,

    @SerializedName("dob")
    val ngaySinh: String
)


