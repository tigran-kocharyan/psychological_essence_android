package ru.hse.pe

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.hse.pe.domain.model.ArticleEntity

class SharedViewModel : ViewModel() {
    private var _article = MutableLiveData<ArticleEntity?>(null)

    var article: LiveData<ArticleEntity?> = _article

    fun setArticle(article: ArticleEntity) {
        _article.value = article
    }
}