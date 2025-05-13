package vn.edu.tdc.bookinghotel.Model

import com.google.gson.annotations.SerializedName

class User(
    val username: String,
    val password: String,
    val fullname: String,
    val email: String,
    val phone: String,
    val cccd: String,
){}

class UserRegister(
    val username: String,
    val password: String,
    val email: String,
)

class UserLogin(
    val username: String,
    val password: String,
)

data class LoginResponse(
    @SerializedName("token")
    val token: String,
    @SerializedName("id")
    val id: Long,
    @SerializedName("status")
    val status: String
)

data class RegisterResponse(
    @SerializedName("id")
    val id: Long,
    @SerializedName("username")
    val username: String,
    @SerializedName("password")
    val password: String,
    @SerializedName("fullname")
    val fullname: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("phone")
    val phone: String,
    val cccd: String,
)
