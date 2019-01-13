package com.newsreader.newsreadermvp

import com.newsreader.newsreadermvp.repository.JsonNewsItem
import com.newsreader.newsreadermvp.repository.UrlsItem

interface MainViewContract {
    fun showProgress()
    fun hideProgress()
    fun showToast(msg: String)
    fun navigateToHomeScreen()
    fun setNewsData(feedList: ArrayList<JsonNewsItem>)
    fun setNavigationItems(urlsItemList: List<UrlsItem>)
}