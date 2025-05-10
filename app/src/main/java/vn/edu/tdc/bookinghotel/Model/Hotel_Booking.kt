package vn.edu.tdc.bookinghotel.Model

class Hotel_Booking {
    var bookingId: Int = 0
    var customerId: Int = 0
    var roomId: Int = 0
    var checkInDate: String = ""
    var checkOutDate: String = ""
    var status: String = ""
    var roomName: String = ""
    var image: Int = 0

    // Trường mới
    var contactInfo: String = ""
    var voucherCode: String = ""
    var paymentMethods: String = ""
    var totalAmount: String = "0 VNĐ"
    var isInsuranceSelected: Boolean = false
    var insurancePrice: String = "0 VNĐ"

    constructor(
        bookingId: Int,
        customerId: Int,
        roomId: Int,
        checkInDate: String,
        checkOutDate: String,
        status: String,
        roomName: String,
        image: Int,
        contactInfo: String,
        voucherCode: String,
        paymentMethods: String,
        totalAmount: String,
        isInsuranceSelected: Boolean,
        insurancePrice: String
    ) {
        this.bookingId = bookingId
        this.customerId = customerId
        this.roomId = roomId
        this.checkInDate = checkInDate
        this.checkOutDate = checkOutDate
        this.status = status
        this.roomName = roomName
        this.image = image
        this.contactInfo = contactInfo
        this.voucherCode = voucherCode
        this.paymentMethods = paymentMethods
        this.totalAmount = totalAmount
        this.isInsuranceSelected = isInsuranceSelected
        this.insurancePrice = insurancePrice
    }

    override fun toString(): String {
        return "$bookingId:$customerId:$roomId:$checkInDate:$checkOutDate:$status:$roomName:$image:" +
                "$contactInfo:$voucherCode:$paymentMethods:$totalAmount:$isInsuranceSelected:$insurancePrice"
    }
}
