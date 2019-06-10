package ru.limeek.organizer.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.limeek.organizer.R

class NewsFragment : NewsFragmentView, Fragment() {

    override lateinit var recAdapter: NewsAdapter
    override lateinit var recView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_news, container, false)

        recAdapter = NewsAdapter(context!!)
        recView = view.findViewById(R.id.recyclerView)
        recView.apply{
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
            adapter = recAdapter
        }

        return view
    }


    override fun onDestroy() {
        super.onDestroy()
        recAdapter.onDestroy()
    }
}