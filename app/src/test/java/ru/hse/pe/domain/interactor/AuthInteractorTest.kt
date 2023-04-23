package ru.hse.pe.domain.interactor

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Completable
import io.reactivex.Single
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import ru.hse.pe.data.api.ApiService
import ru.hse.pe.domain.model.UserEntity

internal class AuthInteractorTest {
    lateinit var apiServive: ApiService
    lateinit var authInteractor: AuthInteractor
    private val userEntityStub = mockk<UserEntity>().apply {
        every { uid } returns UID
    }

    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        apiServive = mockk(relaxed = true)
        authInteractor = AuthInteractor(apiServive)
    }

    @Test
    fun `addUser is successful`() {
        // Arrange
        every { apiServive.addUser(userEntityStub) } returns Completable.complete()

        // Assert
        authInteractor.addUser(userEntityStub).test().assertComplete()
        verify(exactly = 1) { apiServive.addUser(userEntityStub) }
    }

    @Test
    fun `getUser is successful`() {
        // Arrange
        every { apiServive.getUser(UID) } returns Single.just(userEntityStub)

        // Assert
        val result = authInteractor.getUser(UID).blockingGet()
        Truth.assertThat(result).isEqualTo(userEntityStub)
        verify(exactly = 1) { apiServive.getUser(UID) }
    }

    companion object {
        private const val UID = "UID"
    }
}