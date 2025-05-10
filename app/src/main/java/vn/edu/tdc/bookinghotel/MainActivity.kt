package vn.edu.tdc.bookinghotel

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import vn.edu.tdc.bookinghotel.Adapters.MyHotelRecyclerViewAdapter
import vn.edu.tdc.bookinghotel.Adapters.MyVoucherRecyclerViewAdapter
import vn.edu.tdc.bookinghotel.Model.Hotel
import vn.edu.tdc.bookinghotel.Model.Voucher
import vn.edu.tdc.bookinghotel.databinding.HomePageLayoutBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding:HomePageLayoutBinding
    private lateinit var listHotel: ArrayList<Hotel>
    private lateinit var listVoucher: ArrayList<Voucher>

    private lateinit var adapter: MyHotelRecyclerViewAdapter
    private lateinit var adapterVoucher: MyVoucherRecyclerViewAdapter

    private var itemSelected = -1
    private var oldColor: Int = 0
    private lateinit var oldView: View


    //voucher

    val vouchers = arrayListOf(
        Voucher("Giảm đến 1 triệu tất cả Khách Sạn","Đặt khách sạn từ 2.5 triệu","VNEPICTHANKYOU"),
        Voucher("Giảm đến 1 triệu tất cả Khách Sạn","Đặt khách sạn từ 2.5 triệu","VNEPICTHANKYOU"),
        Voucher("Giảm đến 1 triệu tất cả Khách Sạn","Đặt khách sạn từ 2.5 triệu","VNEPICTHANKYOU"),
        Voucher("Giảm đến 1 triệu tất cả Khách Sạn","Đặt khách sạn từ 2.5 triệu","VNEPICTHANKYOU"),
        Voucher("Giảm đến 1 triệu tất cả Khách Sạn","Đặt khách sạn từ 2.5 triệu","VNEPICTHANKYOU"),
        Voucher("Giảm đến 1 triệu tất cả Khách Sạn","Đặt khách sạn từ 2.5 triệu","VNEPICTHANKYOU"),
        Voucher("Giảm đến 1 triệu tất cả Khách Sạn","Đặt khách sạn từ 2.5 triệu","VNEPICTHANKYOU")
    )

    val hotels = arrayListOf(
        Hotel("22Land Residence Hotel 71 Hang Bong", "⭐⭐⭐⭐⭐","8.6/10","864.000 VND", R.drawable.khachsan),
        Hotel("22Land Residence Hotel 71 Hang Bong", "⭐⭐⭐⭐⭐","8.6/10","864.000 VND", R.drawable.khachsan),
        Hotel("22Land Residence Hotel 71 Hang Bong", "⭐⭐⭐⭐⭐","8.6/10","864.000 VND", R.drawable.khachsan),
        Hotel("22Land Residence Hotel 71 Hang Bong", "⭐⭐⭐⭐⭐","8.6/10","864.000 VND", R.drawable.khachsan),
        Hotel("22Land Residence Hotel 71 Hang Bong", "⭐⭐⭐⭐⭐","8.6/10","864.000 VND", R.drawable.khachsan),
        Hotel("22Land Residence Hotel 71 Hang Bong", "⭐⭐⭐⭐⭐","8.6/10","864.000 VND", R.drawable.khachsan),
        Hotel("22Land Residence Hotel 71 Hang Bong", "⭐⭐⭐⭐⭐","8.6/10","864.000 VND", R.drawable.khachsan),
        Hotel("22Land Residence Hotel 71 Hang Bong", "⭐⭐⭐⭐⭐","8.6/10","864.000 VND", R.drawable.khachsan)




    )
    override fun onCreate(savedInstanceState: Bundle?) {
        binding=HomePageLayoutBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)

        setContentView(binding.root)

        val recyclerView = findViewById<RecyclerView>(R.id.recycleListHotel)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        //hiển thị voucher list
        val recyclerViewVoucher = findViewById<RecyclerView>(R.id.recycleListVoucher)
        recyclerViewVoucher.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)


        //gọi adapter voucher
        adapterVoucher = MyVoucherRecyclerViewAdapter(this, vouchers)
        recyclerViewVoucher.adapter = adapterVoucher

        //gọi apdater hotel
        adapter = MyHotelRecyclerViewAdapter(this, hotels)
        recyclerView.adapter = adapter


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

        adapter.setOnItemClick(object : MyHotelRecyclerViewAdapter.OnRecyclerViewItemClickListener {
            override fun onImageClickListener(item: View?, position: Int) {
                Toast.makeText(this@MainActivity, "Ảnh: ${hotels[position].name}", Toast.LENGTH_SHORT).show()
            }

            override fun onMyItemClickListener(item: View?, position: Int) {
                Toast.makeText(this@MainActivity, "Item: ${hotels[position].name}", Toast.LENGTH_SHORT).show()
            }
        })

        adapterVoucher.setOnItemClick(object : MyVoucherRecyclerViewAdapter.OnRecyclerViewItemClickListener {
            override fun onImageClickListener(item: View?, position: Int) {
                Toast.makeText(this@MainActivity, "Ảnh: ${vouchers[position].title}", Toast.LENGTH_SHORT).show()
            }

            override fun onMyItemClickListener(item: View?, position: Int) {
                Toast.makeText(this@MainActivity, "Item: ${vouchers[position].title}", Toast.LENGTH_SHORT).show()
            }
        })

    }
    }
