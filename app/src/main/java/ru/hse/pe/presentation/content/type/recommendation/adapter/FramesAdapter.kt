package ru.hse.pe.presentation.content.type.recommendation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import ru.hse.pe.R


/**
 * Adapter to show events in recyclerview
 */
class FramesAdapter(
    var imageUrls: ArrayList<String>,
) : RecyclerView.Adapter<FrameViewHolder>() {

    fun updateData(imageUrls: List<String>) {
        this.imageUrls.clear()
        this.imageUrls.addAll(imageUrls)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FrameViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return FrameViewHolder(inflater.inflate(R.layout.holder_frame, parent, false))
    }

    override fun onBindViewHolder(holder: FrameViewHolder, position: Int) {
        holder.bind(imageUrls[position])
    }

    override fun getItemCount(): Int {
        return imageUrls.size
    }
}

/**
 * Holder to show the events
 */
class FrameViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val imageView: ImageView = itemView.findViewById(R.id.image)

    fun bind(url: String) {
        imageView.load(url) {
            placeholder(R.drawable.placeholder_article)
            error(R.drawable.placeholder_article)
            crossfade(true)
            transformations(RoundedCornersTransformation(15f))
        }
    }
}