package piedel.piotr.thesis.util.gdrive

import piedel.piotr.thesis.data.model.operation.Operation
import piedel.piotr.thesis.data.model.operation.OperationType
import piedel.piotr.thesis.util.parseStringWithCommaSeparatorToDouble
import java.util.*

class PairOfStringsToOperationConverter {

    fun matchPairsWithTitleValueStringToListOfOperation(pairTitleOperationValueOperation:
                                                        List<Pair<String, String>>, dateOnReceipt: Date?): List<Operation> {
        val operationList: MutableList<Operation> = mutableListOf()
        for (pair in pairTitleOperationValueOperation) {
            operationList.add(createOperationFromPair(pair, dateOnReceipt))
        }
        return operationList
    }

    private fun createOperationFromPair(pair: Pair<String, String>, dateOnReceipt: Date?) =
            Operation(pair.second.parseStringWithCommaSeparatorToDouble(),
                    pair.first,
                    OperationType.OUTCOME,
                    dateOnReceipt)
}