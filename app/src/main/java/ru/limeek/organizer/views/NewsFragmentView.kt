package ru.limeek.organizer.views

import androidx.recyclerview.widget.RecyclerView

interface NewsFragmentView {
    var recView: RecyclerView
    var recAdapter : NewsAdapter
}