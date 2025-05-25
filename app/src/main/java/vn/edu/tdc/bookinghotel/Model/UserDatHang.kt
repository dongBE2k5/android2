package vn.edu.tdc.bookinghotel.Model

class UserDatHang {
    var nameUser: String = ""
    var nameKS: String = ""
    var tongTien: Int = 0
    var bookingId: Long= 0L


    constructor()
    constructor(nameUser: String, nameKS: String, tongTien: Int,bookingId: Long) {
        this.nameUser = nameUser
        this.nameKS = nameKS
        this.tongTien = tongTien
        this.bookingId=bookingId
    }
}
