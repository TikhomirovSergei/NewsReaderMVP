package com.newsreader.newsreadermvp.presenter

import com.newsreader.newsreadermvp.MainViewContract
import com.newsreader.newsreadermvp.repository.JsonNewsItem
import com.newsreader.newsreadermvp.repository.NewsModel

class MainPresenter(private var mainViewContract: MainViewContract?, private val NewsModel: NewsModel)
    : NewsModel.OnFinishedListener {

    fun onDestroy() {
        mainViewContract = null
    }

    fun setUrl(url: String) {
        mainViewContract?.showProgress()
        NewsModel.requestValidateUrl(url, this)
    }

    fun getNewsData() {
        mainViewContract?.showProgress()
        NewsModel.getNewsData(this)
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

    override fun onGetNewsDataSuccess(articles: ArrayList<JsonNewsItem>){
        mainViewContract?.hideProgress()
        mainViewContract?.setNewsData(articles)
    }
}