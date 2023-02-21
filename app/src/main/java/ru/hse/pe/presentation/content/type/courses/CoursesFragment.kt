package ru.hse.pe.presentation.content.type.courses

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.xwray.groupie.GroupieAdapter
import com.xwray.groupie.viewbinding.BindableItem
import ru.hse.pe.App
import ru.hse.pe.R
import ru.hse.pe.SharedViewModel
import ru.hse.pe.databinding.FragmentCoursesBinding
import ru.hse.pe.domain.interactor.ContentInteractor
import ru.hse.pe.domain.model.ContentEntity
import ru.hse.pe.domain.model.CourseEntity
import ru.hse.pe.domain.model.QuizEntity
import ru.hse.pe.presentation.MainActivity
import ru.hse.pe.presentation.content.item.CourseBigItem
import ru.hse.pe.presentation.content.type.test.ui.TestsFragment
import ru.hse.pe.presentation.content.type.test.ui.sheet.TestPreviewFragment
import ru.hse.pe.presentation.content.viewmodel.ContentViewModel
import ru.hse.pe.presentation.content.viewmodel.ContentViewModelFactory
import ru.hse.pe.presentation.content.item.CourseItem
import ru.hse.pe.presentation.content.type.courses.sheet.CoursePreviewFragment
import ru.hse.pe.utils.callback.ContentClickListener
import ru.hse.pe.utils.container.HorizontalContentContainer
import ru.hse.pe.utils.container.VerticalContentContainer
import ru.hse.pe.utils.scheduler.SchedulersProvider
import javax.inject.Inject


class CoursesFragment : Fragment() {
    @Inject
    lateinit var interactor: ContentInteractor

    @Inject
    lateinit var schedulers: SchedulersProvider

    private lateinit var binding: FragmentCoursesBinding
    private val sharedViewModel: SharedViewModel by activityViewModels()

    private val viewModel: ContentViewModel by viewModels {
        ContentViewModelFactory(
            schedulers,
            interactor
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity?.applicationContext as App).getAppComponent().inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCoursesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeLiveData()
        viewModel.getCourses()
        (activity as MainActivity).isBottomNavVisible(false)
    }

    private var clickListener = object : ContentClickListener {
        override fun onContentClick(content: ContentEntity, position: Int) {
            if (content is CourseEntity) {
                sharedViewModel.setCourse(content)
                CoursePreviewFragment.newInstance().show(
                    (activity as AppCompatActivity).supportFragmentManager,
                    CoursePreviewFragment.TAG
                )
            }
        }
    }

    private fun showProgress(isVisible: Boolean) {
        Log.i(TestsFragment.TAG, "showProgress called with param = $isVisible")
        binding.progressbar.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    private fun showError(throwable: Throwable) {
        Log.d(TestsFragment.TAG, "showError() called with: throwable = $throwable")
        Snackbar.make(binding.root, throwable.toString(), BaseTransientBottomBar.LENGTH_SHORT)
            .show()
    }

    private fun showCourses(courses: List<CourseEntity>) {
        val listTests = listOf(getBigCoursesItems(courses), getCoursesItems(courses))
        binding.coursesList.adapter = GroupieAdapter().apply { addAll(listTests) }
    }

    // берем все курсы, которые будут в большой картинке
    private fun getBigCoursesItems(courses: List<CourseEntity>): BindableItem<*> {
        return HorizontalContentContainer(
            "Специально для вас",
            courses.map { CourseBigItem(it, clickListener) }
        )
    }

    // берем все курсы,
    private fun getCoursesItems(courses: List<CourseEntity>): BindableItem<*> {
        return VerticalContentContainer(
            getString(R.string.newCourses),
            courses.map { CourseItem(it, clickListener) }
        )
    }

    private fun observeLiveData() {
        viewModel.getErrorLiveData().observe(viewLifecycleOwner, this::showError)
        viewModel.getProgressLiveData().observe(viewLifecycleOwner, this::showProgress)
        viewModel.getCourseLiveData().observe(viewLifecycleOwner, this::showCourses)
    }

    companion object {
        const val TAG = "CoursesFragment"

        /**
         * Получение объекта [CoursesFragment]
         */
        fun newInstance() = CoursesFragment()
    }
}