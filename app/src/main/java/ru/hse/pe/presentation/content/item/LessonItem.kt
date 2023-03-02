package ru.hse.pe.presentation.content.item
import android.view.View
import com.xwray.groupie.viewbinding.BindableItem
import ru.hse.pe.R
import ru.hse.pe.databinding.HolderLessonBinding
import ru.hse.pe.domain.model.CourseEntity
import ru.hse.pe.domain.model.LessonEntity
import ru.hse.pe.utils.callback.ContentClickListener

class LessonItem(
    private val lesson: LessonEntity,
    private val clickListener: ContentClickListener,
) : BindableItem<HolderLessonBinding>(){
    override fun bind(binding: HolderLessonBinding, position: Int) {
        binding.title.text = lesson.name
        binding.image.rotation = -90f
        binding.root.setOnClickListener { clickListener.onContentClick(lesson, position) }
    }

    override fun getLayout() = R.layout.holder_lesson

    override fun initializeViewBinding(view: View): HolderLessonBinding {
        return HolderLessonBinding.bind(view)
    }
}