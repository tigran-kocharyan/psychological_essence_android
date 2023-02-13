package ru.hse.pe.presentation.content.item

import android.view.View
import coil.load
import coil.transform.RoundedCornersTransformation
import com.xwray.groupie.viewbinding.BindableItem
import ru.hse.pe.R
import ru.hse.pe.databinding.HolderCourseBinding
import ru.hse.pe.domain.model.CourseEntity
import ru.hse.pe.utils.callback.ContentClickListener

class CourseItem(
    private val course: CourseEntity,
    private val clickListener: ContentClickListener,
) : BindableItem<HolderCourseBinding>(){

    override fun bind(binding: HolderCourseBinding, position: Int) {
        binding.name.text = course.name

        if (course.imageUrl != null && course.imageUrl.isNotBlank()) {
            binding.image.load(course.imageUrl) {
                crossfade(true)
                placeholder(R.drawable.ic_launcher)
                error(R.drawable.ic_launcher)
                transformations(RoundedCornersTransformation(10f))
            }
        } else {
            binding.image.load(R.drawable.placeholder_article) {
                crossfade(true)
                transformations(RoundedCornersTransformation(10f))
            }
        }
    }

    override fun getLayout() = R.layout.holder_course

    override fun initializeViewBinding(view: View): HolderCourseBinding {
        return HolderCourseBinding.bind(view)
    }
}