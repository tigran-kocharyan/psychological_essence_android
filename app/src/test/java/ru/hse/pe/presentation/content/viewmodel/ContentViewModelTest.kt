package ru.hse.pe.presentation.content.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import io.mockk.*
import io.reactivex.Single
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import ru.hse.pe.domain.interactor.ContentInteractor
import ru.hse.pe.domain.model.ArticleEntity
import ru.hse.pe.domain.model.CourseEntity
import ru.hse.pe.domain.model.FactEntity
import ru.hse.pe.domain.model.RecommendationEntity
import ru.hse.pe.utils.scheduler.SchedulersProvider
import ru.hse.pe.utils.scheduler.SchedulersProviderImplStub
import java.io.IOException

@RunWith(MockitoJUnitRunner::class)
internal class ContentViewModelTest {
    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    lateinit var viewModel: ContentViewModel
    lateinit var contentInteractor: ContentInteractor
    lateinit var schedulers: SchedulersProvider
    private val exception = IOException("")

    var progressObserver: Observer<Boolean> = mockk()
    var errorObserver: Observer<Throwable> = mockk()

    @Before
    fun setUp() {
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }
        contentInteractor = mockk()
        schedulers = SchedulersProviderImplStub()

        every { progressObserver.onChanged(any()) } just Runs
        every { errorObserver.onChanged(any()) } just Runs

        viewModel = ContentViewModel(schedulers, contentInteractor)
        viewModel.getErrorLiveData().observeForever(errorObserver)
        viewModel.getProgressLiveData().observeForever(progressObserver)
    }

    @Test
    fun `getArticles is success`() {
        val articlesObserver: Observer<List<ArticleEntity>> = mockk()
        val list = emptyList<ArticleEntity>()
        viewModel.getArticlesLiveData().observeForever(articlesObserver)
        every { contentInteractor.getArticles() } returns Single.just(list)
        every { articlesObserver.onChanged(any()) } just Runs


        viewModel.getArticles()

        verifySequence {
            progressObserver.onChanged(true)
            articlesObserver.onChanged(list)
            progressObserver.onChanged(false)
        }
        verify(exactly = 1) { contentInteractor.getArticles() }
        verify { errorObserver wasNot Called }
    }

    @Test
    fun `getArticles is error`() {
        every { contentInteractor.getArticles() } returns Single.error(exception)

        viewModel.getArticles()

        verifySequence {
            progressObserver.onChanged(true)
            errorObserver.onChanged(exception)
            progressObserver.onChanged(false)
        }
        verify(exactly = 1) { contentInteractor.getArticles() }
    }

    @Test
    fun `getFacts is success`() {
        val factsObserver: Observer<List<FactEntity>> = mockk()
        val list = emptyList<FactEntity>()
        viewModel.getFactsLiveData().observeForever(factsObserver)
        every { contentInteractor.getFacts() } returns Single.just(list)
        every { factsObserver.onChanged(any()) } just Runs


        viewModel.getFacts()

        verifySequence {
            progressObserver.onChanged(true)
            factsObserver.onChanged(list)
            progressObserver.onChanged(false)
        }
        verify(exactly = 1) { contentInteractor.getFacts() }
        verify { errorObserver wasNot Called }
    }

    @Test
    fun `getFacts is error`() {
        every { contentInteractor.getFacts() } returns Single.error(exception)

        viewModel.getFacts()

        verifySequence {
            progressObserver.onChanged(true)
            errorObserver.onChanged(exception)
            progressObserver.onChanged(false)
        }
        verify(exactly = 1) { contentInteractor.getFacts() }
    }

    @Test
    fun `getRecommendations is success`() {
        val observer: Observer<List<RecommendationEntity>> = mockk()
        val list = emptyList<RecommendationEntity>()
        viewModel.getRecommendationsLiveData().observeForever(observer)
        every { contentInteractor.getRecommendations() } returns Single.just(list)
        every { observer.onChanged(any()) } just Runs


        viewModel.getRecommendations()

        verifySequence {
            progressObserver.onChanged(true)
            observer.onChanged(list)
            progressObserver.onChanged(false)
        }
        verify(exactly = 1) { contentInteractor.getRecommendations() }
        verify { errorObserver wasNot Called }
    }

    @Test
    fun `getRecommendations is error`() {
        every { contentInteractor.getRecommendations() } returns Single.error(exception)

        viewModel.getRecommendations()

        verifySequence {
            progressObserver.onChanged(true)
            errorObserver.onChanged(exception)
            progressObserver.onChanged(false)
        }
        verify(exactly = 1) { contentInteractor.getRecommendations() }
    }

    @Test
    fun `getCourses is success`() {
        val observer: Observer<List<CourseEntity>> = mockk()
        val list = emptyList<CourseEntity>()
        viewModel.getCourseLiveData().observeForever(observer)
        every { contentInteractor.getCourses() } returns Single.just(list)
        every { observer.onChanged(any()) } just Runs


        viewModel.getCourses()

        verifySequence {
            progressObserver.onChanged(true)
            observer.onChanged(list)
            progressObserver.onChanged(false)
        }
        verify(exactly = 1) { contentInteractor.getCourses() }
        verify { errorObserver wasNot Called }
    }

    @Test
    fun `getCourses is error`() {
        every { contentInteractor.getCourses() } returns Single.error(exception)

        viewModel.getCourses()

        verifySequence {
            progressObserver.onChanged(true)
            errorObserver.onChanged(exception)
            progressObserver.onChanged(false)
        }
        verify(exactly = 1) { contentInteractor.getCourses() }
    }

    @Test
    fun `getTechniques is success`() {
        val observer: Observer<List<ArticleEntity>> = mockk()
        val list = emptyList<ArticleEntity>()
        viewModel.getTechniquesLiveData().observeForever(observer)
        every { contentInteractor.getTechniques() } returns Single.just(list)
        every { observer.onChanged(any()) } just Runs


        viewModel.getTechniques()

        verifySequence {
            progressObserver.onChanged(true)
            observer.onChanged(list)
            progressObserver.onChanged(false)
        }
        verify(exactly = 1) { contentInteractor.getTechniques() }
        verify { errorObserver wasNot Called }
    }

    @Test
    fun `getTechniques is error`() {
        every { contentInteractor.getTechniques() } returns Single.error(exception)

        viewModel.getTechniques()

        verifySequence {
            progressObserver.onChanged(true)
            errorObserver.onChanged(exception)
            progressObserver.onChanged(false)
        }
        verify(exactly = 1) { contentInteractor.getTechniques() }
    }

    @Test
    fun `getSubscriptionUrl is success`() {
        val observer: Observer<String> = mockk()
        viewModel.getSubscriptionUrlLiveData().observeForever(observer)
        every { contentInteractor.getSubscriptionUrl(UID) } returns Single.just(URL)
        every { observer.onChanged(any()) } just Runs


        viewModel.getSubscriptionUrl(UID)

        verifySequence {
            progressObserver.onChanged(true)
            observer.onChanged(URL)
            progressObserver.onChanged(false)
        }
        verify(exactly = 1) { contentInteractor.getSubscriptionUrl(UID) }
        verify { errorObserver wasNot Called }
    }

    @Test
    fun `getSubscriptionUrl is error`() {
        every { contentInteractor.getSubscriptionUrl(UID) } returns Single.error(exception)

        viewModel.getSubscriptionUrl(UID)

        verifySequence {
            progressObserver.onChanged(true)
            errorObserver.onChanged(exception)
            progressObserver.onChanged(false)
        }
        verify(exactly = 1) { contentInteractor.getSubscriptionUrl(UID) }
    }

    companion object {
        private const val UID = "UID"
        private const val URL = "URL"
    }
}