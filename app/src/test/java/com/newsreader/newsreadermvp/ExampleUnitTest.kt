package com.newsreader.newsreadermvp

import com.newsreader.newsreadermvp.repository.NewsModel
import com.newsreader.newsreadermvp.repository.UrlsItem
import org.junit.Assert.assertEquals
import org.junit.Test

class ExampleUnitTest {
    private val newsModel = NewsModel(this.javaClass.getResource("").path)

    @Test
    fun addition_isCorrect() {
        assertEquals(4, (2 + 2).toLong())
    }

    @Test
    fun equalSelectedUrlAfterAndBeforeSaveToFile() {
        val urlsItem = UrlsItem(
                0,
                "https://newsapi.org/v2/top-headlines?country=ru&category=business&apiKey=f97c16d8327346b2b935191967be9818",
                "json",
                "Бизнес")
        newsModel.writeSelectedUrlFromFile(urlsItem)

        val urlsItemFromFile = newsModel.readSelectedUrlFromFile()

        assertEquals(urlsItem.value, urlsItemFromFile.value)
    }
}