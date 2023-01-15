package ru.hse.pe.presentation.content.item

import android.view.View
import coil.load
import com.xwray.groupie.viewbinding.BindableItem
import ru.hse.pe.R
import ru.hse.pe.databinding.HolderBookBinding
import ru.hse.pe.domain.model.RecommendationEntity
import ru.hse.pe.utils.callback.ContentClickListener

class BookItem(
    private val book: RecommendationEntity,
    private val clickListener: ContentClickListener
) : BindableItem<HolderBookBinding>() {

    override fun getLayout() = R.layout.holder_book

    override fun initializeViewBinding(view: View): HolderBookBinding {
        return HolderBookBinding.bind(view)
    }

    override fun bind(binding: HolderBookBinding, position: Int) {
        binding.root.setOnClickListener { clickListener.onContentClick(book, position) }
        if (book.imageUrls.isNotEmpty()) {
            binding.image.load(book.imageUrls[0]) {
                crossfade(true)
                placeholder(R.drawable.placeholder_book)
                error(R.drawable.placeholder_book)
            }
        } else {
            binding.image.load(R.drawable.placeholder_book) {
                crossfade(true)
            }
        }
    }
}
