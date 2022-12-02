//package ru.hse.pe.presentation.article.adapter
//
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.ImageView
//import android.widget.TextView
//import androidx.recyclerview.widget.RecyclerView
//import com.bumptech.glide.Glide
//import nl.totowka.bridge.R
//import nl.totowka.bridge.domain.model.EventEntity
//import nl.totowka.bridge.utils.Common.toCoolString
//import nl.totowka.bridge.utils.callback.EventClickListener
//import ru.hse.pe.domain.model.Article
//
//
///**
// * Adapter to show events in recyclerview
// */
//class EventsAdapter(
//    var events: ArrayList<Article>
//) : RecyclerView.Adapter<EventViewHolder>() {
//
//    fun updateData(events: ArrayList<Article>) {
//        this.events.clear()
//        this.events.addAll(events)
//        notifyDataSetChanged()
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
//        binding = HolderUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//        return EventViewHolder(inflater.inflate(R.layout.holder_event, parent, false))
//    }
//
//    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
//        holder.bind(events[position])
//    }
//
//    override fun getItemCount(): Int {
//        return events.size
//    }
//}
//
///**
// * Holder to show the events
// */
//class EventViewHolder(private val binding: HolderUserBinding) : RecyclerView.ViewHolder(itemView) {
//
//    fun bind(event: Article) {
//
//    }
//}