package com.newsreader.newsreadermvp

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.newsreader.newsreadermvp.repository.JsonNewsItem

class RecAdapter(private val items: ArrayList<JsonNewsItem>, private val listReadedNews: ArrayList<JsonNewsItem>?) : RecyclerView.Adapter<RecHolder>() {

    /**
     * прорисовка view
     */
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecHolder {
        val inflater = LayoutInflater.from(parent!!.context)
        val view = inflater.inflate(R.layout.list_item, parent, false)

        return RecHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    /**
     * заполение view
     */
    override fun onBindViewHolder(holder: RecHolder?, position: Int) {
        var b: Boolean = false
        val item = items[position]
        val selectedItems = listReadedNews
        if (selectedItems != null) {
            for (i in 0 until selectedItems.size) {
                if ((item.title == selectedItems[i].title) &&
                        (item.description == selectedItems[i].description) &&
                        (item.url == selectedItems[i].url) &&
                        (item.urlToImage == selectedItems[i].urlToImage) &&
                        (item.publishedAt == selectedItems[i].publishedAt)){
                    b = true
                    break
                }
            }
        }

        holder?.bind(item, b)
    }
}