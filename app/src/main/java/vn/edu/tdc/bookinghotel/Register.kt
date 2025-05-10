package vn.edu.tdc.bookinghotel

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import vn.edu.tdc.bookinghotel.databinding.RegisterBinding

class Register : AppCompatActivity() {
    private lateinit var binding: RegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = RegisterBinding.inflate(layoutInflater)
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


        //goi lai trang account activity
        binding.btnBack.setOnClickListener {
            val intent =Intent(this,AcountActivity::class.java)
            startActivity(intent)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }

        //ấn đăng ký chuyển đến trang đăng nhập
        binding.btnDangKy.setOnClickListener {
            if (binding.btnDangKy.isEnabled) {
                val intent = Intent(this,Login::class.java)
                startActivity(intent)
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            }
        }

        // tick choọn đồng ý mới cho phép đăng ky
        binding.btnDangKy.isEnabled = false  // Ban đầu không cho nhấn
        binding.btnDangKy.alpha = 0.5f       // Làm mờ nút khi bị khóa

        binding.checkAgree.setOnCheckedChangeListener { _, isChecked ->
            binding.btnDangKy.isEnabled = isChecked
            //làm mo nut dang ky
            binding.btnDangKy.alpha = if (isChecked) 1f else 0.5f
        }


        //lang nghe nguoi dung chon nav, hien tai la tai khoan
        val selectedItem = intent.getIntExtra("selected_nav", R.id.nav_profile)
        binding.bottomNav.selectedItemId = selectedItem

        binding.bottomNav.setOnItemSelectedListener { item ->
            if (item.itemId != selectedItem) {
                when (item.itemId) {
                    R.id.nav_home -> {
                        val intent = Intent(this, MainActivity::class.java)
                        intent.putExtra("selected_nav", R.id.nav_home)
                        startActivity(intent)
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                        finish()
                        true
                    }
                    R.id.nav_search -> {
                        val intent = Intent(this, Hotel_BookingActivity::class.java)
                        intent.putExtra("selected_nav", R.id.nav_search)
                        startActivity(intent)
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                        finish()
                        true
                    }
                    R.id.nav_store -> {
                        val intent = Intent(this, StoreActivity::class.java)
                        intent.putExtra("selected_nav", R.id.nav_store)
                        startActivity(intent)
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                        finish()
                        true
                    }
                    R.id.nav_profile -> {
                        val intent = Intent(this, AcountActivity::class.java)
                        intent.putExtra("selected_nav", R.id.nav_profile)
                        startActivity(intent)
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                        finish()
                        true
                    }
                    else -> false
                }
            } else {
                true
            }
        }
    }
}