package ru.limeek.organizer.views

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import ru.limeek.organizer.R
import ru.limeek.organizer.presenters.NewsAdapterPresenter

class NewsAdapter(val context: Context) : RecyclerView.Adapter<NewsViewHolder>() {
    var presenter : NewsAdapterPresenter = NewsAdapterPresenter(this)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        return NewsViewHolder(LayoutInflater.from(context).inflate(R.layout.news_item, parent, false))
    }

    override fun getItemCount(): Int {
        return presenter.getCount()
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.presenter.newsAdapterPresenter = presenter
        holder.presenter.position = position
        holder.presenter.bind()
    }

    fun onDestroy(){
        presenter.onDestroy()
    }
}