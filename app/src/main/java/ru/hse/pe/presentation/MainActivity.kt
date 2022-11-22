package ru.hse.pe.presentation

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_main.*
import ru.hse.pe.R
import ru.hse.pe.databinding.ActivityMainBinding
import ru.hse.pe.presentation.article.view.ArticleFragment

/**
 * Main [Activity]. All fragments will be added on top of it.
 * Created by Kocharyan Tigran on 04.03.2022.
 */
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        this.supportActionBar?.hide()
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragment_container, ArticleFragment.newInstance(), ArticleFragment.TAG)
                .commit()
        }
        bottom_nav.setOnItemSelectedListener {
            when (it.itemId) {
//                R.id.profile -> setCurrentFragment(ProfileFragment.newInstance(), ProfileFragment.TAG)
                R.id.trending -> {}
                R.id.shop -> {}
                R.id.settings -> {}
            }
            true
        }
    }

    fun isBottomNavVisible(isVisible: Boolean) {
        if (isVisible) {
            bottom_nav.visibility = View.VISIBLE
        } else {
            bottom_nav.visibility = View.GONE
        }
    }

    private fun setCurrentFragment(fragment: Fragment, tag: String) =
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, fragment, tag)
            .commit()
}