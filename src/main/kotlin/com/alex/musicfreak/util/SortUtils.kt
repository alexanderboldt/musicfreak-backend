package com.alex.musicfreak.util

import io.quarkus.panache.common.Sort

/**
 * Converts a [String] to a [Sort] object.
 *
 * Values are separated with a comma and can have a `-` for a descending order,
 * otherwise the ascending order will be used as default.
 *
 * @receiver the [String] that contains the values separate with commas.
 * @return the [Sort] object or `Sort.empty()` if the receiver is `null` or blank.
 *
 * Example:
 * ```
 * "id,title".convertToSort()
 * "id,-title,description".convertToSort()
 * ```
 */
fun String?.convertToSort(): Sort {
    if (this.isNullOrBlank()) return Sort.empty()

    return this.split(",")
        .map { split ->
            when (split.startsWith("-")) {
                true -> Sort.by(split.drop(1), Sort.Direction.Descending)
                false -> Sort.by(split, Sort.Direction.Ascending)
            }
        }.reduce { acc, current -> acc.and(current.columns.first().name, current.columns.first().direction) }
}
