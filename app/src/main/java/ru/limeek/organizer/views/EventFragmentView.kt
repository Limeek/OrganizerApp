package ru.limeek.organizer.views

interface EventFragmentView : View {
    fun refreshRecyclerView()
    fun startDetailsActivity()
}