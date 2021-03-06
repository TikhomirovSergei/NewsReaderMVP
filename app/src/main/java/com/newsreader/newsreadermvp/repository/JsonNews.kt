package com.newsreader.newsreadermvp.repository

//структура json новостного потока
class JsonNews(
        val articles: ArrayList<JsonNewsItem>
)

/*
class RssNewsItem(
        val description: String,
        val image: String,
        val link: String,
        val publishDate: String,
        val title: String
)*/

class JsonNewsItem(
        val title: String,
        val description: String,
        val url: String,
        val urlToImage: String,
        val publishedAt: String
)

//список корректных новостных потоков введенных пользователем
class Urls(
        val urls: ArrayList<UrlsItem>
)

//новостной поток выбранный для отображения
class ISelectedUrl(
        val url: ArrayList<UrlsItem>
)

class UrlsItem(
        var id: Int,
        var value: String,
        var type: String,
        var title: String
)

//структура прочитанной новости
class ISelectedUrlsForNews(
        var selected: ArrayList<SelectedUrlsForNews>
)

class SelectedUrlsForNews(
        val url: SelectedNewsItems
)

class SelectedNewsItems(
        var id: Int,
        var value: String,
        var type: String,
        var title: String,
        val items: ArrayList<JsonNewsItem>
)