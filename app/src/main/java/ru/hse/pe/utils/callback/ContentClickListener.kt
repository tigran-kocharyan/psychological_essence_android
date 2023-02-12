package ru.hse.pe.utils.callback

import ru.hse.pe.domain.model.ContentEntity

/**
 * Interface to interact with onClick event for RecyclerView.
 */
interface ContentClickListener {
    fun onContentClick(content: ContentEntity, position: Int)
}