package vn.edu.tdc.bookinghotel.Activity

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import vn.edu.tdc.bookinghotel.R
import vn.edu.tdc.bookinghotel.View.BottomNavHelper
import vn.edu.tdc.bookinghotel.databinding.EditProfileAccountBinding
import java.util.Calendar

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


        inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager

        // Disable EditTexts ban đầu
        binding.HoTen.isEnabled = false
        binding.DiaChi.isEnabled = false
        binding.tvBirthday.isEnabled = false
        binding.tvGender.isEnabled = false
        binding.edtMail.isEnabled = false
        binding.edtNumber.isEnabled = false

        binding.tvEditToggle.setOnClickListener {
            if (binding.tvEditToggle.text == "Chỉnh sửa") {
                binding.tvEditToggle.text = "Hủy"
                binding.tvEditToggle.setTextColor(ContextCompat.getColor(this, android.R.color.holo_red_dark))

                // Hiện và cho nhập
                binding.edtMail.isEnabled = true
                binding.edtNumber.isEnabled = true
                binding.tvBirthday.isEnabled = true
                binding.tvGender.isEnabled = true
                binding.HoTen.isEnabled = true
                binding.DiaChi.isEnabled = true
            } else {
                binding.tvEditToggle.text = "Chỉnh sửa"
                binding.tvEditToggle.setTextColor(ContextCompat.getColor(this, android.R.color.holo_blue_dark))

                // Ẩn và khoá nhập
                binding.tvBirthday.isEnabled = false
                binding.tvGender.isEnabled = false
                binding.HoTen.isEnabled = false
                binding.DiaChi.isEnabled = false
                binding.edtMail.isEnabled = false
                binding.edtNumber.isEnabled = false
            }
        }

        //goi lai trang account active
        binding.btnBack.setOnClickListener {
            val intent =Intent(this, AcountSuccessActivity::class.java)
            startActivity(intent)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }

        // Bottom Navigation xử lý chuyển activity
        val selectedItem = intent.getIntExtra("selected_nav", R.id.nav_home)
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
}