package vn.edu.tdc.bookinghotel

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import vn.edu.tdc.bookinghotel.databinding.LoginBinding

class Login : AppCompatActivity() {
    private lateinit var binding: LoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = LoginBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        //gọi trang account active
        binding.btnDangNhap.setOnClickListener {
            val intent =Intent(this,AcountActive::class.java)
            startActivity(intent)
        }

        //gọi 3 nút button dưới
        binding.bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                //nút home
                R.id.nav_home -> {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    true
                }
                //nút tìm kiếm
                R.id.nav_search -> {
                    val intent = Intent(this, AcountActivity::class.java)
                    startActivity(intent)
                    true
                }
                //nút tài khoản
                R.id.nav_profile -> {
                    val intent = Intent(this, AcountActivity::class.java)
                    startActivity(intent)
                    true
                }

                else -> false
            }
        }
    }
}