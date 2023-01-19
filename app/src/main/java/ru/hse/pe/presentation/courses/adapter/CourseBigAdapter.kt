package ru.hse.pe.presentation.courses.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.hse.pe.R
import ru.hse.pe.databinding.CourseBigItemBinding
import ru.hse.pe.presentation.courses.Course

class CourseBigAdapter: RecyclerView.Adapter<CourseBigAdapter.BackgroundHolder>() {
    val courseList =  ArrayList<Course>()

    class BackgroundHolder(item: View):RecyclerView.ViewHolder(item) {
        val binding = CourseBigItemBinding.bind(item)

        fun bind(cs: Course) = with(binding){
            imgBigCourse.setImageResource(cs.img)
            titleBigCourse.text = cs.title
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BackgroundHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.course_big_item, parent, false)
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