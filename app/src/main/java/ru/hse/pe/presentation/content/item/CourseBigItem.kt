package ru.hse.pe.presentation.content.item

import android.view.View
import coil.load
import coil.transform.RoundedCornersTransformation
import com.xwray.groupie.viewbinding.BindableItem
import ru.hse.pe.R
import ru.hse.pe.databinding.HolderBigCourseBinding
import ru.hse.pe.domain.model.CourseEntity
import ru.hse.pe.utils.callback.ContentClickListener

class CourseBigItem(
    private val course: CourseEntity,
    private val clickListener: ContentClickListener,
) : BindableItem<HolderBigCourseBinding>(){

    override fun bind(binding: HolderBigCourseBinding, position: Int) {
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

        binding.root.setOnClickListener { clickListener.onContentClick(course, position) }
    }

    override fun getLayout() = R.layout.holder_big_course

    override fun initializeViewBinding(view: View): HolderBigCourseBinding {
        return HolderBigCourseBinding.bind(view)
    }
}