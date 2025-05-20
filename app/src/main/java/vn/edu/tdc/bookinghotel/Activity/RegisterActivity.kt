package vn.edu.tdc.bookinghotel.Activity

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import vn.edu.tdc.bookinghotel.CallAPI.RegisterAPI
import vn.edu.tdc.bookinghotel.Model.RegisterResponse
import vn.edu.tdc.bookinghotel.Model.UserRegister
import vn.edu.tdc.bookinghotel.R
import vn.edu.tdc.bookinghotel.Repository.UserRepository
import vn.edu.tdc.bookinghotel.databinding.RegisterBinding

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: RegisterBinding
    private lateinit var registerAPI: RegisterAPI
    private lateinit var username: String
    private lateinit var password: String
    private lateinit var email: String
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
            val intent =Intent(this, AcountActivity::class.java)
            startActivity(intent)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }

        //ấn đăng ký chuyển đến trang đăng nhập
        binding.btnDangKy.setOnClickListener {
            if (binding.btnDangKy.isEnabled) {
                username = binding.edtUsername.text.toString()
                password = binding.edtPassword.text.toString()
                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show()
                }
                else {
                    val repository= UserRepository(this)
                    repository.fetchregister(
                        username=username,
                        password=password,
                        onSuccess =  {response ->
                            Toast.makeText(this, "Register success", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                            startActivity(intent)
                        },
                        onError={error->
                            Toast.makeText(this, "Login failed: ${error.message}", Toast.LENGTH_SHORT).show()
                        }
                    )
                }

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

                    R.id.nav_store -> {
                        val intent = Intent(this, AcountActivity::class.java)
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
    private fun register(registerRequest: UserRegister) {

        //B2. Dinh nghia doi tuong Retrofit
        val retrofit = Retrofit.Builder()
            .baseUrl(RegisterAPI.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        //B3. Dinh nghia doi tuong LoginAPI
            registerAPI = retrofit.create(RegisterAPI::class.java)
        //B4. Goi ham doc du lieu tu Webservice
        val call = registerAPI.register(registerRequest)
        //B5. Xu li bat dong bo va doc du lieu ve ListView
        call.enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(call: Call<RegisterResponse>, result: Response<RegisterResponse>) {
                // Xu li du lieu doc ve tu Webservice
                // Neu co du lieu moi xu li
                if(result.isSuccessful) {
                    val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                }else {
                    Toast.makeText(this@RegisterActivity, "Đăng ký thất bại", Toast.LENGTH_SHORT).show()
                }

            }

            override fun onFailure(p0: Call<RegisterResponse>, p1: Throwable) {

            }

        })
    }
}