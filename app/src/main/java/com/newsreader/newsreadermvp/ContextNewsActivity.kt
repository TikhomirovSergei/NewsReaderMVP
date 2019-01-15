package com.newsreader.newsreadermvp

import android.annotation.SuppressLint
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.newsreader.newsreadermvp.presenter.MainPresenter
import com.newsreader.newsreadermvp.repository.JsonNewsItem
import com.newsreader.newsreadermvp.repository.NewsModel
import com.newsreader.newsreadermvp.repository.UrlsItem
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.content_main.*

class ContextNewsActivity : AppCompatActivity(), MainViewContract {
    private lateinit var presenter: MainPresenter

    private lateinit var titleTextView: TextView
    private lateinit var moreTextView: TextView
    private lateinit var publishedAtTextView: TextView
    private lateinit var descTextView: TextView
    private lateinit var imgImageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_context_news)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setTitle(R.string.context_news)

        titleTextView = findViewById(R.id.news_context_title)
        moreTextView = findViewById(R.id.news_context_more)
        publishedAtTextView = findViewById(R.id.news_context_publishedAt)
        imgImageView = findViewById(R.id.news_context_img)
        descTextView = findViewById(R.id.news_context_desc)

        val appPath = this.applicationInfo.dataDir
        presenter = MainPresenter(this, NewsModel(appPath))
    }

    /**
     * заполнение view'ек и их отображение
     */
    @SuppressLint("SetTextI18n")
    override fun onResume() {
        super.onResume()

        val title = intent.getStringExtra("title")
        val description = intent.getStringExtra("description")
        val url = intent.getStringExtra("url")
        val urlToImage = intent.getStringExtra("urlToImage")
        val publishedAt = intent.getStringExtra("publishedAt")

        presenter.setReadedNews(JsonNewsItem(title, description, url, urlToImage, publishedAt))

        titleTextView.text = title
        publishedAtTextView.text = deleteTZFromPublishAT(publishedAt)
        Picasso.with(this).load(urlToImage).into(imgImageView)
        descTextView.text = description

        titleTextView.setOnClickListener {
            openUrl(titleTextView, url)
        }
        moreTextView.setOnClickListener {
            openUrl(titleTextView, url)
        }
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

    override fun showProgress() {}

    override fun hideProgress() {}

    override fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    override fun navigateToHomeScreen() {
        val intent = Intent(this, MainActivity::class.java)
        this.startActivity(intent)
    }

    override fun setNewsData(feedList: ArrayList<JsonNewsItem>) {}

    override fun setNavigationItems(urlsItemList: List<UrlsItem>) {}
}
