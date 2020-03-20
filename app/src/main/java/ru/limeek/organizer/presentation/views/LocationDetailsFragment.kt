package ru.limeek.organizer.presentation.views

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import dagger.android.support.DaggerFragment
import ru.limeek.organizer.R
import ru.limeek.organizer.databinding.FragmentLocationDetailsBinding
import ru.limeek.organizer.domain.entities.location.Location
import ru.limeek.organizer.presentation.util.Constants
import ru.limeek.organizer.presentation.viewmodels.LocationDetailsViewModel
import javax.inject.Inject

class LocationDetailsFragment : DaggerFragment() {
    private val logTag = "LocationDetailsFragment"
    private val PLACE_PICKER_REQUEST = 1
    private val REQUEST_CODE_ASK_PERMISSIONS = 1
    private var fromEventDetails: Boolean = false

    @Inject
    internal lateinit var viewModel: LocationDetailsViewModel
    private lateinit var binding: FragmentLocationDetailsBinding

    private val args by navArgs<LocationDetailsFragmentArgs>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentLocationDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fromEventDetails = args.fromEventDetails
        viewModel.init(arguments?.getParcelable(Constants.LOCATION))
        initToolbar()
        observeLiveData()
        initViewListeners()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(resultCode == Activity.RESULT_OK)
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun observeLiveData(){
        viewModel.name.observe(viewLifecycleOwner, Observer { binding.etName.setText(it) })
        viewModel.address.observe(viewLifecycleOwner, Observer { binding.etAddress.setText(it) })
        viewModel.finish.observe(viewLifecycleOwner, Observer{ finish(it) })
    }

    private fun initToolbar(){
        binding.includeToolbar.toolbar.title = getString(R.string.title_locations)
        binding.includeToolbar.toolbar.inflateMenu(R.menu.event_details_menu)
        binding.includeToolbar.toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp)
        binding.includeToolbar.toolbar.setNavigationOnClickListener { finish(null) }
        binding.includeToolbar.toolbar.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.action_submit -> {
                    viewModel.submit()
                    true
                }
                R.id.action_delete ->{
                    viewModel.delete()
                    true
                }
                else -> false
            }
        }
    }

    private fun initViewListeners(){
        binding.etName.addTextChangedListener(nameTextWatcher)
        binding.etAddress.addTextChangedListener(addressTextWatcher)
    }

    private fun finish(location: Location?){
        if(location != null)
            findNavController().navigate(LocationDetailsFragmentDirections
                 .actionLocationDetailsFragmentToEventDetailsFragment())
        else
            findNavController().popBackStack()
    }

    private val nameTextWatcher = object: TextWatcher{
        override fun afterTextChanged(p0: Editable?) {
            viewModel.updateName(p0.toString())
        }
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
    }

    private val addressTextWatcher = object: TextWatcher{
        override fun afterTextChanged(p0: Editable?) {
            viewModel.updateAddress(p0.toString())
        }
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
    }
}