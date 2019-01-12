package com.newsreader.newsreadermvp

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.newsreader.newsreadermvp.repository.JsonNewsItem

class RecAdapter(val items: ArrayList<JsonNewsItem>) : RecyclerView.Adapter<RecHolder>() {

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
        val item = items[position]

        holder?.bind(item)
    }
}