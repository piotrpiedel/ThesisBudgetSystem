package piedel.piotr.thesis.data.model.operation

import java.util.*

interface OperationBase {
    var value: Double
    var title: String?
    var operationType: OperationType
    var date: Date?
    var other_category_id: Int?
}