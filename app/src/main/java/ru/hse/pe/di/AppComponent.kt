package ru.hse.pe.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import ru.hse.pe.presentation.MainActivity
import ru.hse.pe.presentation.auth.view.AuthFragment
import ru.hse.pe.presentation.auth.view.RegisterFragment
import ru.hse.pe.presentation.content.ContentFragment
import ru.hse.pe.presentation.content.type.article.view.ArticlesFragment
import ru.hse.pe.presentation.content.type.fact.view.FactsFragment
import ru.hse.pe.presentation.content.type.recommendation.view.RecommendationsFragment
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
    fun inject(fragment: FactsFragment)
    fun inject(fragment: RecommendationsFragment)
    fun inject(fragment: ArticlesFragment)
    fun inject(fragment: RegisterFragment)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun context(context: Context): Builder

        fun build(): AppComponent
    }
}