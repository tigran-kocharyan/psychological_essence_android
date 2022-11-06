package ru.hse.pe.auth

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.hse.pe.R
import ru.hse.pe.auth.view.LoginFragment

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
                    .add(R.id.fragment_container, LoginFragment.newInstance(), LoginFragment.TAG)
                    .commit()
        }
    }
}