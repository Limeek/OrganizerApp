package ru.limeek.organizer.views

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.activity_location_details.*
import kotlinx.android.synthetic.main.toolbar.*
import ru.limeek.organizer.R
import ru.limeek.organizer.app.App
import ru.limeek.organizer.data.model.location.Location
import ru.limeek.organizer.di.components.ViewComponent
import ru.limeek.organizer.di.modules.ViewModelModule
import ru.limeek.organizer.util.Constants
import ru.limeek.organizer.viewmodels.LocationDetailsViewModel
import javax.inject.Inject

class LocationDetailsActivity : AppCompatActivity() {
    private val logTag = "LocationDetailsActivity"
    private val PLACE_PICKER_REQUEST = 1
    private val REQUEST_CODE_ASK_PERMISSIONS = 1
    private var component : ViewComponent? = null
    private var fromEventDetails: Boolean = false

    @Inject
    internal lateinit var viewModel: LocationDetailsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location_details)

        getViewComponent().inject(this)

        fromEventDetails = intent.getBooleanExtra(Constants.FROM_EVENT_DETAILS, false)
        viewModel.init(intent.extras?.getParcelable(Constants.LOCATION))
        initToolbar()
        observeLiveData()
        initViewListeners()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(resultCode == Activity.RESULT_OK)
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun observeLiveData(){
        viewModel.name.observe(this, Observer { etName.setText(it) })
        viewModel.address.observe(this, Observer { etAddress.setText(it) })
        viewModel.finish.observe(this, Observer{ finish(it) })
    }

    private fun initToolbar(){
        toolbar.title = title
        toolbar.inflateMenu(R.menu.event_details_menu)
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp)
        toolbar.setNavigationOnClickListener { finish() }
        toolbar.setOnMenuItemClickListener {
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
        etName.addTextChangedListener(nameTextWatcher)
        etAddress.addTextChangedListener(addressTextWatcher)
    }

    private fun getViewComponent() : ViewComponent {
        if(component == null){
            component = App.instance.component.newViewComponent(ViewModelModule(this))
        }
        return component!!
    }

    private fun finish(location: Location){
        if(fromEventDetails){
            val intent = Intent()
            intent.putExtra(Constants.LOCATION, location)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
        else{
            finish()
        }
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