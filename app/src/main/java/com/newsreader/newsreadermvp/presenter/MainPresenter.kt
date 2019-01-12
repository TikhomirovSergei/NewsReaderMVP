package com.newsreader.newsreadermvp.presenter

import com.newsreader.newsreadermvp.MainViewContract

class MainPresenter(private var mainViewContract: MainViewContract?) {

    fun onDestroy() {
        mainViewContract = null
    }

    fun getUrl(url: String) {
        if (url != "") {
            mainViewContract?.showProgress()
            //пришел введенных пользователем url -> необходимо валидировать его,
            // при true - добавить в базу, иначе - сообщение об ошибке
            mainViewContract?.navigateToHomeScreen()
        } else {
            mainViewContract?.showToast("url не может быть пустым.")
        }
    }
}