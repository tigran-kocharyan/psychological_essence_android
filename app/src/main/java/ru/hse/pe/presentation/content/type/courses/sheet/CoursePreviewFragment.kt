package ru.hse.pe.presentation.content.type.courses.sheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import coil.transform.RoundedCornersTransformation
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ru.hse.pe.R
import ru.hse.pe.SharedViewModel
import ru.hse.pe.databinding.BottomSheetCourseBinding
import ru.hse.pe.presentation.MainActivity
import ru.hse.pe.presentation.content.type.courses.lesson.LessonAdapter
import ru.hse.pe.presentation.content.type.courses.lesson.Lesson

class CoursePreviewFragment : BottomSheetDialogFragment(){
    private lateinit var binding: BottomSheetCourseBinding
    private val adapter = LessonAdapter()
    private val sharedViewModel: SharedViewModel by activityViewModels()

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
        (activity as MainActivity).isBottomNavVisible(false)

        setContent()
    }

    private fun setContent() {
        val course = sharedViewModel.course.value ?: return

        binding.apply {
            if (course.name?.length?.compareTo(20)!! > 0) {
                name.text = course.name.substring(0, 20) + "..."
            } else {
                name.text = course.name
            }

            if (course.imageUrl != null &&
                course.imageUrl.isNotBlank()) {
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
            countLessons.text = course.lessons.size.toString() + " модулей"
            description.text = course.description

            listLessons.layoutManager = LinearLayoutManager(binding.root.context, LinearLayoutManager.VERTICAL, false)
            listLessons.adapter = adapter

            for(lesson in course.lessons){
                adapter.addLesson(Lesson(
                    lesson.name.toString(),
                    lesson.description.toString()
                ))
            }

            close.setOnClickListener {
                dismiss()
            }
        }
    }

    companion object {
        const val TAG = "CoursePreviewFragment"

        /**
         * Получение объекта [CoursePreviewFragment]
         */
        fun newInstance() = CoursePreviewFragment()
    }
}