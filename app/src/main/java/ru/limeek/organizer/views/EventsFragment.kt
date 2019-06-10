package ru.limeek.organizer.views

import android.content.Intent
import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.DividerItemDecoration.VERTICAL
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.fragment_calendar_events.*
import ru.limeek.organizer.R
import ru.limeek.organizer.app.App
import ru.limeek.organizer.di.components.ViewComponent
import ru.limeek.organizer.di.modules.PresenterModule
import ru.limeek.organizer.presenters.EventsPresenter
import javax.inject.Inject

class EventsFragment : EventFragmentView, Fragment(){

    val logTag = "EventsFragment"

    lateinit var recView : RecyclerView
    var recAdapter : EventsListAdapter? = null
    lateinit var fab : FloatingActionButton
    @Inject
    lateinit var presenter : EventsPresenter

    private var component : ViewComponent? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view : View = inflater.inflate(R.layout.fragment_calendar_events,container,false)

        getViewComponent().inject(this)

        presenter.onCreate()

        recAdapter = EventsListAdapter(context)
        recView = view.findViewById(R.id.recViewEvents)
        recView.apply {
            setHasFixedSize(true)
            addItemDecoration(DividerItemDecoration(context, VERTICAL))
            layoutManager = LinearLayoutManager(context)
            adapter = recAdapter
        }

        fab = view.findViewById(R.id.floatingButton)
        fab.setOnClickListener(presenter.onFabClick())

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        refreshDateText()
    }

    override fun startDetailsActivity() {
        val intent = Intent(activity, EventDetailsActivity::class.java)
        startActivity(intent)
    }

    override fun refreshRecyclerView() {
        recAdapter!!.refreshRecycler()
    }

    private fun getViewComponent() : ViewComponent{
        if(component == null){
            component = App.instance.component.newViewComponent(PresenterModule(this))
        }
        return component!!
    }

    private fun refreshDateText(){
        tvCurrentDate.text = presenter.getCurrentDateString()
    }

    fun refresh(){
        refreshRecyclerView()
        refreshDateText()
    }

    override fun onDestroy() {
        super.onDestroy()
        recAdapter!!.onDestroy()
        presenter.onDestroy()
        component = null
    }
}