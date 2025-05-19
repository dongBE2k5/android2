package vn.edu.tdc.bookinghotel.Model

import com.google.gson.annotations.SerializedName

class UserNew
    (  @SerializedName("userId")   
       val id: Long,

       @SerializedName("username")
       val username: String,

       @SerializedName("role")
       val role: String,


       )
{
   override fun toString(): String {
      return "${id} - ${username} - ${role}"
   }
    }
      
