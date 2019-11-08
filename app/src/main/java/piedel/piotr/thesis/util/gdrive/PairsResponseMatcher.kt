package piedel.piotr.thesis.util.gdrive

import piedel.piotr.thesis.util.toPair

class PairsResponseMatcher {

    private fun matchStringFromListToTitleValuePair(dividedString: List<String>): List<Pair<String, String>> {
        val pairTitleOperationValueOperation: List<Pair<String, String>>?
        if (!dividedString.isNullOrEmpty()) {
            pairTitleOperationValueOperation = dividedString.toPair()
        } else return emptyList()
        return pairTitleOperationValueOperation
    }

}