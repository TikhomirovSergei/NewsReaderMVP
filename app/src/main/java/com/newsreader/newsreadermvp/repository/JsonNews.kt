package com.newsreader.newsreadermvp.repository

class JsonNews(
        val articles: ArrayList<JsonNewsItem>
)

class JsonNewsItem(
        val author: String,
        val title: String,
        val description: String,
        val url: String,
        val urlToImage: String,
        val publishedAt: String,
        val content: String
)

class Urls(
        val urls: ArrayList<UrlsItem>
)

class UrlsItem(
        val id: Int,
        var value: String,
        var type: String
)