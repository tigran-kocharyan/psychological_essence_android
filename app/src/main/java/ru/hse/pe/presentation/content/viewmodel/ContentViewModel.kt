package ru.hse.pe.presentation.content.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import ru.hse.pe.domain.interactor.ContentInteractor
import ru.hse.pe.domain.model.ArticleEntity
import ru.hse.pe.domain.model.FactEntity
import ru.hse.pe.domain.model.QuizEntity
import ru.hse.pe.domain.model.RecommendationEntity
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
    private val factsLiveData = MutableLiveData<List<FactEntity>>()
    private val errorLiveData = MutableLiveData<Throwable>()
    private val disposables = CompositeDisposable()


    /**
     * Скачать статьи из БД
     */
    fun getArticles() {
        disposables.add(contentInteractor.getArticles()
            .observeOn(Schedulers.io()).subscribeOn(Schedulers.io())
            .doOnSubscribe { progressLiveData.postValue(true) }
            .doAfterTerminate { progressLiveData.postValue(false) }
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.ui())
            .subscribe(articlesLiveData::setValue, errorLiveData::setValue)
        )
    }

    /**
     * Скачать статьи из БД
     */
    fun getQuizzes() {
        disposables.add(contentInteractor.getQuizzes()
            .observeOn(Schedulers.io()).subscribeOn(Schedulers.io())
            .doOnSubscribe { progressLiveData.postValue(true) }
            .doAfterTerminate { progressLiveData.postValue(false) }
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.ui())
            .subscribe(quizzesLiveData::setValue, errorLiveData::setValue)
        )
    }

    /**
     * Скачать статьи из БД
     */
    fun getRecommendations() {
        disposables.add(contentInteractor.getRecommendations()
            .observeOn(Schedulers.io()).subscribeOn(Schedulers.io())
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
            .observeOn(Schedulers.io()).subscribeOn(Schedulers.io())
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

    fun getRecommendationsLiveData(): MutableLiveData<List<RecommendationEntity>> =
        recommendationsLiveData

    fun getFactsLiveData(): MutableLiveData<List<FactEntity>> =
        factsLiveData

    companion object {
        private const val TAG = "ContentViewModel"
    }
}