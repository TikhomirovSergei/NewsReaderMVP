package com.newsreader.newsreadermvp

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.newsreader.newsreadermvp.presenter.MainPresenter
import kotlinx.android.synthetic.main.content_main.*

class SettingsActivity : AppCompatActivity(), MainViewContract {
    private lateinit var btn: Button
    private lateinit var eText: EditText

    private lateinit var presenter: MainPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        presenter = MainPresenter(this)
        progressBar.visibility = View.GONE

        eText = findViewById(R.id.settings_editText)
        btn = findViewById(R.id.settings_button)
        btn.setOnClickListener {
            presenter.getUrl(eText.text.toString())
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

    override fun onDestroy() {
        presenter.onDestroy()
        super.onDestroy()
    }

    override fun showProgress() {
        progressBar.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        progressBar.visibility = View.GONE
    }

    override fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    override fun navigateToHomeScreen() {
        val intent = Intent(this, MainActivity::class.java)
        this.startActivity(intent)
    }
}
