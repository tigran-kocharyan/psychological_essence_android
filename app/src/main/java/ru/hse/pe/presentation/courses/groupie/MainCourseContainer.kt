package ru.hse.pe.presentation.courses.groupie

import android.view.View
import com.xwray.groupie.GroupieAdapter
import com.xwray.groupie.viewbinding.BindableItem
import ru.hse.pe.R
import ru.hse.pe.databinding.MainCourseContainerBinding


class MainCourseContainer(
    private val itemsAllCourses: List<BindableItem<*>>,
    private val itemsNewCourses: List<BindableItem<*>>,
    private val itemsSpecCourses: List<BindableItem<*>>,
    ) : BindableItem<MainCourseContainerBinding>() {

    override fun bind(binding: MainCourseContainerBinding, position: Int) {
        binding.rcAllCourses.adapter = GroupieAdapter().apply { addAll(itemsAllCourses) }
        binding.rcNewCourses.adapter = GroupieAdapter().apply { addAll(itemsNewCourses) }
        binding.rcSpecCourses.adapter = GroupieAdapter().apply { addAll(itemsSpecCourses) }
    }

    override fun getLayout() = R.layout.main_course_container

    override fun initializeViewBinding(view: View): MainCourseContainerBinding {
        return MainCourseContainerBinding.bind(view)
    }
}