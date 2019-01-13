package com.newsreader.newsreadermvp

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.google.gson.Gson
import com.newsreader.newsreadermvp.presenter.MainPresenter
import com.newsreader.newsreadermvp.repository.JsonNewsItem
import com.newsreader.newsreadermvp.repository.NewsModel
import com.newsreader.newsreadermvp.repository.UrlsItem
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, MainViewContract {
    private lateinit var presenter: MainPresenter

    private lateinit var vRecView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val appPath = this.applicationInfo.dataDir

        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        val drawer = findViewById<View>(R.id.drawer_layout) as DrawerLayout
        val toggle = ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer.addDrawerListener(toggle)
        toggle.syncState()

        val navigationView = findViewById<View>(R.id.nav_view) as NavigationView
        navigationView.setNavigationItemSelectedListener(this)

        presenter = MainPresenter(this, NewsModel(appPath))
        progressBar.visibility = View.GONE

        vRecView = findViewById(R.id.mainAct_recView)

        swipe_refresh_layout.setOnRefreshListener {
            presenter.getNewsData(false)
        }
    }

    override fun onResume() {
        super.onResume()
        presenter.getUrlList() //для отображения навигации
        presenter.getNewsData(true)
    }

    override fun onBackPressed() {
        val drawer = findViewById<View>(R.id.drawer_layout) as DrawerLayout
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        return if (id == R.id.action_settings) {
            val intent = Intent(this, SettingsActivity::class.java)
            this.startActivity(intent)
            true
        } else super.onOptionsItemSelected(item)

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        //нажата клавиша навигации
        presenter.setSelectedUrl(item.itemId)
        presenter.getNewsData(true)

        val drawer = findViewById<View>(R.id.drawer_layout) as DrawerLayout
        drawer.closeDrawer(GravityCompat.START)
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
        swipe_refresh_layout.isRefreshing = false
    }

    override fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    override fun navigateToHomeScreen() {
        return
    }

    override fun setNewsData(feedList: ArrayList<JsonNewsItem>) {
        val listReadedNews = presenter.getListReadedNews()
        vRecView.adapter = RecAdapter(feedList, listReadedNews)
        vRecView.layoutManager = LinearLayoutManager(this)
    }

    override fun setNavigationItems(urlsItemList: List<UrlsItem>) {
        val navView = findViewById<View>(R.id.nav_view) as NavigationView
        val menu = navView.menu
        menu.clear()
        for (itemList in urlsItemList) {
            menu.add(R.id.menu_group, itemList.id, Menu.NONE, itemList.title)
        }
    }
}
