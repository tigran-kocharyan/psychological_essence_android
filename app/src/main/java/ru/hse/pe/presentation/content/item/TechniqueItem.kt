package ru.hse.pe.presentation.content.item

import android.view.View
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.xwray.groupie.viewbinding.BindableItem
import ru.hse.pe.R
import ru.hse.pe.databinding.HolderTechniqueBinding
import ru.hse.pe.domain.model.ArticleEntity
import ru.hse.pe.utils.callback.ContentClickListener

class TechniqueItem(
    private val article: ArticleEntity,
    private val clickListener: ContentClickListener
) : BindableItem<HolderTechniqueBinding>() {

    override fun getLayout() = R.layout.holder_technique

    override fun initializeViewBinding(view: View): HolderTechniqueBinding {
        return HolderTechniqueBinding.bind(view)
    }

    override fun bind(binding: HolderTechniqueBinding, position: Int) {
        binding.root.setOnClickListener { clickListener.onContentClick(article, position) }
        if (article.imageUrl != null && article.imageUrl.isNotBlank()) {
            Glide
                .with(binding.root)
                .load(article.imageUrl)
                .transform(CenterCrop(), RoundedCorners(10))
                .transition(withCrossFade())
                .error(R.drawable.placeholder_article)
                .into(binding.image)
        } else {
            Glide
                .with(binding.root)
                .load(R.drawable.placeholder_article)
                .transform(CenterCrop(), RoundedCorners(10))
                .transition(withCrossFade())
                .into(binding.image)
        }
        binding.title.text = article.title
        binding.category.text = article.category
        binding.lock.isVisible = article.requiresSubscription
    }
}
