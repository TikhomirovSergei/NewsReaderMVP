package com.newsreader.newsreadermvp

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import com.newsreader.newsreadermvp.presenter.MainPresenter
import com.newsreader.newsreadermvp.repository.JsonNewsItem
import com.newsreader.newsreadermvp.repository.NewsModel
import kotlinx.android.synthetic.main.content_main.*


class SettingsActivity : AppCompatActivity(), MainViewContract {
    private lateinit var btn: Button
    private lateinit var eText: EditText
    private lateinit var lLayout: LinearLayout

    private lateinit var presenter: MainPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        setTitle(R.string.settings_title)

        val appPath = this.applicationInfo.dataDir

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        presenter = MainPresenter( this, NewsModel(appPath))
        progressBar.visibility = View.GONE

        eText = findViewById(R.id.settings_editText)
        btn = findViewById(R.id.settings_button)
        btn.setOnClickListener {
            presenter.setUrl(eText.text.toString())
        }

        lLayout = findViewById(R.id.settings_lLayout)
        lLayout.visibility = View.VISIBLE
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDestroy()
    }

    override fun showProgress() {
        progressBar.visibility = View.VISIBLE
        lLayout.visibility = View.GONE
    }

    override fun hideProgress() {
        progressBar.visibility = View.GONE
        lLayout.visibility = View.VISIBLE
    }

    override fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    override fun navigateToHomeScreen() {
        val intent = Intent(this, MainActivity::class.java)
        this.startActivity(intent)
    }

    override fun setNewsData(feedList: ArrayList<JsonNewsItem>) {
        return
    }
}
