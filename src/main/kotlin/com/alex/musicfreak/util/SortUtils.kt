package com.alex.musicfreak.util

import io.quarkus.panache.common.Sort

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
