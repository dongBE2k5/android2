package vn.edu.tdc.bookinghotel.Model

class UserDatHang {
    var nameUser: String = ""
    var nameKS: String = ""
    var tongTien: Int = 0

    constructor()

    constructor(nameUser: String, nameKS: String, tongTien: Int) {
        this.nameUser = nameUser
        this.nameKS = nameKS
        this.tongTien = tongTien
    }
}
