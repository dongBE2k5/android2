package vn.edu.tdc.bookinghotel.View

import android.app.Activity
import android.content.Intent
import android.view.Menu
import com.google.android.material.bottomnavigation.BottomNavigationView
import vn.edu.tdc.bookinghotel.*
import vn.edu.tdc.bookinghotel.Activity.*
import vn.edu.tdc.bookinghotel.Session.SessionManager

object BottomNavHelper {

    fun setup(activity: Activity, bottomNav: BottomNavigationView, currentItemId: Int) {
        val session = SessionManager(activity)
        val role = session.getRoleUserNamer()  // SỬ DỤNG HÀM NÀY THAY VÌ getRole()
        val isLoggedIn = session.isLoggedIn()

        setupVisibility(bottomNav.menu, isLoggedIn, role)

        bottomNav.selectedItemId = currentItemId

        bottomNav.setOnItemSelectedListener { item ->
            if (item.itemId != currentItemId) {
                val intent = when (item.itemId) {
                    R.id.nav_home -> Intent(activity, MainActivity::class.java)
                    R.id.nav_store -> {
                        if (isLoggedIn) {
                            Intent(activity, StoreActivity::class.java)
                        } else {
                            Intent(activity, AcountActivity::class.java) // yêu cầu đăng nhập
                        }
                    }
                    R.id.nav_profile -> {
                        if (isLoggedIn) {
                            Intent(activity, AcountSuccessActivity::class.java)
                        } else {
                            Intent(activity, AcountActivity::class.java)
                        }
                    }
                    R.id.nav_admin -> Intent(activity, AdminActivity::class.java)
                    else -> null
                }

                intent?.let {
                    it.putExtra("selected_nav", item.itemId)
                    activity.startActivity(it)
                    activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                    activity.finish()
                }
            }
            true
        }
    }


    private fun setupVisibility(menu: Menu, isLoggedIn: Boolean, role: String?) {
        menu.findItem(R.id.nav_admin)?.isVisible = isLoggedIn && role == "ROLE_ADMIN"
        menu.findItem(R.id.nav_store)?.isVisible = isLoggedIn && role != "ROLE_ADMIN"
        menu.findItem(R.id.nav_home)?.isVisible = true
        menu.findItem(R.id.nav_profile)?.isVisible = true
    }
}
