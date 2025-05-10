package vn.edu.tdc.bookinghotel.Model

class Voucher {
    var title:String=""
    var detail :String=""
    var code:String = ""

    constructor(title: String, detail: String, code: String) {
        this.title = title
        this.detail = detail
        this.code = code

    }
    override fun toString(): String {
        return "$title:$detail:$code"
    }
}