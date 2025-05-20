package vn.edu.tdc.bookinghotel.View

import android.app.Activity
import android.content.Intent
import com.google.android.material.bottomnavigation.BottomNavigationView
import vn.edu.tdc.bookinghotel.*
import vn.edu.tdc.bookinghotel.Activity.AcountActivity
import vn.edu.tdc.bookinghotel.Activity.AcountSuccessActivity
import vn.edu.tdc.bookinghotel.Activity.AdminActivity
import vn.edu.tdc.bookinghotel.Activity.MainActivity
import vn.edu.tdc.bookinghotel.Activity.StoreActivity
import vn.edu.tdc.bookinghotel.Session.SessionManager

object BottomNavHelper {
    fun setup(activity: Activity, bottomNav: BottomNavigationView, currentItemId: Int) {
        bottomNav.selectedItemId = currentItemId


        bottomNav.setOnItemSelectedListener { item ->
            if (item.itemId != currentItemId) {
                val session = SessionManager(activity)
                val intent = when (item.itemId) {

                    R.id.nav_home -> Intent(activity, MainActivity::class.java)
                    R.id.nav_store -> Intent(activity, StoreActivity::class.java)
                    R.id.nav_profile -> Intent(activity,
                        if(session.isLoggedIn() ){
                        AcountSuccessActivity::class.java
                    }else{
                            AcountActivity::class.java
                    }


                    )
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
}