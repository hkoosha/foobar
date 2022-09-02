package io.koosha.foobar.common.model


sealed class ApiError(val message: String) {

    abstract val error: String

    override fun equals(other: Any?): Boolean {
        if (this === other)
            return true
        if (javaClass != other?.javaClass)
            return false

        other as ApiError

        if (message != other.message)
            return false

        return true
    }

    override fun hashCode(): Int {
        return message.hashCode()
    }

}

class EntityNotFoundApiError(
    message: String,
    val context: Set<EntityInfo>,
) : ApiError(message) {

    companion object {
        const val ERROR = "entity not found"
    }

    override val error = ERROR

    override fun equals(other: Any?): Boolean {
        if (this === other)
            return true
        if (javaClass != other?.javaClass)
            return false
        if (!super.equals(other))
            return false

        other as EntityNotFoundApiError

        if (context != other.context)
            return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + context.hashCode()
        return result
    }
}

class EntityBadValueApiError(
    message: String,
    val context: Map<String, List<String>>,
) : ApiError(message) {

    companion object {
        const val ERROR = "bad value for entity"
    }

    override val error = ERROR

}

class EntityIllegalStateApiError(
    message: String,
    val context: Set<EntityInfo>,
) : ApiError(message) {

    companion object {
        const val ERROR = "bad value for entity"
    }

    override val error = ERROR

}

// TODO remove extra info from here!
class ServerError(
    message: String,
    val context: Throwable?,
) : ApiError(message + " / " + context?.message + " / " + context?.stackTraceToString()) {

    companion object {
        const val ERROR = "Internal server error"
    }

    override val error = ERROR

}
