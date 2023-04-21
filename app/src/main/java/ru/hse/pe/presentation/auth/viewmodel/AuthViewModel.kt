package ru.hse.pe.presentation.auth.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import ru.hse.pe.domain.interactor.AuthInteractor
import ru.hse.pe.domain.model.UserEntity
import ru.hse.pe.presentation.content.viewmodel.ContentViewModel
import ru.hse.pe.utils.scheduler.SchedulersProvider


/**
 * ViewModel for Profile related actions.
 */
class AuthViewModel(
    private val schedulers: SchedulersProvider,
    private val authInteractor: AuthInteractor
) : ViewModel() {
    private val _progressLiveData = MutableLiveData<Boolean>()
    val progressLiveData: LiveData<Boolean> get() = _progressLiveData

    private val _errorLiveData = MutableLiveData<Throwable>()
    val errorLiveData: LiveData<Throwable> get() = _errorLiveData

    private val _userLiveData = MutableLiveData<UserEntity>()
    val userLiveData: LiveData<UserEntity> get() = _userLiveData


    private val disposables = CompositeDisposable()

    fun addUser(user: UserEntity) {
        disposables.add(authInteractor.addUser(user)
            .observeOn(Schedulers.io()).subscribeOn(Schedulers.io())
            .doOnSubscribe { _progressLiveData.postValue(true) }
            .doAfterTerminate { _progressLiveData.postValue(false) }
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.ui())
            .subscribe({}, _errorLiveData::setValue)
        )
    }

    fun getUser(uid: String) {
        disposables.add(authInteractor.getUser(uid)
            .observeOn(Schedulers.io()).subscribeOn(Schedulers.io())
            .doOnSubscribe { _progressLiveData.postValue(true) }
            .doAfterTerminate { _progressLiveData.postValue(false) }
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.ui())
            .subscribe(_userLiveData::setValue, _errorLiveData::setValue)
        )
    }

    /**
     * Method clears disposables.
     */
    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
        disposables.clear()
    }

    companion object {
        private const val TAG = "AuthViewModel"
    }
}

/**
 * Фабрика для ViewModel [ContentViewModel]
 */
class AuthViewModelFactory(
    private val schedulersProvider: SchedulersProvider,
    private val authInteractor: AuthInteractor
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AuthViewModel(
            schedulersProvider,
            authInteractor
        ) as T
    }
}