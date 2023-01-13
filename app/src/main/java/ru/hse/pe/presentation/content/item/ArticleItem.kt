package ru.hse.pe.presentation.content.item

import android.view.View
import coil.load
import coil.transform.RoundedCornersTransformation
import com.xwray.groupie.viewbinding.BindableItem
import ru.hse.pe.R
import ru.hse.pe.databinding.HolderArticleBinding
import ru.hse.pe.domain.model.ArticleEntity
import ru.hse.pe.utils.callback.ContentClickListener

class ArticleItem(
    private val article: ArticleEntity,
    private val clickListener: ContentClickListener
) : BindableItem<HolderArticleBinding>() {

    override fun getLayout() = R.layout.holder_article

    override fun initializeViewBinding(view: View): HolderArticleBinding {
        return HolderArticleBinding.bind(view)
    }

    override fun bind(binding: HolderArticleBinding, position: Int) {
        binding.root.setOnClickListener { clickListener.onContentClick(article, position) }
        if (article.imageUrl != null && article.imageUrl.isNotBlank()) {
            binding.image.load(article.imageUrl) {
                crossfade(true)
                placeholder(R.drawable.ic_launcher)
                error(R.drawable.ic_launcher)
                transformations(RoundedCornersTransformation(10f))
            }
        } else {
            binding.image.load(R.drawable.placeholder_article) {
                crossfade(true)
                transformations(RoundedCornersTransformation(10f))
            }
        }
        binding.title.text = article.title
        binding.likesCount.text = article.likes.toString()
        binding.viewsCount.text = article.views.toString()
    }
}
