package vn.edu.tdc.bookinghotel

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import vn.edu.tdc.bookinghotel.databinding.AccountBinding
import vn.edu.tdc.bookinghotel.databinding.AcountActiveBinding
import vn.edu.tdc.bookinghotel.databinding.HomePageLayoutBinding

class AcountActivity :AppCompatActivity(){
    private lateinit var binding: AccountBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding= AccountBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //gọi nút back trở về home
        binding.btnBack.setOnClickListener {
            val intent =Intent(this,MainActivity::class.java)
            startActivity(intent)
        }

        //gọi nút login
        binding.login.setOnClickListener {
            val intent =Intent(this,Login::class.java)
            startActivity(intent)
        }

        //gọi nút register
        binding.register.setOnClickListener {
            val intent =Intent(this,Register::class.java)
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