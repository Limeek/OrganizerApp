package ru.limeek.organizer.digest.news.presenter

import android.util.Log
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import ru.limeek.organizer.app.App
import ru.limeek.organizer.di.modules.RepositoryModule
import ru.limeek.organizer.digest.news.view.NewsAdapter
import ru.limeek.organizer.model.news.Article
import ru.limeek.organizer.model.news.News
import ru.limeek.organizer.model.repository.Repository
import javax.inject.Inject

class NewsAdapterPresenter (val newsAdapter : NewsAdapter) {

    var news : News? = null
    var disposable : Disposable? = null

    @Inject
    lateinit var repository : Repository

    init{
        App.instance.component.newPresenterComponent(RepositoryModule()).inject(this)
        getNews()
    }

    fun getNews(){
        disposable =
                repository.newsApi
                .getRecentNews()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(
                        {news ->
                            this.news = news
                            newsAdapter.notifyDataSetChanged()},
                        {error -> Log.wtf("NewsAdapterPresenter", "Error")
                            error.printStackTrace()
                        }
                )
    }

    fun getCount(): Int{
        if(news != null)
            return news!!.totalResults!!
        return 0
    }

    fun getItemAt(position: Int) : Article{
        return news!!.articles!![position]
    }

    fun onDestroy(){
        disposable!!.dispose()
        disposable = null
    }

}