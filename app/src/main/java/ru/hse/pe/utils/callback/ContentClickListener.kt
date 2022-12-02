package ru.hse.pe.utils.callback

import ru.hse.pe.domain.model.ArticleEntity

/**
 * Interface to interact with onClick event for RecyclerView.
 */
interface ContentClickListener {
    fun onArticleClick(article: ArticleEntity, position: Int)
}