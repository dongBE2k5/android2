import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import vn.edu.tdc.bookinghotel.Model.Hotel
import vn.edu.tdc.bookinghotel.Model.Voucher
import vn.edu.tdc.bookinghotel.R
import android.content.ClipData
import android.content.ClipboardManager
import android.widget.Toast
import vn.edu.tdc.bookinghotel.databinding.CardRecyclerListVoucherBinding

class MyVoucherRecyclerViewAdapter(
    private val context: Context,
    private val list: ArrayList<Voucher>
) : RecyclerView.Adapter<MyVoucherRecyclerViewAdapter.MyViewHolderVoucher>()  {


    private var selectedPosition = -1
    private var onItemClick: OnRecyclerViewItemClickListener? = null

    fun setOnItemClick(listener: OnRecyclerViewItemClickListener) {
        onItemClick = listener
    }

    interface OnRecyclerViewItemClickListener {
        fun onImageClickListener(item: View?, position: Int)
        fun onMyItemClickListener(item: View?, position: Int)
    }

    inner class MyViewHolderVoucher(
        val binding: CardRecyclerListVoucherBinding,
        var itemPosition: Int = 0,

    ) : RecyclerView.ViewHolder(binding.root) {
        val btnCopy = binding.btnCopy
        init {
            binding.root.setOnClickListener {
                if (adapterPosition == RecyclerView.NO_POSITION) return@setOnClickListener
                itemPosition = adapterPosition

                if (selectedPosition == itemPosition) {
                    selectedPosition = -1
                } else {
                    selectedPosition = itemPosition
                }
                notifyDataSetChanged()
                onItemClick?.onMyItemClickListener(binding.root, itemPosition)
            }
        }
    }


    override fun getItemViewType(position: Int): Int {
        return if (position == selectedPosition) 1 else 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolderVoucher {
        val binding = CardRecyclerListVoucherBinding.inflate(LayoutInflater.from(context), parent, false)
        return MyViewHolderVoucher(binding)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: MyViewHolderVoucher, position: Int) {
        val voucher = list[position]
        holder.itemPosition = position

        val binding = holder.binding
        binding.tvTitleVoucher.text = voucher.code
        binding.tvDetailVoucher.text = voucher.description
        binding.tvCodeVoucher.text = voucher.code
        binding.quantityVoucher.text = "x${voucher.quantity}"

        // Xử lý nút Copy
        holder.btnCopy.setOnClickListener {
            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("Voucher Code", voucher.code)
            clipboard.setPrimaryClip(clip)
            Toast.makeText(context, "Copied voucher code: ${voucher.code}", Toast.LENGTH_SHORT).show()
        }
    }


}