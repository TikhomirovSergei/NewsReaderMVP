package com.newsreader.newsreadermvp

import com.newsreader.newsreadermvp.repository.JsonNewsItem

interface MainViewContract {
    fun showProgress()
    fun hideProgress()
    fun showToast(msg: String)
    fun navigateToHomeScreen()
    fun setNewsData(feedList: ArrayList<JsonNewsItem>)
}