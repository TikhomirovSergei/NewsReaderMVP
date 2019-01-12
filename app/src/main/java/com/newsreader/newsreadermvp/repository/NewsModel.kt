package com.newsreader.newsreadermvp.repository

import com.google.gson.Gson
import com.newsreader.newsreadermvp.createRequest
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.File

//https://habr.com/rss/hubs/all/

class NewsModel(path: String) {
    private var urlsBD: String = "$path/Urls.json"

    //выбранный url
    //список прочитанных новостей
    interface OnFinishedListener {
        fun onSetUrlSuccess()
        fun onResultFail(str: String)
        fun onGetNewsDataSuccess(articles: ArrayList<JsonNewsItem>)
    }

    //проверка url на корректность
    fun requestValidateUrl(url: String, onFinishedListener: OnFinishedListener) {
        if (url != "") {
            val observable = createRequest(url)
                    .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())

            observable.subscribe({
                val jsonArray = readUrlsFile()

                val newObject = JSONObject()
                if (jsonArray.length() > 0)
                    newObject.put("id", jsonArray.length())
                else
                    newObject.put("id", 0)
                newObject.put("value", url)
                newObject.put("type", "json")
                jsonArray.put(newObject)

                val main = JSONObject()
                main.put("urls", jsonArray)

                File(urlsBD).writeText(main.toString())

                onFinishedListener.onSetUrlSuccess()
            }, {
                onFinishedListener.onResultFail(it.toString())
            })
        } else {
            onFinishedListener.onResultFail("url не может быть пустым.")
        }
    }

    //получение данных по корректному url
    fun getNewsData(onFinishedListener: OnFinishedListener) {
        try {
            //TODO если не указан выбранный url, то выводить первый из списка
            val jsonArray: JSONArray = readUrlsFile()
            if (jsonArray.length() > 0) {
                val urlsItem = Gson().fromJson(jsonArray.getJSONObject(0).toString(), UrlsItem::class.java)
                val observable = createRequest(urlsItem.value)
                        .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())

                observable.subscribe({
                    val articles = Gson().fromJson(it.toString(), JsonNews::class.java)

                    onFinishedListener.onGetNewsDataSuccess(articles.articles)
                }, {
                    onFinishedListener.onResultFail(it.toString())
                })
            } else {
                onFinishedListener.onResultFail("")
            }
        } catch (ex: java.lang.Exception) {
            onFinishedListener.onResultFail(ex.toString())
        }
    }

    private fun readUrlsFile(): JSONArray {
        try {
            File(urlsBD).createNewFile()
            val bufferedReader: BufferedReader = File(urlsBD).bufferedReader()
            var inputString = bufferedReader.use { it.readText() }

            val urls = Gson().fromJson(inputString, Urls::class.java)

            val jsonArray = JSONArray()
            if (urls != null) {
                for (url in urls.urls) {
                    val newObject = JSONObject()
                    newObject.put("id", url.id)
                    newObject.put("value", url.value)
                    jsonArray.put(newObject)
                }
            }

            return jsonArray
        } catch (ex: Exception) {
            throw ex
        }
    }
}