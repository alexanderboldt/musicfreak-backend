package com.alex.musicfreak.util

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.quarkus.panache.common.Sort

class SortUtilsTest : StringSpec({

    "should convert null" {
        val sort = null.convertToSort()

        sort.columns shouldHaveSize 0
    }

    "should convert empty" {
        val sort = "".convertToSort()

        sort.columns shouldHaveSize 0
    }

    "should convert id asc" {
        val sort = "id".convertToSort()

        sort.columns shouldHaveSize 1

        sort.columns[0].name shouldBe "id"
        sort.columns[0].direction shouldBe Sort.Direction.Ascending
    }

    "should convert id desc" {
        val sort = "-id".convertToSort()

        sort.columns shouldHaveSize 1

        sort.columns[0].name shouldBe "id"
        sort.columns[0].direction shouldBe Sort.Direction.Descending
    }

    "should convert id asc and title asc" {
        val sort = "id,title".convertToSort()

        sort.columns shouldHaveSize 2

        sort.columns[0].name shouldBe "id"
        sort.columns[0].direction shouldBe Sort.Direction.Ascending

        sort.columns[1].name shouldBe "title"
        sort.columns[1].direction shouldBe Sort.Direction.Ascending
    }

    "should convert id asc and title desc" {
        val sort = "id,-title".convertToSort()

        sort.columns shouldHaveSize 2

        sort.columns[0].name shouldBe "id"
        sort.columns[0].direction shouldBe Sort.Direction.Ascending

        sort.columns[1].name shouldBe "title"
        sort.columns[1].direction shouldBe Sort.Direction.Descending
    }

    "should convert id desc and title asc" {
        val sort = "-id,title".convertToSort()

        sort.columns shouldHaveSize 2

        sort.columns[0].name shouldBe "id"
        sort.columns[0].direction shouldBe Sort.Direction.Descending

        sort.columns[1].name shouldBe "title"
        sort.columns[1].direction shouldBe Sort.Direction.Ascending
    }

    "should convert id desc and title desc" {
        val sort = "-id,-title".convertToSort()

        sort.columns shouldHaveSize 2

        sort.columns[0].name shouldBe "id"
        sort.columns[0].direction shouldBe Sort.Direction.Descending

        sort.columns[1].name shouldBe "title"
        sort.columns[1].direction shouldBe Sort.Direction.Descending
    }
})
