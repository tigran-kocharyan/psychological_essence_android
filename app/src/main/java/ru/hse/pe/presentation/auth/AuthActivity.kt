package ru.hse.pe.presentation.auth

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.hse.pe.R
import ru.hse.pe.presentation.auth.view.AuthFragment

/**
 * Основное Activity, на которое помещаются фрагменты авторизации
 */
class AuthActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .setCustomAnimations(
                    R.anim.slide_in,
                    R.anim.fade_out,
                    R.anim.pop_enter,
                    R.anim.pop_exit
                )
                .add(R.id.fragment_container, AuthFragment.newInstance(), AuthFragment.TAG)
                    .commit()
        }
    }
}