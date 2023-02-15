package ru.hse.pe.presentation.content.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import ru.hse.pe.domain.interactor.ContentInteractor
import ru.hse.pe.domain.model.*
import ru.hse.pe.utils.scheduler.SchedulersProvider


/**
 * ViewModel for Profile related actions.
 */
class ContentViewModel(
    private val schedulers: SchedulersProvider,
    private val contentInteractor: ContentInteractor
) : ViewModel() {

    private val progressLiveData = MutableLiveData<Boolean>()
    private val recommendationsLiveData = MutableLiveData<List<RecommendationEntity>>()
    private val articlesLiveData = MutableLiveData<List<ArticleEntity>>()
    private val quizzesLiveData = MutableLiveData<List<QuizEntity>>()
    private val quizResultLiveData = MutableLiveData<QuizResultEntity>()
    private val courseLiveData = MutableLiveData<List<CourseEntity>>()
    private val factsLiveData = MutableLiveData<List<FactEntity>>()
    private val errorLiveData = MutableLiveData<Throwable>()

    private val disposables = CompositeDisposable()

    /**
     * Скачать статьи из БД
     */
    fun getArticles() {
        disposables.add(contentInteractor.getArticles()
            .observeOn(schedulers.io()).subscribeOn(schedulers.io())
            .doOnSubscribe { progressLiveData.postValue(true) }
            .doAfterTerminate { progressLiveData.postValue(false) }
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.ui())
            .subscribe(articlesLiveData::setValue, errorLiveData::setValue)
        )
    }

    /**
     * Скачать тесты из БД
     */
    fun getQuizzes() {
        disposables.add(contentInteractor.getQuizzes()
            .observeOn(schedulers.io()).subscribeOn(schedulers.io())
            .doOnSubscribe { progressLiveData.postValue(true) }
            .doAfterTerminate { progressLiveData.postValue(false) }
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.ui())
            .subscribe(quizzesLiveData::setValue, errorLiveData::setValue)
        )
    }

    /**
     * Получить результат прохождения теста для отображения
     */
    fun getQuizResult(answers: QuizAnswerEntity) {
        disposables.add(contentInteractor.getQuizResult(answers)
            .observeOn(schedulers.io()).subscribeOn(schedulers.io())
            .doOnSubscribe { progressLiveData.postValue(true) }
            .doAfterTerminate { progressLiveData.postValue(false) }
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.ui())
            .subscribe(quizResultLiveData::setValue, errorLiveData::setValue)
        )
    }

    /**
     * Скачать курсы из БД
     */
    fun getCourses() {
        disposables.add(contentInteractor.getCourses()
            .observeOn(schedulers.io()).subscribeOn(schedulers.io())
            .doOnSubscribe { progressLiveData.postValue(true) }
            .doAfterTerminate { progressLiveData.postValue(false) }
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.ui())
            .subscribe(courseLiveData::setValue, errorLiveData::setValue)
        )
    }

    /**
     * Скачать статьи из БД
     */
    fun getRecommendations() {
        disposables.add(contentInteractor.getRecommendations()
            .observeOn(schedulers.io()).subscribeOn(schedulers.io())
            .doOnSubscribe { progressLiveData.postValue(true) }
            .doAfterTerminate { progressLiveData.postValue(false) }
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.ui())
            .subscribe(recommendationsLiveData::setValue, errorLiveData::setValue)
        )
    }

    /**
     * Скачать факты из БД
     */
    fun getFacts() {
        disposables.add(contentInteractor.getFacts()
            .observeOn(schedulers.io()).subscribeOn(schedulers.io())
            .doOnSubscribe { progressLiveData.postValue(true) }
            .doAfterTerminate { progressLiveData.postValue(false) }
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.ui())
            .subscribe(factsLiveData::setValue, errorLiveData::setValue)
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

    /**
     * @return LiveData<Boolean> for progress display
     */
    fun getProgressLiveData(): LiveData<Boolean> =
        progressLiveData

    /**
     * @return LiveData<Boolean> for error display
     */
    fun getErrorLiveData(): LiveData<Throwable> =
        errorLiveData

    fun getArticlesLiveData(): MutableLiveData<List<ArticleEntity>> =
        articlesLiveData

    fun getQuizzesLiveData(): MutableLiveData<List<QuizEntity>> =
        quizzesLiveData

    fun getQuizResultLiveData(): MutableLiveData<QuizResultEntity> =
        quizResultLiveData

    fun getCourseLiveData(): MutableLiveData<List<CourseEntity>> =
        courseLiveData

    fun getRecommendationsLiveData(): MutableLiveData<List<RecommendationEntity>> =
        recommendationsLiveData

    fun getFactsLiveData(): MutableLiveData<List<FactEntity>> =
        factsLiveData

    companion object {
        private const val TAG = "ContentViewModel"
    }
}

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