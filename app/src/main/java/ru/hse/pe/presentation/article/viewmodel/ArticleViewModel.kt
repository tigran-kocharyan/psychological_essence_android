package ru.hse.pe.presentation.article.viewmodel

import android.text.TextUtils
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import ru.hse.pe.data.model.Article
import ru.hse.pe.utils.scheduler.SchedulersProvider
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.URL
import java.net.URLConnection


/**
 * ViewModel for Profile related actions.
 */
class ArticleViewModel(
    private val schedulers: SchedulersProvider
) : ViewModel() {

    private val progressLiveData = MutableLiveData<Boolean>()
    private val markdownLiveData = MutableLiveData<String>()
    private val articleLiveData = MutableLiveData<Article>()
    private val articlesLiveData = MutableLiveData<ArrayList<Article>>()
    private val errorLiveData = MutableLiveData<Throwable>()
    private val disposables = CompositeDisposable()

    private val database = FirebaseDatabase.getInstance()
    private var article = database.getReference(FB_ARTICLE_ABUSE)

    /**
     * Скачать статью из БД
     */
    fun getArticlesFromFB() {
        progressLiveData.postValue(true)
        database.getReference(FB_ARTICLES).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val articles = arrayListOf<Article>()
                for (ds in dataSnapshot.children) {
                    ds.getValue(Article::class.java)?.let {
                        articles.add(it)
                    }
                }
                Log.d(TAG, "onDataChange: ${articles.toString()}")
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    /**
     * Скачать статью из БД
     */
    fun getArticleFromFB() {
        progressLiveData.postValue(true)
        article.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                articleLiveData.postValue(dataSnapshot.getValue(Article::class.java))
                progressLiveData.postValue(false)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                errorLiveData.postValue(databaseError.toException())
                progressLiveData.postValue(false)
            }
        })
    }

    /**
     * Скачать ссылку на текст
     *
     * @param urlString ссылка на markdown
     */
    fun getMarkdownText(urlString: String) {
        disposables.add(getMarkdown(urlString)
            .doOnSubscribe { progressLiveData.postValue(true) }
            .doAfterTerminate { progressLiveData.postValue(false) }
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.ui())
            .subscribe(markdownLiveData::setValue, errorLiveData::setValue)
        )
    }

    private fun getMarkdown(urlString: String): Single<String> = Single.fromCallable {
        val feedUrl: URLConnection
        val placeAddress: MutableList<String> = ArrayList()
        try {
            feedUrl = URL(urlString).openConnection()
            val stream: InputStream = feedUrl.getInputStream()
            val reader = BufferedReader(InputStreamReader(stream, "UTF-8"))
            var line: String? = null
            while (reader.readLine().also { line = it } != null) {
                placeAddress.add(line ?: "")
            }
            stream.close()
            return@fromCallable TextUtils.join("\n", placeAddress)
                .replace(regex = Regex("^[^#]+\n#.+\n"), "")
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return@fromCallable ""
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
    fun getMarkdownLiveData(): LiveData<String> =
        markdownLiveData

    /**
     * @return LiveData<Boolean> for error display
     */
    fun getErrorLiveData(): LiveData<Throwable> =
        errorLiveData

    /**
     * @return LiveData<Boolean> for error display
     */
    fun getArticleLiveData(): LiveData<Article> =
        articleLiveData


    companion object {
        private const val TAG = "ArticleViewModel"
        const val FB_ARTICLE_ABUSE = "Articles/AbuseRelationship"
        const val FB_ARTICLES = "Articles"
    }
}