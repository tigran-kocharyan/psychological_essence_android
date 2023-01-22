package ru.hse.pe.presentation.courses.groupie.courseSmall

import android.content.Intent
import android.view.View
import androidx.core.content.ContextCompat.startActivity
import com.xwray.groupie.GroupieAdapter
import com.xwray.groupie.viewbinding.BindableItem
import ru.hse.pe.R
import ru.hse.pe.databinding.CourseSmallBinding
import ru.hse.pe.presentation.courses.allCourses.AllCourses

class CourseSmall(
    private val name: String ?= null,
    private val seeAll: String ?= null,
    private val onClick: (url: String) -> Unit,
    private val items: List<BindableItem<*>>
) : BindableItem<CourseSmallBinding>() {

    override fun bind(binding: CourseSmallBinding, position: Int) {
        binding.name.text = name
        binding.seeAll.text = seeAll


        binding.rcSmallView.adapter = GroupieAdapter().apply { addAll(items) }

        binding.seeAll.setOnClickListener{
            val myIntent = Intent(binding.root.context, AllCourses::class.java)
            startActivity(binding.root.context, myIntent, null)
        }
    }

    override fun getLayout() = R.layout.course_small

    override fun initializeViewBinding(view: View): CourseSmallBinding {
        return CourseSmallBinding.bind(view)
    }

    override fun toString(): String {
        return "CourseSmall(name=$name, seeAll=$seeAll, onClick=$onClick, items=$items)"
    }
}