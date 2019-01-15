package com.newsreader.newsreadermvp.repository

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.newsreader.newsreadermvp.createRequest
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import me.toptas.rssconverter.RssConverterFactory
import me.toptas.rssconverter.RssFeed
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.io.BufferedReader
import java.io.File

//https://habr.com/rss/hubs/all/
//TODO поработать над отображением rss description
class NewsModel(path: String) {
    private var urlsBD: String = "$path/Urls.json"
    private var selectedUrl: String = "$path/SelectedUrl.json"
    private var readedNews: String = "$path/ReadedNews.json"

    interface OnFinishedListener {
        fun onSetUrlSuccess()
        fun onResultFail(str: String)
        fun onGetNewsDataSuccess(articles: ArrayList<JsonNewsItem>)
    }

    /**
     * проверка url на корректность, его добавление в БД
     */
    fun requestValidateUrl(title: String, url: String, onFinishedListener: OnFinishedListener) {
        if ((title != "") && (url != "")) {
            val observable = createRequest(url)
                    .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())

            observable.subscribe({
                var isJson = false
                try {
                    //некая проверка, что входной поток это json
                    Gson().fromJson(it.toString(), JsonNews::class.java)
                    isJson = true
                } catch (ex: java.lang.Exception) {
                    val retrofit = Retrofit.Builder()
                            .baseUrl(url)
                            .addConverterFactory(RssConverterFactory.create())
                            .build()

                    val service = retrofit.create(RssService::class.java)
                    service.getRss(url)
                            .enqueue(object : Callback<RssFeed> {
                                override fun onResponse(call: Call<RssFeed>, response: Response<RssFeed>) {
                                    addToListNewCorrectUrl(title, url, "rss")
                                    onFinishedListener.onSetUrlSuccess()
                                }

                                override fun onFailure(call: Call<RssFeed>, t: Throwable) {
                                    onFinishedListener.onResultFail(t.toString())
                                }
                            })
                }

                if (isJson) {
                    addToListNewCorrectUrl(title, url, "json")
                    onFinishedListener.onSetUrlSuccess()
                }
            }, {
                onFinishedListener.onResultFail(it.toString())
            })
        } else {
            onFinishedListener.onResultFail("url и title обязательны для заполнения.")
        }
    }

    /**
     * получение данных по корректному url
     */
    fun getNewsData(url: String, type: String, onFinishedListener: OnFinishedListener) {
        try {
            if (type == "json") {
                val observable = createRequest(url)
                        .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())

                observable.subscribe({
                    val articles = Gson().fromJson(it.toString(), JsonNews::class.java)

                    onFinishedListener.onGetNewsDataSuccess(articles.articles)
                }, {
                    onFinishedListener.onResultFail(it.toString())
                })
            } else if (type == "rss") {
                val retrofit = Retrofit.Builder()
                        .baseUrl(url)
                        .addConverterFactory(RssConverterFactory.create())
                        .build()

                val service = retrofit.create(RssService::class.java)
                service.getRss(url)
                        .enqueue(object : Callback<RssFeed> {
                            override fun onResponse(call: Call<RssFeed>, response: Response<RssFeed>) {
                                var newsList = ArrayList<JsonNewsItem>()
                                for (item in response.body()!!.items!!) {
                                    var newsItem = JsonNewsItem(
                                            item.title ?: "",
                                            item.description?: "",
                                            item.link?: "",
                                            item.image?: "",
                                            item.publishDate?: "")
                                    newsList.add(newsItem)
                                }
                                onFinishedListener.onGetNewsDataSuccess(newsList)
                            }

                            override fun onFailure(call: Call<RssFeed>, t: Throwable) {
                                onFinishedListener.onResultFail(t.toString())
                            }
                        })
            } else
                throw java.lang.Exception("Поддерживается только json поток.")
        } catch (ex: java.lang.Exception) {
            onFinishedListener.onResultFail(ex.toString())
        }
    }

    /**
     * загрузка списка корректных url адресов
     */
    fun getUrlList(): ArrayList<UrlsItem>? {
        try {
            return readUrlsFile()
        } catch (ex: Exception) {
            throw ex
        }
    }

    /**
     * загрузка из файла выбранного для отображения url
     */
    fun readSelectedUrlFromFile(): UrlsItem {
        try {
            File(selectedUrl).createNewFile()
            val bufferedReader: BufferedReader = File(selectedUrl).bufferedReader()
            var inputString = bufferedReader.use { it.readText() }

            val urls = Gson().fromJson(inputString, ISelectedUrl::class.java)

            return urls.url[0]
        } catch (ex: Exception) {
            throw ex
        }
    }

    /**
     * запись в файл выбранного для отображения url
     */
    fun writeSelectedUrlFromFile(selectedItem: UrlsItem) {
        try {
            val newsItem: ArrayList<UrlsItem> = ArrayList<UrlsItem>()
            newsItem.add(selectedItem)
            val iSelectedUrl = ISelectedUrl(newsItem)

            val gson = GsonBuilder().setPrettyPrinting().create()
            File(selectedUrl).writeText(gson.toJson(iSelectedUrl))
        } catch (ex: Exception) {
            throw ex
        }
    }

    /**
     * загрузка списка прочитанных новостей
     */
    fun readReadedNewsFromFile(): ISelectedUrlsForNews? {
        try {
            File(readedNews).createNewFile()
            val bufferedReader: BufferedReader = File(readedNews).bufferedReader()
            var inputString = bufferedReader.use { it.readText() }

            return Gson().fromJson(inputString, ISelectedUrlsForNews::class.java)
        } catch (ex: Exception) {
            throw ex
        }
    }

    /**
     * запись в файл списка прочитанных новостей
     */
    fun writeReadedNewsFromFile(selectedItem: UrlsItem, newsItem: JsonNewsItem) {
        try {
            var selectedNews = readReadedNewsFromFile()

            var urlExist = false
            if (selectedNews != null) {
                for (i in 0 until selectedNews.selected.size) {
                    if (selectedNews.selected[i].url.value == selectedItem.value) {
                        urlExist = true
                        val structViewedNewsSelectedUrl = selectedNews.selected[i]
                        var newsWasReaded = false
                        for (k in 0 until structViewedNewsSelectedUrl.url.items.size) {
                            if (structViewedNewsSelectedUrl.url.items[k] == newsItem) {
                                newsWasReaded = true
                            }
                        }
                        if (!newsWasReaded) {
                            structViewedNewsSelectedUrl.url.items.add(structViewedNewsSelectedUrl.url.items.size, newsItem)
                        }
                    }
                }
                if (!urlExist) {
                    val jsonNewsItem: ArrayList<JsonNewsItem> = ArrayList<JsonNewsItem>()
                    jsonNewsItem.add(0, newsItem)

                    selectedNews.selected.add(selectedNews.selected.size, SelectedUrlsForNews(
                            SelectedNewsItems(
                                    selectedItem.id,
                                    selectedItem.value,
                                    selectedItem.type,
                                    selectedItem.title,
                                    jsonNewsItem)
                    ))
                }
            } else {
                val jsonNewsItem: ArrayList<JsonNewsItem> = ArrayList<JsonNewsItem>()
                jsonNewsItem.add(0, newsItem)

                val selected: ArrayList<SelectedUrlsForNews> = ArrayList<SelectedUrlsForNews>()
                selected.add(0, SelectedUrlsForNews(
                        SelectedNewsItems(
                                selectedItem.id,
                                selectedItem.value,
                                selectedItem.type,
                                selectedItem.title,
                                jsonNewsItem)
                ))

                selectedNews = ISelectedUrlsForNews(selected)
            }

            val gson = GsonBuilder().setPrettyPrinting().create()
            File(readedNews).writeText(gson.toJson(selectedNews))
        } catch (ex: Exception) {
            throw ex
        }
    }

    /**
     * чтение всех новостных потоков введенных пользователем
     */
    private fun readUrlsFile(): ArrayList<UrlsItem>? {
        try {
            File(urlsBD).createNewFile()
            val bufferedReader: BufferedReader = File(urlsBD).bufferedReader()
            val inputString = bufferedReader.use { it.readText() }

            return Gson().fromJson(inputString, Urls::class.java)?.urls
        } catch (ex: Exception) {
            throw ex
        }
    }

    private fun addToListNewCorrectUrl(title: String, url: String, type: String) {
        try {
            var urlsItems = readUrlsFile()

            if (urlsItems != null) {
                for (i in 0 until urlsItems.size) {
                    if ((urlsItems[i].value == url) || (urlsItems[i].title == title)) {
                        throw java.lang.Exception("Такой url или title уже используются.")
                    }
                }
            } else {
                urlsItems = ArrayList<UrlsItem>()
            }
            urlsItems.add(UrlsItem(urlsItems.size, url, type, title))
            writeSelectedUrlFromFile(UrlsItem(0, url, type, title))

            val urls = Urls(urlsItems)

            val gson = GsonBuilder().setPrettyPrinting().create()
            File(urlsBD).writeText(gson.toJson(urls))
        } catch (ex: Exception) {
            throw java.lang.Exception(ex.toString())
        }
    }
}
