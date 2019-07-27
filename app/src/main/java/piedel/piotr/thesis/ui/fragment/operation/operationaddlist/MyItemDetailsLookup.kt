package piedel.piotr.thesis.ui.fragment.operation.operationaddlist

import android.view.MotionEvent
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.widget.RecyclerView
import timber.log.Timber

class MyItemDetailsLookup(private val recyclerView: RecyclerView) : ItemDetailsLookup<Long>() {

    override fun getItemDetails(event: MotionEvent): ItemDetails<Long>? {
        val viewUnderSelectedArea = recyclerView.findChildViewUnder(event.x, event.y)
        Timber.d("getItemDetails()")
        if (viewUnderSelectedArea != null) {
            val viewHolder = recyclerView.getChildViewHolder(viewUnderSelectedArea)
            if (viewHolder is OperationAddListAdapter.OperationViewHolder) {
                return viewHolder.getItemDetails()
            }
        }
        return null
    }
}