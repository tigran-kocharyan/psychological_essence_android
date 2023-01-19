package ru.hse.pe.presentation.courses.groupie.courseBig

import android.view.View
import com.xwray.groupie.viewbinding.BindableItem
import kotlinx.android.synthetic.main.course_big_item.view.*
import ru.hse.pe.R
import ru.hse.pe.databinding.CourseBigItemBinding

class CourseBigItem(
    private val title: String,
    private val imageId: Int,
) : BindableItem<CourseBigItemBinding>(){

    override fun bind(viewHolder: CourseBigItemBinding, position: Int) {
        viewHolder.root.titleBigCourse.text = title
        viewHolder.root.imgBigCourse.setImageResource(imageId)
    }

    override fun getLayout() = R.layout.course_big_item

    override fun initializeViewBinding(view: View): CourseBigItemBinding {
        return CourseBigItemBinding.bind(view)
    }
}