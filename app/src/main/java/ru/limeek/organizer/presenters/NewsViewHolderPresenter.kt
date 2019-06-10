package ru.limeek.organizer.presenters

import com.squareup.picasso.Picasso
import ru.limeek.organizer.views.NewsViewHolder
import javax.inject.Inject

class NewsViewHolderPresenter @Inject constructor (val view: NewsViewHolder) {
    var position: Int = 0
    lateinit var newsAdapterPresenter: NewsAdapterPresenter

    fun onClick() {
        view.startNewWebActivity(newsAdapterPresenter.getItemAt(position).url)
    }

    fun bind(){
        val article = newsAdapterPresenter.news!!.articles!![position]
        view.source.text = article.source!!.name
        view.title.text = article.title
        Picasso.get()
                .load(article.urlToImage)
                .resize(150,150)
                .into(view.imgView)
    }
}