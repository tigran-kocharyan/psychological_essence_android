package ru.hse.pe.presentation

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_main.*
import ru.hse.pe.R
import ru.hse.pe.SharedViewModel
import ru.hse.pe.databinding.ActivityMainBinding
import ru.hse.pe.presentation.auth.view.LoginFragment
import ru.hse.pe.presentation.content.ContentFragment
import ru.hse.pe.presentation.content.type.courses.CoursesFragment
import ru.hse.pe.presentation.shop.ShopFragment
import ru.hse.pe.presentation.shop.SubscriptionFragment

/**
 * Main [Activity]. All fragments will be added on top of it.
 * Created by Kocharyan Tigran on 04.03.2023.
 */
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val sharedViewModel: SharedViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        this.supportActionBar?.hide()
        sharedViewModel.setUid(intent.getStringExtra(LoginFragment.EXTRA_AUTH_DATA) ?: "")
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragment_container, ContentFragment.newInstance(), ContentFragment.TAG)
                .commit()
        }
        bottom_nav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.content -> setCurrentFragment(
                    ContentFragment.newInstance(),
                    ContentFragment.TAG
                )
                R.id.shop -> setCurrentFragment(
                    SubscriptionFragment.newInstance(),
                    SubscriptionFragment.TAG
                )
                R.id.profile -> setCurrentFragment(
                    PlaceholderFragment.newInstance(),
                    PlaceholderFragment.TAG
                )
                R.id.course -> setCurrentFragment(
                    CoursesFragment.newInstance(),
                    CoursesFragment.TAG
                )
            }
            true
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        supportFragmentManager.popBackStack()
        return true
    }

    fun isBottomNavVisible(isVisible: Boolean) {
        if (isVisible) {
            bottom_nav.visibility = View.VISIBLE
        } else {
            bottom_nav.visibility = View.GONE
        }
    }

    fun setDefaultMenuItemSelected(id: Int) {
        bottom_nav.selectedItemId = id
    }

    fun setCurrentFragment(fragment: Fragment, tag: String) =
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, fragment, tag)
            .commit()
}