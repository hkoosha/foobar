package io.koosha.foobar.maker.api

import io.koosha.foobar.maker.api.model.EntityId
import io.koosha.foobar.maker.api.svc.EntityIdService
import io.koosha.foobar.maker.api.svc.Rand
import org.springframework.boot.ApplicationArguments

fun ApplicationArguments.firstOrRandomUnPrefixed(name: String, len: Int = 9): String =
    this.getOptionValues(name)?.firstOrNull() ?: Rand().string(len)

fun ApplicationArguments.firstOrRandom(name: String, len: Int = 9): String =
    this.getOptionValues(name)?.firstOrNull() ?: Rand().string(len, "${name}_")

fun ApplicationArguments.firstOrNull(name: String): String? = this.getOptionValues(name)?.firstOrNull()

fun ApplicationArguments.firstOrDef(name: String, def: String): String =
    this.getOptionValues(name)?.firstOrNull() ?: def

fun ApplicationArguments.first(name: String): String =
    this.getOptionValues(name)?.firstOrNull() ?: throw CliException("option not give: $name")

fun matches(
    s0: String?,
    s1: String?,
): Boolean =
    when {
        s0 == null || s1 == null -> false
        s0.length == s1.length -> s0 == s1
        else -> {
            val longStr = if (s0.length > s1.length) s0 else s1
            val shortStr = if (s0.length > s1.length) s1 else s0
            longStr.substring(0, shortStr.length) == shortStr
        }
    }

fun <T> stringAll(
    repo: EntityIdService,
    entityType: String,
    all: Collection<T>,
    idExtractor: (T) -> String,
): String {

    val sorted = all.sortedBy {
        repo.findInternalIdByEntityTypeAndEntityId(entityType, idExtractor(it))
            .map { it1 -> it1.internalId }
            .orElse(-1)
    }
    val sb = StringBuilder()
    for (entity in sorted) {
        val id: EntityId? =
            repo.findInternalIdByEntityTypeAndEntityId(entityType, idExtractor(entity))
                .orElse(null)
        sb
            .append("\n========================================\n")
            .append(id?.toString() ?: "?")
            .append("\n")
            .append(entity.toString())
    }
    return sb.toString()
}
