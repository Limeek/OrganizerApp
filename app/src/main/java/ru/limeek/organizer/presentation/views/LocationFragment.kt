package ru.limeek.organizer.presentation.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.GravityCompat
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.material.navigation.NavigationView
import dagger.android.support.DaggerFragment
import ru.limeek.organizer.R
import ru.limeek.organizer.databinding.FragmentLocationBinding
import ru.limeek.organizer.domain.entities.location.Location
import ru.limeek.organizer.presentation.adapter.LocationsAdapter
import ru.limeek.organizer.presentation.viewmodels.LocationViewModel
import javax.inject.Inject

class LocationFragment : DaggerFragment() {

    private val REQUEST_CODE_ASK_PERMISSIONS = 1

    private val adapter: LocationsAdapter by lazy {
        LocationsAdapter().apply {
            binding.recyclerView.adapter = this
            onItemClick = onAdapterItemClick
        }
    }

    @Inject
    lateinit var viewModel: LocationViewModel
    private lateinit var binding: FragmentLocationBinding

    private var onAdapterItemClick = fun(location: Location) {
        startDetailsActivity(location)
    }

    private val navigationItemClick = NavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_calendar -> startCalendarActivity()
        }
        true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                binding.drawerLayout.openDrawer(GravityCompat.START)
                true
            }
            else -> super.onOptionsItemSelected(item)

        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentLocationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initToolbar()
        binding.navView.setNavigationItemSelectedListener(navigationItemClick)
        observeLiveData()
    }

    private fun initToolbar(){
        binding.includeToolbar.toolbar.setNavigationIcon(R.drawable.ic_menu_white_24dp)
        binding.includeToolbar.toolbar.setNavigationOnClickListener{ binding.drawerLayout.openDrawer(GravityCompat.START) }
        binding.includeToolbar.toolbar.title = getString(R.string.title_locations)
        binding.navView.setNavigationItemSelectedListener(navigationItemClick)
    }

    private fun startDetailsActivity(location: Location? = null) {
        val direction = LocationFragmentDirections.actionLocationFragmentToLocationDetailsFragment(false, location)
        findNavController().navigate(direction)
    }

    private fun observeLiveData() {
        viewModel.locations.observe(viewLifecycleOwner, Observer {
            adapter.dataset = it
            adapter.notifyDataSetChanged()
        })

        viewModel.startDetailsActivity.observe(viewLifecycleOwner, Observer {
            startDetailsActivity()
        })
    }

    private fun startCalendarActivity() {
        val direction = LocationFragmentDirections.actionLocationFragmentToMainFragment()
        findNavController().navigate(direction)
    }
}