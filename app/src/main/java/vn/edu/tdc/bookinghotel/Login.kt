package vn.edu.tdc.bookinghotel

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import vn.edu.tdc.bookinghotel.databinding.LoginBinding

class Login : AppCompatActivity() {
    private lateinit var binding: LoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = LoginBinding.inflate(layoutInflater)
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


        //gọi trang account active
        binding.btnDangNhap.setOnClickListener {
            val intent =Intent(this,AcountActive::class.java)
            startActivity(intent)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }

        //goi lai trang account activity
        binding.btnBack.setOnClickListener {
            val intent =Intent(this,AcountActivity::class.java)
            startActivity(intent)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }

        //gọi trang dang ky
        binding.tvDangKy.setOnClickListener {
            val intent =Intent(this,Register::class.java)
            startActivity(intent)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
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
                        val intent = Intent(this, AcountActivity::class.java)
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
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                        startActivity(intent)
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