package ru.hse.pe.presentation.content.type.courses.lesson

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.hse.pe.R
import ru.hse.pe.databinding.HolderLessonBinding

class LessonAdapter : RecyclerView.Adapter<LessonAdapter.BackgroundHolder>() {
    val lessonsList = ArrayList<Lesson>()

    class BackgroundHolder(item: View) : RecyclerView.ViewHolder(item) {
        val binding = HolderLessonBinding.bind(item)

        fun bind(lesson: Lesson) = with(binding) {
            binding.titleLesson.text = lesson.title
            binding.descriptionLesson.text = lesson.desc

            var isOpen = true
            binding.openDescription.setOnClickListener {
                openLesson(isOpen)
                isOpen = !isOpen
            }

            binding.titleLesson.setOnClickListener {
                openLesson(isOpen)
                isOpen = !isOpen
            }
        }

        private fun openLesson(isOpen: Boolean) {
            if (isOpen) {
                binding.openDescription.rotation = 180F
                binding.descriptionLesson.maxLines = 100
            } else {
                binding.openDescription.rotation = 0F
                binding.descriptionLesson.maxLines = 0
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BackgroundHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.holder_lesson, parent, false)
        return BackgroundHolder(view)
    }

    override fun onBindViewHolder(holder: BackgroundHolder, position: Int) {
        holder.bind(lessonsList[position])
    }

    override fun getItemCount(): Int {
        return lessonsList.size
    }

    fun addLesson(lesson: Lesson) {
        lessonsList.add(lesson)
        notifyDataSetChanged()
    }
}
