package ru.limeek.organizer.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import ru.limeek.organizer.app.App
import ru.limeek.organizer.databinding.CalendarFragmentBinding
import ru.limeek.organizer.di.modules.ViewModelModule
import ru.limeek.organizer.viewmodels.CalendarViewModel
import javax.inject.Inject

class CalendarFragment: Fragment() {
    private lateinit var binding: CalendarFragmentBinding

    @Inject
    lateinit var viewModel: CalendarViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = CalendarFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        App.instance.component.newViewViewModelComponent(ViewModelModule(this)).inject(this)
        viewModel.onCreate()
        binding.viewModel = viewModel
        observeLiveData()
    }

    private fun observeLiveData(){
        viewModel.refreshEventFragment.observe(this, Observer{
            (activity as? MainActivity)?.refreshEventsFragment()
        })
    }
}