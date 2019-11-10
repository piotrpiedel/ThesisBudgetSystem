package piedel.piotr.thesis.util.gdrive

import piedel.piotr.thesis.data.model.operation.Operation

class ParsedOperationsHolder {

    var listOfParsedOperationsFromOCRString: MutableList<Operation> = mutableListOf()
        private set

    fun addResultToOperationList(operationList: List<Operation>) {
        if (!operationList.isNullOrEmpty()) {
            listOfParsedOperationsFromOCRString.addAll(operationList)
        } else return
    }
}