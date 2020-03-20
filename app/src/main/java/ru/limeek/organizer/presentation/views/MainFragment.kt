package ru.limeek.organizer.presentation.views

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.FrameLayout
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.navigation.NavigationView
import dagger.android.support.DaggerFragment
import ru.limeek.organizer.R
import ru.limeek.organizer.databinding.FragmentMainBinding
import ru.limeek.organizer.domain.entities.event.Event
import ru.limeek.organizer.presentation.viewmodels.MainViewModel
import javax.inject.Inject

class MainFragment: DaggerFragment(){
    companion object{
        const val TAG = "MainFragment"

        fun newInstance(): Fragment{
            return MainFragment()
        }
    }

    @Inject
    lateinit var viewModel: MainViewModel
    private lateinit var binding: FragmentMainBinding

    private lateinit var calendarFragment: CalendarFragment
    private lateinit var eventFragment: EventsFragment
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<FrameLayout>
    private val REQUEST_CODE_ASK_PERMISSIONS = 1

    private val navigationItemClick = NavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_locations -> startLocationsFragment()
        }
        true
    }

    private val contentGlobalLayoutListener = ViewTreeObserver.OnGlobalLayoutListener {
        val contentHeight = binding.content.measuredHeight
        bottomSheetBehavior.peekHeight = binding.container.height - contentHeight
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.content.viewTreeObserver.addOnGlobalLayoutListener(contentGlobalLayoutListener)

        binding.floatingButton.setOnClickListener { viewModel.onFabClick() }

        observeLiveData()
        initToolbar()
        initFragments()
        initBottomSheet()
        checkAndRequestPermissions()
    }

    fun startEventDetailsFragment(event: Event? = null) {
        val directions = MainFragmentDirections.actionMainFragmentToEventDetailsFragment(event)
        findNavController().navigate(directions)
    }

    fun refreshEventsFragment() {
        eventFragment.refresh()
    }

    private fun observeLiveData(){
        viewModel.openEventDetailsFrag.observe(viewLifecycleOwner, Observer { startEventDetailsFragment(null) })
    }

    private fun initToolbar(){
        binding.includeToolbar.toolbar.setNavigationIcon(R.drawable.ic_menu_white_24dp)
        binding.includeToolbar.toolbar.setNavigationOnClickListener{ binding.drawerLayout.openDrawer(GravityCompat.START) }
        binding.includeToolbar.toolbar.title = getString(R.string.app_name)
        binding.navView.setNavigationItemSelectedListener(navigationItemClick)
    }

    private fun startLocationsFragment() {
        val directions = MainFragmentDirections.actionMainFragmentToLocationFragment()
        findNavController().navigate(directions)
    }

    private fun initFragments() {
        calendarFragment = CalendarFragment()
        eventFragment = EventsFragment()
        childFragmentManager.beginTransaction().replace(R.id.calendarFragContainer, calendarFragment).commit()
        childFragmentManager.beginTransaction().replace(R.id.fragContainer, eventFragment).commit()
    }

    private fun initBottomSheet(){
        bottomSheetBehavior = BottomSheetBehavior.from(binding.fragContainer)
    }

    private fun checkAndRequestPermissions() {
        context?.let {
            if (ActivityCompat.checkSelfPermission(it, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(it, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)

                requestPermissions(
                        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE),
                        REQUEST_CODE_ASK_PERMISSIONS
                )
        }
    }
}