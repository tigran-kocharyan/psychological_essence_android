package ru.hse.pe.presentation.content.type.courses.sheet

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import coil.load
import coil.transform.RoundedCornersTransformation
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.xwray.groupie.GroupieAdapter
import com.xwray.groupie.viewbinding.BindableItem
import ru.hse.pe.R
import ru.hse.pe.SharedViewModel
import ru.hse.pe.databinding.BottomSheetCourseBinding
import ru.hse.pe.domain.model.ContentEntity
import ru.hse.pe.domain.model.CourseEntity
import ru.hse.pe.domain.model.LessonEntity
import ru.hse.pe.presentation.MainActivity
import ru.hse.pe.presentation.content.item.LessonItem
import ru.hse.pe.presentation.content.type.courses.lesson.LessonFragment
import ru.hse.pe.utils.callback.ContentClickListener
import ru.hse.pe.utils.container.VerticalContentContainer

class CoursePreviewFragment : BottomSheetDialogFragment() {
    private lateinit var binding: BottomSheetCourseBinding
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private lateinit var lessonList: MutableList<LessonEntity>

    private var clickListener = object : ContentClickListener {
        override fun onContentClick(content: ContentEntity, position: Int) {
            if (content is LessonEntity) {
                sharedViewModel.setLesson(content)
                dismiss()
                setCurrentFragment(
                    LessonFragment.newInstance(),
                    LessonFragment.TAG
                )
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BottomSheetCourseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setContent()
    }

    private fun setContent() {
        val course = sharedViewModel.course.value ?: return

        with(binding) {
            if (course.name?.length?.compareTo(20)!! > 0) {
                name.text = course.name.substring(0, 20) + "..."
            } else {
                name.text = course.name
            }

            if (course.imageUrl != null &&
                course.imageUrl.isNotBlank()
            ) {
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
            title.text = course.name
            countLessons.text = course.lessonsCount.toString() + " модулей"
            description.text = course.description

            val lessons = listOf(getLessons(course))
            listLessons.adapter = GroupieAdapter().apply { addAll(lessons) }

            start.setOnClickListener {
                clickListener.onContentClick(lessonList[0],0)
                setCurrentFragment(
                    LessonFragment.newInstance(),
                    LessonFragment.TAG
                )
                dismiss()
            }

            close.setOnClickListener {
                dismiss()
            }
        }
    }

    private fun getLessons(course: CourseEntity): BindableItem<*> {
        lessonList = mutableListOf()
        for (i in 0 until course.lessonsCount!!) {
            lessonList.add(
                LessonEntity(
                    course.lessonsIds?.get(i),
                    course.lessonsNames?.get(i),
                )
            )
        }
        return VerticalContentContainer(
            "Содержание курса",
            lessonList.map { LessonItem(it, clickListener) }
        )
    }

    private fun setCurrentFragment(fragment: Fragment, tag: String) =
        parentFragmentManager
            .beginTransaction()
            .setCustomAnimations(
                R.anim.slide_in,
                R.anim.fade_out,
                R.anim.pop_enter,
                R.anim.pop_exit
            )
            .add(R.id.fragment_container, fragment, tag)
            .addToBackStack(null)
            .commit()

    companion object {
        const val TAG = "CoursePreviewFragment"

        /**
         * Получение объекта [CoursePreviewFragment]
         */
        fun newInstance() = CoursePreviewFragment()
    }
}