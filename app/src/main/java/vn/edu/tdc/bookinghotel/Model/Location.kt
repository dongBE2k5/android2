package vn.edu.tdc.bookinghotel.Model

import com.google.gson.annotations.SerializedName

class Location(
    @SerializedName("locationId")
    val id: Long,
    @SerializedName("name")
    val name: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("image")
    val image: String,
    @SerializedName("hotels")
    val hotels: List<Hotel>
) {
    override fun toString(): String {
        return "Location(id=$id, name='$name', description='$description', image='$image', hotels=$hotels)"
    }
}
