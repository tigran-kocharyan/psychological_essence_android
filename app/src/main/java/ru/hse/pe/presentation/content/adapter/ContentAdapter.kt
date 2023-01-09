package ru.hse.pe.presentation.content.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.holder_article.view.*
import ru.hse.pe.R
import ru.hse.pe.databinding.HolderArticleBinding
import ru.hse.pe.domain.model.ArticleEntity
import ru.hse.pe.utils.callback.ContentClickListener

/**
 * Adapter to show articles in recyclerview
 */
class ContentAdapter(
    var articles: ArrayList<ArticleEntity>,
    private var clickListener: ContentClickListener
) : RecyclerView.Adapter<ArticleViewHolder>() {

    fun updateData(articles: ArrayList<ArticleEntity>) {
        this.articles.clear()
        this.articles.addAll(articles)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        return ArticleViewHolder(
            HolderArticleBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        holder.bind(articles[position], clickListener)
    }

    override fun getItemCount(): Int {
        return articles.size
    }
}

/**
 * Holder to show the events
 */
class ArticleViewHolder(private val binding: HolderArticleBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(article: ArticleEntity, clickListener: ContentClickListener) {
        itemView.setOnClickListener { clickListener.onArticleClick(article, adapterPosition) }
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