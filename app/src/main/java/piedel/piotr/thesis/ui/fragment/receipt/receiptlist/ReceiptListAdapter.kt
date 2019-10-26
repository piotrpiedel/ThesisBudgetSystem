package piedel.piotr.thesis.ui.fragment.receipt.receiptlist

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import piedel.piotr.thesis.R
import piedel.piotr.thesis.data.model.receipt.Receipt
import piedel.piotr.thesis.injection.scopes.ActivityContext
import piedel.piotr.thesis.util.dateToDayMonthYearFormatString
import piedel.piotr.thesis.util.glide.glideLoadAsSmallBitmap
import javax.inject.Inject


class ReceiptListAdapter @Inject constructor(@ActivityContext val context: Context) : RecyclerView.Adapter<ReceiptListAdapter.ReceiptViewHolder>() {

    private val listOfReceipt: MutableList<Receipt> = mutableListOf()

    private var adapterListener: ReceiptAdapterListener? = null

    fun setClickListener(operationAdapteListener: ReceiptAdapterListener) {
        adapterListener = operationAdapteListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReceiptViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.receipt_card_view, parent, false)
        return ReceiptViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listOfReceipt.size
    }

    override fun onBindViewHolder(holder: ReceiptViewHolder, position: Int) {
        val receiptItem = listOfReceipt[position]
        holder.receipt = receiptItem
        loadPictureFromGallery(context, receiptItem.receiptImageSourcePath, holder)
        holder.receiptTitle.text = receiptItem.title
        holder.receiptDate.text = dateToDayMonthYearFormatString(receiptItem.date)
        holder.receiptValueOf.text = receiptItem.value.toString()
    }

    @SuppressLint("CheckResult")
    private fun loadPictureFromGallery(context: Context, picturePath: String?, holder: ReceiptViewHolder) {
        if (!picturePath.isNullOrEmpty() && picturePath != "Empty Uri")
            glideLoadAsSmallBitmap(context, picturePath)
                    .listener(HolderGlideRequestListener(context,holder))
                    .submit()
    }

    fun updateListOfReceipt(receipts: MutableList<Receipt>) {
        listOfReceipt.clear()
        listOfReceipt.addAll(receipts)
        notifyDataSetChanged()
    }

    fun notifyAboutItemRemoved(itemPosition: Int) {
        listOfReceipt.removeAt(itemPosition)
        notifyItemRemoved(itemPosition)
        notifyItemRangeChanged(itemPosition, itemCount)
    }

    interface ReceiptAdapterListener {
        fun onClickListener(receiptItem: Receipt)
        fun onReceiptLongClick(receiptItem: Receipt, position: Int)
    }


    inner class ReceiptViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var receipt: Receipt? = null

        @BindView(R.id.receipt_thumbnail)
        lateinit var receiptThumbNail: ImageView

        @BindView(R.id.receipt_title)
        lateinit var receiptTitle: TextView

        @BindView(R.id.receipt_date)
        lateinit var receiptDate: TextView

        @BindView(R.id.receipt_value)
        lateinit var receiptValueOf: TextView

        init {
            ButterKnife.bind(this, itemView)
            itemView.setOnClickListener { adapterListener.let { adapterListener?.onClickListener(receipt as Receipt) } }
            itemView.setOnLongClickListener {
                adapterListener.let { adapterListener?.onReceiptLongClick(receipt as Receipt, this.layoutPosition) }
                true
            }
        }
    }
}