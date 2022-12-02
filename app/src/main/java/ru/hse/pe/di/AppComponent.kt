package ru.hse.pe.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import ru.hse.pe.presentation.MainActivity
import ru.hse.pe.presentation.auth.view.AuthFragment
import ru.hse.pe.presentation.content.ContentFragment
import javax.inject.Singleton

/**
 * Common component fot the whole project.
 * Created by Kocharyan Tigran on 04.03.2022.
 */
@Singleton
@Component(
    modules = [
        BindModule::class, RetrofitModule::class
    ]
)
interface AppComponent {
    fun inject(launcher: MainActivity)
    fun inject(fragment: AuthFragment)
    fun inject(fragment: ContentFragment)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun context(context: Context): Builder

        fun build(): AppComponent
    }
}