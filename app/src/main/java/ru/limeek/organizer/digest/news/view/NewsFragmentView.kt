package ru.limeek.organizer.digest.news.view

import android.support.v7.widget.RecyclerView

interface NewsFragmentView {
    var recView: RecyclerView
    var recAdapter : NewsAdapter
}