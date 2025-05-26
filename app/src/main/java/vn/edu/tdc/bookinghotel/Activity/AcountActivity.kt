package vn.edu.tdc.bookinghotel.Activity

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import vn.edu.tdc.bookinghotel.R
import vn.edu.tdc.bookinghotel.View.BottomNavHelper
import vn.edu.tdc.bookinghotel.databinding.AccountBinding

class AcountActivity :AppCompatActivity(){
    private lateinit var binding: AccountBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding= AccountBinding.inflate(layoutInflater)
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



        //gọi nút back trở về home
        binding.btnBack.setOnClickListener {
            val intent =Intent(this, MainActivity::class.java)
            startActivity(intent)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }

        //gọi nút login

        binding.login.setOnClickListener {
            val intent =Intent(this, LoginActivity::class.java)
            startActivity(intent)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }

        //gọi nút register
        binding.register.setOnClickListener {
            val intent =Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }


        // Bottom Navigation xử lý chuyển activity
        val selectedItem = intent.getIntExtra("selected_nav", R.id.nav_profile)
        BottomNavHelper.setup(this, binding.bottomNav, selectedItem)

    }
}