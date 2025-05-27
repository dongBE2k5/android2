package vn.edu.tdc.bookinghotel.Activity

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import vn.edu.tdc.bookinghotel.CallAPI.LoginAPI
import vn.edu.tdc.bookinghotel.Model.LoginResponse
import vn.edu.tdc.bookinghotel.Model.UserLogin
import vn.edu.tdc.bookinghotel.R
import vn.edu.tdc.bookinghotel.Repository.UserRepository
import vn.edu.tdc.bookinghotel.Session.SessionManager
import vn.edu.tdc.bookinghotel.View.BottomNavHelper
import vn.edu.tdc.bookinghotel.databinding.LoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: LoginBinding
    private lateinit var loginAPI: LoginAPI
    private lateinit var username: String
    private lateinit var password: String
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

        window.setDecorFitsSystemWindows(false)

        window.insetsController?.let { controller ->
            controller.hide(
                android.view.WindowInsets.Type.statusBars() or android.view.WindowInsets.Type.navigationBars()
            )
            controller.systemBarsBehavior = android.view.WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }


        //gọi trang account active
        binding.btnDangNhap.setOnClickListener {

            username = binding.edtUsername.text.toString()
            password = binding.edtPassword.text.toString()
            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập tên đăng nhập và mật khẩu", Toast.LENGTH_SHORT)
                    .show()
            }
            else {
            val repository=UserRepository(this)
            repository.fetchlogin(
                username=username,
                password=password,
                   onSuccess =  {response ->
                       Toast.makeText(this, "Login success", Toast.LENGTH_SHORT).show()

                       val intent =Intent(this@LoginActivity, AcountSuccessActivity::class.java)
                       startActivity(intent)
                   },
                onError={error->
                    showErrorDialog(error.message ?: "Sai tài khoản hoặc mật khẩu không chính xác.")
                }
            )



            Log.d("user",username)
            Log.d("user",password)


            }
//

        }

        //goi lai trang account activity
        binding.btnBack.setOnClickListener {
            val intent =Intent(this, AcountActivity::class.java)
            startActivity(intent)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }

        //gọi trang dang ky
        binding.tvDangKy.setOnClickListener {
            val intent =Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }


        // Bottom Navigation setup
        val selectedItem = intent.getIntExtra("selected_nav", R.id.nav_profile)
        BottomNavHelper.setup(this, binding.bottomNav, selectedItem)

    }

    private fun login(loginRequest: UserLogin) {

        //B2. Dinh nghia doi tuong Retrofit
        val retrofit = Retrofit.Builder()
            .baseUrl(LoginAPI.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        //B3. Dinh nghia doi tuong LoginAPI
            loginAPI = retrofit.create(LoginAPI::class.java)
        //B4. Goi ham doc du lieu tu Webservice
        val call = loginAPI.login(loginRequest)
        //B5. Xu li bat dong bo va doc du lieu ve ListView
        call.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, result: Response<LoginResponse>) {
                // Xu li du lieu doc ve tu Webservice
                // Neu co du lieu moi xu li
                if(result.isSuccessful) {
                    val loginResponse = result.body()
                    // Xu li nullable
                    loginResponse?.let {
                        // Luu token vao SharedPreferences
                        val session = SessionManager(this@LoginActivity)
                        session.saveToken(loginResponse.token)
                        session.saveRoleUserName(loginResponse.role) // Lưu role nếu cần phân quyền
                        session.saveUserName(loginResponse.username)
                        session.saveIdUser(loginResponse.idUser) // nếu có id

                        // Chuyen huong den MainActivity

                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                    }

                }else {
                    Toast.makeText(this@LoginActivity, "Sai Username hoặc Password", Toast.LENGTH_SHORT).show()

                }
            }

            override fun onFailure(call: Call<LoginResponse>, error: Throwable) {
                Toast.makeText(this@LoginActivity, "Lỗi: ${error.message}", Toast.LENGTH_LONG).show()

            }

        })
    }
    fun showErrorDialog(message: String) {
        AlertDialog.Builder(this)
            .setTitle("Thông báo")
            .setMessage(message)
            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
            .show()
    }
}