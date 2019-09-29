package piedel.piotr.thesis.data.model.operation

data class OperationSelectable(
        val operation: Operation,
        var selected: Boolean) : OperationBase by operation