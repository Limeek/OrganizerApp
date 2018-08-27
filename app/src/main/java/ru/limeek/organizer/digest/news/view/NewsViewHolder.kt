package ru.limeek.organizer.digest.news.view

import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import kotlinx.android.synthetic.main.news_item.view.*
import ru.limeek.organizer.digest.news.presenter.NewsViewHolderPresenter

class NewsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val presenter : NewsViewHolderPresenter = NewsViewHolderPresenter(this)

    init {
        view.setOnClickListener{presenter.onClick()}
    }

    val title : TextView = view.tvTitle
    val source : TextView = view.tvSource
    val imgView : ImageView = view.ivNews

    fun startNewWebActivity(uri : String?){
        val intent = Intent(itemView.context, NewsWebActivity::class.java)
        intent.putExtra("uri", uri)
        itemView.context.startActivity(intent)
    }
}