package com.newsreader.newsreadermvp

interface MainViewContract {
    fun showProgress()
    fun hideProgress()
    fun showToast(msg: String)
    fun navigateToHomeScreen()
}