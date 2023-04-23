package ru.hse.pe.domain.interactor

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Single
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import ru.hse.pe.data.api.ApiService
import ru.hse.pe.domain.model.*

internal class ContentInteractorTest {
    lateinit var apiServive: ApiService
    lateinit var contentInteractor: ContentInteractor
    private val articleEntityStub = mockk<ArticleEntity>().apply {
        every { id } returns ID
    }
    private val articlesListStub = listOf(articleEntityStub)

    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        apiServive = mockk(relaxed = true)
        contentInteractor = ContentInteractor(apiServive)
    }

    @Test
    fun `getArticles is successful`() {
        // Arrange
        every { apiServive.getArticles() } returns Single.just(articlesListStub)

        // Assert
        val result = contentInteractor.getArticles().blockingGet()
        Truth.assertThat(result).isEqualTo(articlesListStub)
        verify(exactly = 1) { apiServive.getArticles() }
    }

    @Test
    fun `getUser is successful`() {
        val expected = emptyList<FactEntity>()
        // Arrange
        every { apiServive.getFacts() } returns Single.just(expected)

        // Assert
        val result = contentInteractor.getFacts().blockingGet()
        Truth.assertThat(result).isEqualTo(expected)
        verify(exactly = 1) { apiServive.getFacts() }
    }

    @Test
    fun `getQuizzes is successful`() {
        val expected = emptyList<QuizEntity>()
        // Arrange
        every { apiServive.getQuizzes() } returns Single.just(expected)

        // Assert
        val result = contentInteractor.getQuizzes().blockingGet()
        Truth.assertThat(result).isEqualTo(expected)
        verify(exactly = 1) { apiServive.getQuizzes() }
    }

    @Test
    fun `getCourses is successful`() {
        val expected = emptyList<CourseEntity>()
        // Arrange
        every { apiServive.getCourses() } returns Single.just(expected)

        // Assert
        val result = contentInteractor.getCourses().blockingGet()
        Truth.assertThat(result).isEqualTo(expected)
        verify(exactly = 1) { apiServive.getCourses() }
    }

    @Test
    fun `getRecommendations is successful`() {
        val expected = emptyList<RecommendationEntity>()
        // Arrange
        every { apiServive.getRecommendations() } returns Single.just(expected)

        // Assert
        val result = contentInteractor.getRecommendations().blockingGet()
        Truth.assertThat(result).isEqualTo(expected)
        verify(exactly = 1) { apiServive.getRecommendations() }
    }

    @Test
    fun `getTechniques is successful`() {
        val expected = emptyList<ArticleEntity>()
        // Arrange
        every { apiServive.getTechniques() } returns Single.just(expected)

        // Assert
        val result = contentInteractor.getTechniques().blockingGet()
        Truth.assertThat(result).isEqualTo(expected)
        verify(exactly = 1) { apiServive.getTechniques() }
    }

    @Test
    fun `getSubscriptionUrl is successful`() {
        // Arrange
        every { apiServive.getSubscriptionUrl(UID) } returns Single.just(URL)

        // Assert
        val result = contentInteractor.getSubscriptionUrl(UID).blockingGet()
        Truth.assertThat(result).isEqualTo(URL)
        verify(exactly = 1) { apiServive.getSubscriptionUrl(UID) }
    }

    companion object {
        private const val UID = "UID"
        private const val URL = "URL"
        private const val ID = 0
    }
}