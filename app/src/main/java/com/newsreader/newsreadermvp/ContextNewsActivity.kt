package com.newsreader.newsreadermvp

import android.annotation.SuppressLint
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso

class ContextNewsActivity : AppCompatActivity() {
    private lateinit var titleTextView: TextView
    private lateinit var moreTextView: TextView
    private lateinit var publishedAtTextView: TextView
    private lateinit var descTextView: TextView
    private lateinit var imgImageView: ImageView

    /**
     * заполнение view'ек и их отображение
     */
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
    }

    @SuppressLint("SetTextI18n")
    override fun onResume() {
        super.onResume()

        var author = intent.getStringExtra("author")
        val title = intent.getStringExtra("title")
        val description = intent.getStringExtra("description")
        val url = intent.getStringExtra("url")
        val urlToImage = intent.getStringExtra("urlToImage")
        val publishedAt = intent.getStringExtra("publishedAt")

        author = if (author !== null)
            "by $author / "
        else
            ""

        titleTextView.text = title
        publishedAtTextView.text = author + deleteTZFromPublishAT(publishedAt)
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
}
