package com.newsreader.newsreadermvp

import android.content.Intent
import android.net.Uri
import android.widget.TextView
import io.reactivex.Observable
import java.net.HttpURLConnection
import java.net.URL


/**
 * загрузка списка новостей
 */
fun createRequest(url: String) = Observable.create<String> {
    val urlConnection = URL(url).openConnection() as HttpURLConnection
    try {
        urlConnection.connect()
        if (urlConnection.responseCode != HttpURLConnection.HTTP_OK)
            it.onError(RuntimeException(urlConnection.responseMessage))
        else {
            val str = urlConnection.inputStream.bufferedReader().readText()
            it.onNext(str)
        }
    } finally {
        urlConnection.disconnect()
    }
}

/**
 * удаление T и Z из входной строки
 */
fun deleteTZFromPublishAT(publishedAt: String): String {
    val publishedAtText = publishedAt.replace("T", " ")
    return publishedAtText.replace("Z", "")
}

/**
 * вызов браузеров для открытия ссылки
 */
fun openUrl(textView: TextView, url: String) {
    val intent = Intent(Intent.ACTION_VIEW)
    intent.data = Uri.parse(url)
    textView.context.startActivity(intent)
}