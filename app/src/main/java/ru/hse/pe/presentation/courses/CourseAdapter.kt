package ru.hse.pe.presentation.courses

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.hse.pe.R
import ru.hse.pe.databinding.CourseItemBinding

class CourseAdapter: RecyclerView.Adapter<CourseAdapter.BackgroundHolder>() {
    val courseList =  ArrayList<Course>()

    class BackgroundHolder(item: View):RecyclerView.ViewHolder(item) {
        val binding = CourseItemBinding.bind(item)

        fun bind(cs: Course) = with(binding){
            imgCourse.setImageResource(cs.img)
            titleCourse.text = cs.title
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BackgroundHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.course_item, parent, false)
        return BackgroundHolder(view)
    }

    override fun onBindViewHolder(holder: BackgroundHolder, position: Int) {
        holder.bind(courseList[position])
    }

    override fun getItemCount(): Int {
        return courseList.size
    }

    fun addCourse(cs: Course){
        courseList.add(cs)
        notifyDataSetChanged()
    }
}