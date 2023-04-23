package ru.hse.pe.presentation.auth.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import io.mockk.*
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import ru.hse.pe.domain.interactor.AuthInteractor
import ru.hse.pe.domain.model.UserEntity
import ru.hse.pe.utils.scheduler.SchedulersProvider
import ru.hse.pe.utils.scheduler.SchedulersProviderImplStub
import java.io.IOException

@RunWith(MockitoJUnitRunner::class)
internal class AuthViewModelTest {
    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    lateinit var viewModel: AuthViewModel
    lateinit var authInteractor: AuthInteractor
    lateinit var schedulers: SchedulersProvider
    private val exception = IOException("")

    var progressObserver: Observer<Boolean> = mockk()
    var errorObserver: Observer<Throwable> = mockk()

    @Before
    fun setUp() {
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }
        authInteractor = mockk()
        schedulers = SchedulersProviderImplStub()

        every { progressObserver.onChanged(any()) } just Runs
        every { errorObserver.onChanged(any()) } just Runs


        viewModel = AuthViewModel(schedulers, authInteractor)
        viewModel.errorLiveData.observeForever(errorObserver)
        viewModel.progressLiveData.observeForever(progressObserver)
    }

    @Test
    fun `getUser is success`() {
        val observer: Observer<UserEntity> = mockk()
        val user = mockk<UserEntity>(relaxed = true)
        viewModel.userLiveData.observeForever(observer)
        every { authInteractor.getUser(UID) } returns Single.just(user)

        viewModel.getUser(UID)

        verifySequence {
            progressObserver.onChanged(true)
            observer.onChanged(user)
            progressObserver.onChanged(false)
        }
        verify(exactly = 1) { authInteractor.getUser(UID) }
        verify { errorObserver wasNot Called }
    }

    @Test
    fun `getUser is error`() {
        every { authInteractor.getUser(UID) } returns Single.error(exception)

        viewModel.getUser(UID)

        verifySequence {
            progressObserver.onChanged(true)
            errorObserver.onChanged(exception)
            progressObserver.onChanged(false)
        }
        verify(exactly = 1) { authInteractor.getUser(UID) }
    }

    @Test
    fun `addUser is error`() {
        val user = mockk<UserEntity>(relaxed = true)
        every { authInteractor.addUser(user) } returns Completable.error(exception)

        viewModel.addUser(user)

        verifySequence {
            progressObserver.onChanged(true)
            errorObserver.onChanged(exception)
            progressObserver.onChanged(false)
        }
        verify(exactly = 1) { authInteractor.addUser(user) }
    }

    companion object {
        private const val UID = "UID"
    }
}