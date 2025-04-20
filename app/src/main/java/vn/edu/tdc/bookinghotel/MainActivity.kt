package vn.edu.tdc.bookinghotel

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import vn.edu.tdc.bookinghotel.Adapters.MyHotelRecyclerViewAdapter
import vn.edu.tdc.bookinghotel.Model.Hotel
import vn.edu.tdc.bookinghotel.databinding.HomePageLayoutBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding:HomePageLayoutBinding
    private lateinit var listHotel: ArrayList<Hotel>
    private lateinit var adapter: MyHotelRecyclerViewAdapter

    private var itemSelected = -1
    private var oldColor: Int = 0
    private lateinit var oldView: View


    val hotels = arrayListOf(
        Hotel("Khách sạn A", "Còn phòng", "Gần trung tâm, tiện nghi đầy đủ", R.drawable.baseline_co_present_24),
        Hotel("Khách sạn B", "Hết phòng", "View biển, có hồ bơi", R.drawable.baseline_co_present_24),
        Hotel("Khách sạn C", "Còn phòng", "Phòng đôi, wifi miễn phí", R.drawable.baseline_co_present_24)
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        binding=HomePageLayoutBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)

        setContentView(binding.root)

        val recyclerView = findViewById<RecyclerView>(R.id.recycleListHotel)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val adapter = MyHotelRecyclerViewAdapter(this, hotels)
        recyclerView.adapter = adapter

        adapter.setOnItemClick(object : MyHotelRecyclerViewAdapter.OnRecyclerViewItemClickListener {
            override fun onImageClickListener(item: View?, position: Int) {
                Toast.makeText(this@MainActivity, "Ảnh: ${hotels[position].name}", Toast.LENGTH_SHORT).show()
            }

            override fun onMyItemClickListener(item: View?, position: Int) {
                Toast.makeText(this@MainActivity, "Item: ${hotels[position].name}", Toast.LENGTH_SHORT).show()
            }
        })

    }
    }
