package piedel.piotr.thesis.ui.fragment.operation.operationaddlist

import androidx.recyclerview.selection.ItemDetailsLookup
import piedel.piotr.thesis.data.model.operation.Operation

class OperationItemDetails(private val adapterPosition: Int, private val selectionKey: Operation)
    : ItemDetailsLookup.ItemDetails<Operation>() {
    override fun getSelectionKey(): Operation? = selectionKey
    override fun getPosition(): Int = adapterPosition
}