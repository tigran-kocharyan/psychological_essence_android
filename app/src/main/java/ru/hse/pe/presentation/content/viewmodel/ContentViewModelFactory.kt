package ru.hse.pe.presentation.content.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.hse.pe.domain.interactor.ContentInteractor
import ru.hse.pe.utils.scheduler.SchedulersProvider

/**
 * Фабрика для ViewModel [ContentViewModel]
 */
class ContentViewModelFactory(
    private val schedulersProvider: SchedulersProvider,
    private val contentInteractor: ContentInteractor
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ContentViewModel(
            schedulersProvider,
            contentInteractor
        ) as T
    }
}
