package ru.hse.pe.presentation.courses.groupie.courseSmall

import android.view.View
import com.xwray.groupie.viewbinding.BindableItem
import kotlinx.android.synthetic.main.course_small_item.view.*
import ru.hse.pe.R
import ru.hse.pe.databinding.CourseSmallItemBinding

class CourseSmallItem(
    private val title: String,
    private val imageId: Int,
) : BindableItem<CourseSmallItemBinding>(){

    override fun bind(viewHolder: CourseSmallItemBinding, position: Int) {
        viewHolder.root.titleSmallCourse.text = title
        viewHolder.root.imgCourse.setImageResource(imageId)
    }

    override fun getLayout() = R.layout.course_small_item

    override fun initializeViewBinding(view: View): CourseSmallItemBinding {
        return CourseSmallItemBinding.bind(view)
    }

    override fun toString(): String {
        return "CourseSmallItem(title='$title', imageId=$imageId)"
    }
}