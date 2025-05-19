package vn.edu.tdc.bookinghotel.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import vn.edu.tdc.bookinghotel.R

import vn.edu.tdc.bookinghotel.Model.ListDetail
class ListDetailRecyclerViewAdapter(
    private val listDetail: List<ListDetail>,
    private val listener: ChiTietPhongRecyclerViewAdapter.OnItemClickListener // Thêm listener
) : RecyclerView.Adapter<ListDetailRecyclerViewAdapter.ListDetailViewHolder>() {

    inner class ListDetailViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvPhonCoSan: TextView = itemView.findViewById(R.id.tvPhongCoSan)
        val recyclerViewChiTietPhong: RecyclerView = itemView.findViewById(R.id.recyclerViewChiTietPhong)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListDetailViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_detail_hotel, parent, false)
        return ListDetailViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListDetailViewHolder, position: Int) {
        val listDetails = listDetail[position]
        holder.tvPhonCoSan.text = listDetails.tvPhongCoSan
        val context = holder.itemView.context
        // Khởi tạo adapter con và truyền listener vào
        val adapterChiTietPhong = ChiTietPhongRecyclerViewAdapter(context,listDetails.danhSachPhong, listener)
        holder.recyclerViewChiTietPhong.layoutManager = LinearLayoutManager(holder.itemView.context)
        holder.recyclerViewChiTietPhong.adapter = adapterChiTietPhong
    }

    override fun getItemCount(): Int = listDetail.size
}


