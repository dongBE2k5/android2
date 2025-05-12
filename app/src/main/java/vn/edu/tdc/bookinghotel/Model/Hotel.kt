package vn.edu.tdc.bookinghotel.Model

class Hotel {
    val id: Long
    val name: String
    val address: String
    val phone: String
    val image: String
    val email: String
    val status: String

    constructor(
        id: Long,
        name: String,
        address: String,
        phone: String,
        image: String,
        email: String,
        status: String
    ) {
        this.id = id
        this.name = name
        this.address = address
        this.phone = phone
        this.image = image
        this.email = email
        this.status = status
    }
}