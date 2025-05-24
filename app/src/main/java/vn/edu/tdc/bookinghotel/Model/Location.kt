package vn.edu.tdc.bookinghotel.Model

import com.google.gson.annotations.SerializedName

class Location(
    @SerializedName("locationId")
    val id: Long = 0L,

    @SerializedName("name")
    val name: String = "",

    @SerializedName("description")
    val description: String = "",

    @SerializedName("image")
    val image: String = "",

    @SerializedName("hotels")
    val hotels: List<Hotel> = emptyList()
) {
    override fun toString(): String {
        return "Location(id=$id, name='$name', description='$description', image='$image')"
    }
}
