package ru.limeek.organizer.views

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_news.*
import ru.limeek.organizer.R
import ru.limeek.organizer.adapter.NewsAdapter
import ru.limeek.organizer.app.App
import ru.limeek.organizer.data.model.news.Article
import ru.limeek.organizer.di.modules.ViewModelModule
import ru.limeek.organizer.viewmodels.NewsViewModel
import javax.inject.Inject

class NewsFragment : Fragment() {

    @Inject
    lateinit var viewModel: NewsViewModel

    private var onAdapterItemClick = fun(article: Article){ openNewsWebView(article) }
    val adapter: NewsAdapter by lazy{NewsAdapter().apply {
        recyclerView.adapter = this
        onItemClick = onAdapterItemClick
    }}

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_news, container, false);
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        injectComponent()
    }

    private fun openNewsWebView(article: Article){
        val intent = Intent(context, NewsWebActivity::class.java)
        intent.putExtra("uri", article.url)
        startActivity(intent)
    }

    private fun injectComponent(){
        App.instance.component.newViewViewModelComponent(ViewModelModule(this)).inject(this)
    }
}