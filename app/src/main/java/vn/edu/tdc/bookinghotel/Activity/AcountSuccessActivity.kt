package vn.edu.tdc.bookinghotel.Activity

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import androidx.appcompat.app.AppCompatActivity
import vn.edu.tdc.bookinghotel.R
import vn.edu.tdc.bookinghotel.Repository.CustomerRepository
import vn.edu.tdc.bookinghotel.Session.SessionManager
import vn.edu.tdc.bookinghotel.View.BottomNavHelper
import vn.edu.tdc.bookinghotel.databinding.AcountActiveBinding

class AcountSuccessActivity : AppCompatActivity() {

    private lateinit var binding: AcountActiveBinding

    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AcountActiveBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupFullScreen()

        var session = SessionManager(this)
        Log.d("iduser", session.getIdUser().toString())
        val repositoryCustomer = CustomerRepository()


            repositoryCustomer.fetchCustomerByUser(
                session.getIdUser()!!.toLong(),
                onSuccess = { customer ->
                    binding.userNameAccount.text = session.getUserName()
                    binding.number.text = customer.phone ?: ""
                    binding.email.text = customer.email ?: ""
                    binding.hoTen.text = customer.fullName ?: ""
                },
                onError = {
                    // TODO: handle error if needed (e.g., show Toast)
                }
            )


        val selectedItem = intent.getIntExtra("selected_nav", R.id.nav_profile)
        BottomNavHelper.setup(this, binding.bottomNav, selectedItem)

        binding.btnDangXuat.setOnClickListener {
            session.logout()
            startActivity(Intent(this, AcountActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            })
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            finish()
        }


        binding.btnXemHoSo.setOnClickListener {
            startActivity(Intent(this, EditProfile::class.java))
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }
    }

    private fun setupFullScreen() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.setDecorFitsSystemWindows(false)
            window.insetsController?.let {
                it.hide(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
                it.systemBarsBehavior =
                    WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        } else {
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE

            window.statusBarColor = Color.TRANSPARENT
            window.navigationBarColor = Color.TRANSPARENT
        }
    }
}
