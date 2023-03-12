package io.koosha.foobar.common.error

import io.koosha.foobar.common.model.EntityInfo
import jakarta.validation.ConstraintViolation


abstract class EntityException : RuntimeException {

    val context: Set<EntityInfo>

    constructor(entityType: String, entityId: Any?) : super("$entityType => $entityId") {
        this.context = setOf(
            EntityInfo(
                entityType = entityType,
                entityId = entityId,
            )
        )
    }

    constructor(entityType: String, entityId: Any?, msg: String) : super(msg) {
        this.context = setOf(
            EntityInfo(
                entityType = entityType,
                entityId = entityId,
            )
        )
    }

    constructor(entityType: String, entityId: Any?, throwable: Throwable) : super(
        "$entityType => $entityId",
        throwable
    ) {
        this.context = setOf(
            EntityInfo(
                entityType = entityType,
                entityId = entityId,
            )
        )
    }

    constructor(entityType: String, entityId: Any?, msg: String, throwable: Throwable) : super(msg, throwable) {
        this.context = setOf(
            EntityInfo(
                entityType = entityType,
                entityId = entityId,
            )
        )
    }

    constructor(context: Set<EntityInfo>) : super(context.joinToString { "${it.entityType} => ${it.entityId}" }) {
        this.context = LinkedHashSet(context)
    }

    constructor(context: Set<EntityInfo>, msg: String) : super(msg) {
        this.context = LinkedHashSet(context)
    }

    constructor(
        context: Set<EntityInfo>,
        throwable: Throwable,
    ) : super(context.joinToString { "${it.entityType} => ${it.entityId}" }, throwable) {
        this.context = LinkedHashSet(context)
    }

    constructor(context: Set<EntityInfo>, msg: String, throwable: Throwable) : super(msg, throwable) {
        this.context = LinkedHashSet(context)
    }

}

open class EntityBadValueException : EntityException {

    val errors: Set<ConstraintViolation<*>>?

    constructor(
        context: Set<EntityInfo>,
    ) : super(context) {
        this.errors = null
    }

    constructor(
        context: Set<EntityInfo>,
        msg: String,
    ) : super(context, msg) {
        this.errors = null
    }

    constructor(
        context: Set<EntityInfo>,
        throwable: Throwable,
    ) : super(context, throwable) {
        this.errors = null
    }

    constructor(
        context: Set<EntityInfo>,
        msg: String,
        throwable: Throwable,
    ) : super(context, msg, throwable) {
        this.errors = null
    }

    constructor(
        context: Set<EntityInfo>,
        errors: Set<ConstraintViolation<*>>?,
    ) : super(context) {
        this.errors = if (errors == null)
            null
        else
            LinkedHashSet(errors)
    }

    constructor(
        context: Set<EntityInfo>,
        msg: String,
        errors: Set<ConstraintViolation<*>>?,
    ) : super(context, msg) {
        this.errors = if (errors == null)
            null
        else
            LinkedHashSet(errors)
    }

    constructor(
        context: Set<EntityInfo>,
        throwable: Throwable,
        errors: Set<ConstraintViolation<*>>?,
    ) : super(context, throwable) {
        this.errors = if (errors == null)
            null
        else
            LinkedHashSet(errors)
    }

    constructor(
        context: Set<EntityInfo>,
        msg: String,
        throwable: Throwable,
        errors: Set<ConstraintViolation<*>>?,
    ) : super(context, msg, throwable) {
        this.errors = if (errors == null)
            null
        else
            LinkedHashSet(errors)
    }

    constructor(
        entityType: String,
        entityId: Any?,
    ) : super(entityType, entityId) {
        this.errors = null
    }

    constructor(
        entityType: String,
        entityId: Any?,
        msg: String,
    ) : super(entityType, entityId, msg) {
        this.errors = null
    }

    constructor(
        entityType: String,
        entityId: Any?,
        throwable: Throwable,
    ) : super(entityType, entityId, throwable) {
        this.errors = null
    }

    constructor(
        entityType: String,
        entityId: Any?,
        msg: String,
        throwable: Throwable,
    ) : super(entityType, entityId, msg, throwable) {
        this.errors = null
    }

    constructor(
        entityType: String,
        entityId: Any?,
        errors: Set<ConstraintViolation<*>>?,
    ) : super(entityType, entityId) {
        this.errors = if (errors == null)
            null
        else
            LinkedHashSet(errors)
    }

    constructor(
        entityType: String,
        entityId: Any?,
        msg: String,
        errors: Set<ConstraintViolation<*>>?,
    ) : super(entityType, entityId, msg) {
        this.errors = if (errors == null)
            null
        else
            LinkedHashSet(errors)
    }

    constructor(
        entityType: String,
        entityId: Any?,
        throwable: Throwable,
        errors: Set<ConstraintViolation<*>>?,
    ) : super(entityType, entityId, throwable) {
        this.errors = if (errors == null)
            null
        else
            LinkedHashSet(errors)
    }

    constructor(
        entityType: String,
        entityId: Any?,
        msg: String,
        throwable: Throwable,
        errors: Set<ConstraintViolation<*>>?,
    ) : super(entityType, entityId, msg, throwable) {
        this.errors = if (errors == null)
            null
        else
            LinkedHashSet(errors)
    }
}

open class EntityInIllegalStateException : EntityException {

    constructor(
        context: Set<EntityInfo>,
    ) : super(context)

    constructor(
        context: Set<EntityInfo>,
        msg: String,
    ) : super(context, msg)

    constructor(
        context: Set<EntityInfo>,
        throwable: Throwable,
    ) : super(context, throwable)

    constructor(
        context: Set<EntityInfo>,
        msg: String,
        throwable: Throwable,
    ) : super(context, msg, throwable)

    constructor(
        entityType: String,
        entityId: Any?,
    ) : super(entityType, entityId)

    constructor(
        entityType: String,
        entityId: Any?,
        msg: String,
    ) : super(entityType, entityId, msg)

    constructor(
        entityType: String,
        entityId: Any?,
        throwable: Throwable,
    ) : super(entityType, entityId, throwable)

    constructor(
        entityType: String,
        entityId: Any?,
        msg: String,
        throwable: Throwable,
    ) : super(entityType, entityId, msg, throwable)
}

open class EntityNotFoundException : EntityException {

    constructor(
        context: Set<EntityInfo>,
    ) : super(context)

    constructor(
        context: Set<EntityInfo>,
        msg: String,
    ) : super(context, msg)

    constructor(
        context: Set<EntityInfo>,
        throwable: Throwable,
    ) : super(context, throwable)

    constructor(
        context: Set<EntityInfo>,
        msg: String,
        throwable: Throwable,
    ) : super(context, msg, throwable)

    constructor(
        entityType: String,
        entityId: Any?,
    ) : super(entityType, entityId)

    constructor(
        entityType: String,
        entityId: Any?,
        msg: String,
    ) : super(entityType, entityId, msg)

    constructor(
        entityType: String,
        entityId: Any?,
        throwable: Throwable,
    ) : super(entityType, entityId, throwable)

    constructor(
        entityType: String,
        entityId: Any?,
        msg: String,
        throwable: Throwable,
    ) : super(entityType, entityId, msg, throwable)
}
