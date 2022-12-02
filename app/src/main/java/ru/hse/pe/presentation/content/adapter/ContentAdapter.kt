package nl.totowka.bridge.presentation.events.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.hse.pe.R
import ru.hse.pe.domain.model.ArticleEntity
import ru.hse.pe.utils.callback.ContentClickListener


/**
 * Adapter to show events in recyclerview
 */
class ContentAdapter(
    var articles: ArrayList<ArticleEntity>,
    private var clickListener: ContentClickListener
) : RecyclerView.Adapter<EventViewHolder>() {

    fun updateData(articles: ArrayList<ArticleEntity>) {
        this.articles.clear()
        this.articles.addAll(articles)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return EventViewHolder(inflater.inflate(R.layout.holder_article, parent, false))
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        holder.bind(articles[position], clickListener)
    }

    override fun getItemCount(): Int {
        return articles.size
    }
}

/**
 * Holder to show the events
 */
class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val imageView: ImageView = itemView.findViewById(R.id.image)
    private val titleTextView: TextView = itemView.findViewById(R.id.title)

    fun bind(article: ArticleEntity, clickListener: ContentClickListener) {
        val context = itemView.context
        itemView.setOnClickListener { clickListener.onArticleClick(article, adapterPosition) }
        Glide.with(context)
            .load(article.imageUrl)
            .placeholder(R.drawable.ic_launcher)
            .error(R.drawable.ic_launcher)
            .circleCrop()
            .into(imageView)
        titleTextView.text = article.title
    }
}