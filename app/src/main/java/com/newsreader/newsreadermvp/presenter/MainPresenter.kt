package com.newsreader.newsreadermvp.presenter

import com.newsreader.newsreadermvp.MainViewContract
import com.newsreader.newsreadermvp.repository.JsonNewsItem
import com.newsreader.newsreadermvp.repository.NewsModel
import com.newsreader.newsreadermvp.repository.UrlsItem

class MainPresenter(private var mainViewContract: MainViewContract?, private val NewsModel: NewsModel)
    : NewsModel.OnFinishedListener {

    fun onDestroy() {
        mainViewContract = null
    }

    fun setUrl(title: String, url: String) {
        mainViewContract?.showProgress()
        NewsModel.requestValidateUrl(title, url, this)
    }

    fun setSelectedUrl(itemId: Int) {
        val jsonArrayUrls: ArrayList<UrlsItem>? = NewsModel.getUrlList()
        if (jsonArrayUrls != null) {
            NewsModel.writeSelectedUrlFromFile(jsonArrayUrls[itemId])
        } else {
            onResultFail("Запись в файл выбранного url не удалась.")
        }
    }

    fun getNewsData(mainScroll: Boolean) {
        try {
            if (mainScroll)
                mainViewContract?.showProgress()
            val selectedUrl = NewsModel.readSelectedUrlFromFile()
            NewsModel.getNewsData(selectedUrl.value, selectedUrl.type, this)
        } catch (ex: Exception) {
            onResultFail("")
        }
    }

    fun getUrlList() {
        try {
            val jsonArrayUrls: ArrayList<UrlsItem>? = NewsModel.getUrlList()

            if (jsonArrayUrls != null)
                mainViewContract?.setNavigationItems(jsonArrayUrls)

        } catch (ex: Exception) {
            mainViewContract?.showToast("При загрузке новостных потоков произошла ошибка. $ex")
        }
    }

    fun setReadedNews(newsItem: JsonNewsItem) {
        try {
            val selectedUrl = NewsModel.readSelectedUrlFromFile()
            NewsModel.writeReadedNewsFromFile(selectedUrl, newsItem)
        } catch (ex: java.lang.Exception) {
            onResultFail(ex.toString())
        }
    }

    fun getListReadedNews(): ArrayList<JsonNewsItem>? {
        try {
            val selectedUrl = NewsModel.readSelectedUrlFromFile()
            val selectedNews = NewsModel.readReadedNewsFromFile()
            if (selectedNews != null) {
                for (i in 0 until selectedNews.selected.size) {
                    if (selectedNews.selected[i].url.value == selectedUrl.value) {
                        return selectedNews.selected[i].url.items
                    }
                }
            }
            return null
        } catch (ex: java.lang.Exception) {
            onResultFail(ex.toString())
        }
        return null
    }

    override fun onSetUrlSuccess() {
        mainViewContract?.hideProgress()
        mainViewContract?.navigateToHomeScreen()
    }

    override fun onResultFail(str: String) {
        mainViewContract?.hideProgress()
        if (str != "")
            mainViewContract?.showToast("Произошла ошибка: $str")
        else
            mainViewContract?.showToast("Новостных потоков не обнаружено")
    }

    override fun onGetNewsDataSuccess(articles: ArrayList<JsonNewsItem>) {
        mainViewContract?.hideProgress()
        mainViewContract?.setNewsData(articles)
    }
}