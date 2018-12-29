package piedel.piotr.thesis.ui.fragment.receipt.receiptlist

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import piedel.piotr.thesis.R
import piedel.piotr.thesis.data.model.receipt.Receipt
import piedel.piotr.thesis.injection.scopes.ActivityContext
import piedel.piotr.thesis.util.dateToTextString
import piedel.piotr.thesis.util.requestGlideBuildierOptionsAsBitmap
import timber.log.Timber
import javax.inject.Inject


class ReceiptAdapter @Inject constructor(@ActivityContext val context: Context) : RecyclerView.Adapter<ReceiptAdapter.ReceiptViewHolder>() {

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
        val receiptItem = listOfReceipt.get(position)
        loadPictureFromGallery(context, receiptItem.receiptImageSourcePath, holder)
        holder.receiptTitle.text = receiptItem.title
        holder.receiptDate.text = dateToTextString(receiptItem.date)
        holder.receiptValueOf.text = receiptItem.value.toString()
    }

    @SuppressLint("CheckResult")
    private fun loadPictureFromGallery(context: Context, picturePath: String?, holder: ReceiptViewHolder) {
        if (!picturePath.isNullOrEmpty() && !picturePath.equals("Empty Uri"))
            requestGlideBuildierOptionsAsBitmap(context, picturePath)
                    .listener(object : RequestListener<Bitmap> {
                        override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Bitmap>?, isFirstResource: Boolean): Boolean {
                            Timber.d("loadPictureFromGallery onLoadFailed")
                            return true
                        }

                        override fun onResourceReady(resource: Bitmap, model: Any?, target: Target<Bitmap>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                            Timber.d("loadPictureFromGallery onResourceReady")
                            holder.receiptThumbNail.setImageBitmap(resource)
                            return true
                        }
                    })
                    .submit()
    }

    fun updateListOfReceipt(receipts: MutableList<Receipt>) {
        listOfReceipt.clear()
        listOfReceipt.addAll(receipts)
        notifyDataSetChanged()
    }

    interface ReceiptAdapterListener {
    }


    inner class ReceiptViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

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
        }
    }
}