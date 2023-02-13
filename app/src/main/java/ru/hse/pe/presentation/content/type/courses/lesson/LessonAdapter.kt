package ru.hse.pe.presentation.content.type.courses.lesson

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.hse.pe.R
import ru.hse.pe.databinding.LessonItemBinding

class LessonAdapter: RecyclerView.Adapter<LessonAdapter.BackgroundHolder>() {

    val lessonsList =  ArrayList<LessonData>()


    class BackgroundHolder(item: View): RecyclerView.ViewHolder(item) {
        val binding = LessonItemBinding.bind(item)


        fun bind(lesson: LessonData) = with(binding){
            binding.titleLesson.text = lesson.title
            binding.descLesson.text = lesson.desc

            var isOpen = true
            binding.openDesc.setOnClickListener {
                openLesson(isOpen)
                isOpen = !isOpen
            }

            binding.titleLesson.setOnClickListener {
                openLesson(isOpen)
                isOpen = !isOpen
            }
        }

        private fun openLesson(isOpen: Boolean){
            if(isOpen){
                binding.openDesc.rotation = 180F
                binding.descLesson.maxLines = 100
            }else{
                binding.openDesc.rotation = 0F
                binding.descLesson.maxLines = 0
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BackgroundHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.lesson_item, parent, false)
        return BackgroundHolder(view)
    }

    override fun onBindViewHolder(holder: BackgroundHolder, position: Int) {
        holder.bind(lessonsList[position])
    }

    override fun getItemCount(): Int {
        return lessonsList.size
    }

    fun addLesson(lesson: LessonData){
        lessonsList.add(lesson)
        notifyDataSetChanged()
    }


}
