package io.koosha.foobar.common.model

sealed class ApiError(
    val message: String
) {

    abstract val error: String

    abstract override fun equals(other: Any?): Boolean

    abstract override fun hashCode(): Int

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

        val that = other as? EntityNotFoundApiError

        return that != null &&
                this.message == that.message &&
                this.context == that.context
    }

    override fun hashCode(): Int {
        var result = super.message.hashCode()
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

    override fun equals(other: Any?): Boolean {
        if (this === other)
            return true

        val that = other as? EntityBadValueApiError

        return that != null &&
                this.message == that.message &&
                this.context == that.context
    }

    override fun hashCode(): Int {
        var result = super.message.hashCode()
        result = 31 * result + context.hashCode()
        return result
    }

}

class EntityIllegalStateApiError(
    message: String,
    val context: Set<EntityInfo>,
) : ApiError(message) {

    companion object {
        const val ERROR = "bad value for entity"
    }

    override val error = ERROR

    override fun equals(other: Any?): Boolean {
        if (this === other)
            return true

        val that = other as? EntityIllegalStateApiError

        return that != null &&
                this.message == that.message &&
                this.context == that.context
    }

    override fun hashCode(): Int {
        var result = super.message.hashCode()
        result = 31 * result + context.hashCode()
        return result
    }

}

// TODO remove extra info from here!
class ServerError(
    message: String,
    context: Throwable?,
) : ApiError(message + " / " + context?.message + " / " + context?.stackTraceToString()) {

    companion object {
        const val ERROR = "Internal server error"
    }

    override val error = ERROR

    override fun equals(other: Any?): Boolean {
        if (this === other)
            return true

        val that = other as? EntityIllegalStateApiError

        return that != null &&
                this.message == that.message
    }

    override fun hashCode(): Int {
        return super.message.hashCode()
    }

}
