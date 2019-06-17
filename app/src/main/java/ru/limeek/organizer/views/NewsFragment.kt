package ru.limeek.organizer.views

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.limeek.organizer.app.App
import ru.limeek.organizer.data.model.news.Article
import ru.limeek.organizer.databinding.FragmentNewsBinding
import ru.limeek.organizer.di.modules.ViewModelModule
import ru.limeek.organizer.viewmodels.NewsViewModel
import javax.inject.Inject

class NewsFragment : Fragment() {

    private lateinit var binding: FragmentNewsBinding
    @Inject
    lateinit var viewModel: NewsViewModel

    private var onAdapterItemClick = fun(article: Article){ openNewsWebView(article) }
    var adapter = NewsAdapter().apply { onItemClick = onAdapterItemClick }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentNewsBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        injectComponent()
        binding.viewModel = viewModel
        binding.adapter = adapter
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