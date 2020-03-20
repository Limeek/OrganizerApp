package ru.limeek.organizer.presentation.views

import android.os.Bundle
import dagger.android.support.DaggerAppCompatActivity
import ru.limeek.organizer.R

class MainActivity : DaggerAppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        openMainFragment()
    }

    private fun openMainFragment(){
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.nav_host_fragment, MainFragment.newInstance(), MainFragment.TAG)
                .commit()
    }
}
