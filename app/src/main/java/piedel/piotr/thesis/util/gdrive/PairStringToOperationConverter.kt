package piedel.piotr.thesis.util.gdrive

import piedel.piotr.thesis.data.model.operation.Operation
import piedel.piotr.thesis.data.model.operation.OperationType
import piedel.piotr.thesis.util.parseStringWithCommaSeparatorToDouble

class PairStringToOperationConverter {


    private fun createOperationsFromListOfPairTitleValue(pairTitleOperationValueOperation: List<Pair<String, String>>): List<Operation> {
        val operationList: MutableList<Operation> = mutableListOf()
        for (pair in pairTitleOperationValueOperation) {
            operationList.add(createOperationFromPair(pair))
        }
        return operationList
    }

    private fun createOperationFromPair(pair: Pair<String, String>) =
            Operation(pair.second.parseStringWithCommaSeparatorToDouble(), pair.first, OperationType.OUTCOME, dateOnReceipt)
}