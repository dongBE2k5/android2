package vn.edu.tdc.bookinghotel.Model

import java.io.Serializable

class Room : Serializable {
    var name: String = ""
    var status: String = ""
    var shortdescription: String = ""
    var longdescription: String = ""
    var tiennghi: String = ""
    var chitiet: String = ""
    var price: String = ""
    var image: Int = 0

    constructor(
        name: String,
        status: String,
        shortdescription: String,
        longdescription: String,
        tiennghi: String,
        chitiet: String,
        price: String,
        image: Int
    ) {
        this.name = name
        this.status = status
        this.shortdescription = shortdescription
        this.longdescription = longdescription
        this.tiennghi = tiennghi
        this.chitiet = chitiet
        this.price = price
        this.image = image
    }

    override fun toString(): String {
        return "$name:$status:$shortdescription:$longdescription:$tiennghi:$chitiet:$price:$image"
    }
}
