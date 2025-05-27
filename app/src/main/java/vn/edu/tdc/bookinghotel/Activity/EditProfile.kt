package vn.edu.tdc.bookinghotel.Activity

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import vn.edu.tdc.bookinghotel.Model.CustomerUpdateUser
import vn.edu.tdc.bookinghotel.R
import vn.edu.tdc.bookinghotel.Repository.CustomerRepository
import vn.edu.tdc.bookinghotel.Session.SessionManager
import vn.edu.tdc.bookinghotel.View.BottomNavHelper
import vn.edu.tdc.bookinghotel.databinding.EditProfileAccountBinding
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.concurrent.TimeUnit

class EditProfile: AppCompatActivity() {
    private lateinit var binding: EditProfileAccountBinding

    private lateinit var inputMethodManager: InputMethodManager


    lateinit var tvBirthday: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        binding = EditProfileAccountBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        //full màn hiình
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.navigationBarColor = Color.TRANSPARENT
            window.statusBarColor = Color.TRANSPARENT
        }
        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                )
        setContentView(binding.root)

        window.setDecorFitsSystemWindows(false)

        window.insetsController?.let { controller ->
            controller.hide(
                android.view.WindowInsets.Type.statusBars() or android.view.WindowInsets.Type.navigationBars()
            )
            controller.systemBarsBehavior = android.view.WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }



        val session=SessionManager(this)
        val repositoryCustomer=CustomerRepository()
        session.getIdUser()?.let {
            repositoryCustomer.fetchCustomerByUser(
                it.toLong(),
                onSuccess = {resources->
                  binding.edtCCCD.setText(resources.cccd?:"")
                    binding.edtNumber.setText(resources.phone?:"")
                    binding.edtMail.setText(resources.email?:"")
                    binding.HoTen.setText(resources.fullName?:"")
                    binding.tvBirthday.setText(resources.ngaySinh?:"")

                    binding.DiaChi.setText(resources.diaChi)
                    binding.tvGender.setText(resources.gioiTinh)
                },
                onError = {

                }
            )
        }

        inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager

        // Disable EditTexts ban đầu
        binding.HoTen.isEnabled = false
        binding.DiaChi.isEnabled = false
        binding.tvBirthday.isEnabled = false
        binding.tvGender.isEnabled = false
        binding.edtMail.isEnabled = false
        binding.edtNumber.isEnabled = false
        binding.edtCCCD.isEnabled = false

        binding.tvEditToggle.setOnClickListener {
            if (binding.tvEditToggle.text == "Chỉnh sửa") {
                binding.tvEditToggle.text = "Lưu"
                binding.tvEditToggle.setTextColor(ContextCompat.getColor(this, android.R.color.holo_green_dark))

                // Cho phép nhập
                binding.edtMail.isEnabled = true
                binding.edtNumber.isEnabled = true
                binding.tvBirthday.isEnabled = true
                binding.tvGender.isEnabled = true
                binding.HoTen.isEnabled = true
                binding.DiaChi.isEnabled = true
                binding.edtCCCD.isEnabled = true

            } else if (binding.tvEditToggle.text == "Lưu") {
                // Thu thập dữ liệu mới
                val email = binding.edtMail.text.toString()
                val phone = binding.edtNumber.text.toString()
                val cccd = binding.edtCCCD.text.toString()
                val diaChi = binding.DiaChi.text.toString()
                var ngaySinh = binding.tvBirthday.text.toString()
                val gioiTinh = binding.tvGender.text.toString()
                val fullName = binding.HoTen.text.toString()
                ngaySinh=convertDateFormat(ngaySinh)

                val customer = CustomerUpdateUser(
                    fullName, cccd, phone, email, diaChi,  gioiTinh,ngaySinh
                )

                session.getIdUser()?.let { idUser ->
                    repositoryCustomer.updateCustomerByIdUser(
                        idUser.toLong(),
                        customer,
                        onSuccess = {
                            Toast.makeText(this, "Cập nhật thành công", Toast.LENGTH_SHORT).show()
                            // Khóa các trường sau khi lưu
                            binding.edtMail.isEnabled = false
                            binding.edtNumber.isEnabled = false
                            binding.tvBirthday.isEnabled = false
                            binding.tvGender.isEnabled = false
                            binding.HoTen.isEnabled = false
                            binding.DiaChi.isEnabled = false
                            binding.edtCCCD.isEnabled = false

                            binding.tvEditToggle.text = "Chỉnh sửa"
                            binding.tvEditToggle.setTextColor(ContextCompat.getColor(this, android.R.color.holo_blue_dark))
                        },
                        onError = { error ->
                            Toast.makeText(this, "Cập nhật thất bại: ${error.message}", Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            }
        }


        //goi lai trang account active
        binding.btnBack.setOnClickListener {
            val intent =Intent(this, AcountSuccessActivity::class.java)
            startActivity(intent)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }

        // Bottom Navigation xử lý chuyển activity
        val selectedItem = intent.getIntExtra("selected_nav", R.id.nav_profile)
        BottomNavHelper.setup(this, binding.bottomNav, selectedItem)

        //gọi hàm ngy sinh
        binding.tvBirthday.setOnClickListener {
            showDatePicker()
        }


        //gọi gioi tinh nam nu

        binding.tvGender.setOnClickListener {
            val genderOptions = arrayOf("Nam", "Nữ")
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Chọn giới tính")
                .setItems(genderOptions) { _, which ->
                    val selectedGender = genderOptions[which]
                    binding.tvGender.text = selectedGender
                }
                .show()
        }
    }

    //hàm hiển thi lich
    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePicker = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
            val selectedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
            binding.tvBirthday.text = selectedDate
        }, year, month, day)

        datePicker.show()
    }
    private fun getDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePicker = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
            val selectedDate = String.format("%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay)
            binding.tvBirthday.text = selectedDate
        }, year, month, day)

        datePicker.show()
    }

    private fun convertDateFormat(inputDate: String): String {
        return try {
            val inputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val date = inputFormat.parse(inputDate)
            outputFormat.format(date!!)
        } catch (e: Exception) {
            ""
        }
    }
}