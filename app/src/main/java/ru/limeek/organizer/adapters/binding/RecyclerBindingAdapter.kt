package ru.limeek.organizer.adapters.binding

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import ru.limeek.organizer.data.model.event.Event
import ru.limeek.organizer.views.EventsListAdapter

object RecyclerBindingAdapter {
    @BindingAdapter("verticalDecoration")
    @JvmStatic
    fun addItemDecoration(recyclerView: RecyclerView, boolean: Boolean){
        if(boolean)
            recyclerView.addItemDecoration(DividerItemDecoration(recyclerView.context, DividerItemDecoration.VERTICAL))
        else
            recyclerView.removeItemDecoration(DividerItemDecoration(recyclerView.context, DividerItemDecoration.VERTICAL))
    }

    @BindingAdapter("data", "adapter")
    @JvmStatic
    fun bindEventToRecycler(recyclerView: RecyclerView, events: List<Event>?, adapter: EventsListAdapter?) {
        if (recyclerView.adapter == null && adapter != null)
            recyclerView.adapter = adapter

        if(events != null) {
            adapter?.dataset = events
            adapter?.notifyDataSetChanged()
        }
    }
}