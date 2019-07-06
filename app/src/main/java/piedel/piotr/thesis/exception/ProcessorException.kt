package piedel.piotr.thesis.exception

class ProcessorException(message: String) : Exception(message) {
    constructor() : this(DEFAULT_MESSAGE)

    companion object {

        private const val DEFAULT_MESSAGE = "Unknown exception happens to processor"

        const val NOT_INITIALIZED = "The processor is not properly initialized"
        const val UNKNOWN_PROCESSOR_TYPE = "Cannot resolve processor type"
        const val RUNTIME_EXCEPTION = "Processor exception in runtime"
    }
}