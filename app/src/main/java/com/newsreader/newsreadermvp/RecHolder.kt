package com.newsreader.newsreadermvp

import android.content.Intent
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.newsreader.newsreadermvp.repository.JsonNewsItem
import com.squareup.picasso.Picasso

class RecHolder(view: View) : RecyclerView.ViewHolder(view) {
    fun bind(item: JsonNewsItem, isRead: Boolean) {
        val lLayout = itemView.findViewById<LinearLayout>(R.id.item_LLayout)
        if (isRead)
            lLayout.setBackgroundColor(Color.parseColor("#C0C0C0"))

        val vTitle = itemView.findViewById<TextView>(R.id.item_title)
        val vDesc = itemView.findViewById<TextView>(R.id.item_desc)
        val vImg = itemView.findViewById<ImageView>(R.id.item_img)
        val vData = itemView.findViewById<TextView>(R.id.item_publishedAt)

        vTitle.text = item.title
        vDesc.text = item.description
        vData.text = deleteTZFromPublishAT(item.publishedAt)

        if (item.urlToImage != "")
            Picasso.with(vImg.context).load(item.urlToImage).into(vImg)

        itemView.setOnClickListener {
            val intent = Intent(vImg.context, ContextNewsActivity::class.java)
            intent.putExtra("title", item.title)
            intent.putExtra("description", item.description)
            intent.putExtra("url", item.url)
            intent.putExtra("urlToImage", item.urlToImage)
            intent.putExtra("publishedAt", item.publishedAt)
            vImg.context.startActivity(intent)
        }
    }
}