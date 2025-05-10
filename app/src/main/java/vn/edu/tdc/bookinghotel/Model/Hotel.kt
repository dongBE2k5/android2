package vn.edu.tdc.bookinghotel.Model

class Hotel {
    var thanhpho:String=""
    var name:String=""
    var status :String=""
    var feedback:String =""
    var description:String = ""
    var image:Int = 0

    constructor(thanhpho: String,name: String, status: String,  feedback:String,description: String,image:Int) {
        this.thanhpho=thanhpho
        this.name = name
        this.status = status
        this.feedback=feedback
        this.description = description
        this.image=image
    }
    override fun toString(): String {
        return "$thanhpho:$name:$status:$feedback:$description:$image"
    }
}