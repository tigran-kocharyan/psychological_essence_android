package ru.hse.pe.presentation.auth

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.hse.pe.R
import ru.hse.pe.presentation.auth.view.AuthFragment
import ru.hse.pe.utils.Utils.setCommonAnimations

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
                .setCommonAnimations()
                .add(R.id.fragment_container, AuthFragment.newInstance(), AuthFragment.TAG)
                .commit()
        }
    }
}