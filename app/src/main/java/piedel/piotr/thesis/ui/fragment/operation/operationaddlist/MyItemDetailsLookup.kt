package piedel.piotr.thesis.ui.fragment.operation.operationaddlist

import android.view.MotionEvent
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.widget.RecyclerView
import timber.log.Timber

class MyItemDetailsLookup(private val recyclerView: RecyclerView) :
        ItemDetailsLookup<Long>() {
    override fun getItemDetails(event: MotionEvent): ItemDetails<Long>? {
        val view = recyclerView.findChildViewUnder(event.x, event.y)
        Timber.d("getItemDetails()")
        if (view != null) {
            return (recyclerView.getChildViewHolder(view) as OperationAddListAdapter.OperationViewHolder).getItemDetails()
        }
        return null
    }


}