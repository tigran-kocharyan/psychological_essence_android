package ru.hse.pe.presentation.content.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import ru.hse.pe.R
import ru.hse.pe.databinding.HolderArticleBinding
import ru.hse.pe.databinding.HolderSeriesBinding
import ru.hse.pe.domain.model.ArticleEntity
import ru.hse.pe.domain.model.ContentEntity
import ru.hse.pe.domain.model.RecommendationEntity
import ru.hse.pe.utils.callback.ContentClickListener

/**
 * Adapter to show articles in recyclerview
 */
class ContentAdapter(
    var contents: ArrayList<ContentEntity>,
    private var clickListener: ContentClickListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    fun updateData(contents: ArrayList<ContentEntity>) {
        this.contents.clear()
        this.contents.addAll(contents)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
            0 -> ArticleViewHolder(
                HolderArticleBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            1 -> SeriesViewHolder(
                HolderSeriesBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            else -> ArticleViewHolder(
                HolderArticleBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }

    override fun getItemViewType(position: Int) = when (contents[position]) {
        is ArticleEntity -> 0
        is RecommendationEntity -> 1
        else -> 0
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ArticleViewHolder -> holder.bind(contents[position] as ArticleEntity, clickListener)
            is SeriesViewHolder -> holder.bind(
                contents[position] as RecommendationEntity,
                clickListener
            )
        }

    }

    override fun getItemCount(): Int {
        return contents.size
    }
}

/**
 * Holder to show the events
 */
class ArticleViewHolder(private val binding: HolderArticleBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(article: ArticleEntity, clickListener: ContentClickListener) {
        itemView.setOnClickListener { clickListener.onContentClick(article, adapterPosition) }
        binding.image.load(article.imageUrl) {
            crossfade(true)
            placeholder(R.drawable.ic_launcher)
            error(R.drawable.ic_launcher)
            transformations(RoundedCornersTransformation(10f))
        }
        binding.title.text = article.title
        binding.likesCount.text = article.likes.toString()
        binding.viewsCount.text = article.views.toString()
    }
}

/**
 * Holder to show the events
 */
class SeriesViewHolder(private val binding: HolderSeriesBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(content: ContentEntity, clickListener: ContentClickListener) {
        if (content is RecommendationEntity) {
            val context = itemView.context
            itemView.setOnClickListener { clickListener.onContentClick(content, adapterPosition) }
            if (content.imageUrls.isNotEmpty()) {
                binding.image.load(content.imageUrls[0]) {
                    crossfade(true)
                    placeholder(R.drawable.series_poster)
                    error(R.drawable.ic_launcher)
                    transformations(RoundedCornersTransformation(5f))
                }
            } else {
                binding.image.load(R.drawable.series_poster) {
                    transformations(RoundedCornersTransformation(5f))
                }
            }
            binding.title.text = content.title
            binding.date.text = context?.getString(R.string.date_recommendation, content.year) ?: ""
            binding.category.text =
                context?.getString(R.string.category_recommendation, content.category) ?: ""
            binding.description.text = content.content
        }
    }
}