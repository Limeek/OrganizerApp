package ru.limeek.organizer.event.view

import ru.limeek.organizer.mvp.View

interface EventFragmentView : View {
    fun refreshRecyclerView()
    fun startDetailsActivity()
}