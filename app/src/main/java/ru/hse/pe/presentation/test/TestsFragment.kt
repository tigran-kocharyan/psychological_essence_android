package ru.hse.pe.presentation.test

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
import ru.hse.pe.databinding.FragmentTestsBinding
import ru.hse.pe.domain.interactor.ContentInteractor
import ru.hse.pe.domain.model.ContentEntity
import ru.hse.pe.domain.model.QuizEntity
import ru.hse.pe.presentation.MainActivity
import ru.hse.pe.presentation.content.viewmodel.ContentViewModel
import ru.hse.pe.presentation.content.viewmodel.ContentViewModelFactory
import ru.hse.pe.presentation.test.bottomSheetFragment.ActionTestBottom
import ru.hse.pe.presentation.test.groupie.SpecTestContainer
import ru.hse.pe.presentation.test.groupie.SpecTestItem
import ru.hse.pe.presentation.test.groupie.TestContainer
import ru.hse.pe.presentation.test.groupie.TestItem
import ru.hse.pe.utils.callback.ContentClickListener
import ru.hse.pe.utils.scheduler.SchedulersProvider
import javax.inject.Inject


class TestsFragment : Fragment() {
    @Inject
    lateinit var interactor: ContentInteractor

    @Inject
    lateinit var schedulers: SchedulersProvider

    private lateinit var binding: FragmentTestsBinding
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
        binding = FragmentTestsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeLiveData()
        viewModel.getQuizzes()
        (activity as MainActivity).isBottomNavVisible(false)
    }

    private var clickListener = object : ContentClickListener {
        override fun onContentClick(content: ContentEntity, position: Int) {
            if (content is QuizEntity) {
                sharedViewModel.setQuiz(content)
                ActionTestBottom.newInstance().show(
                    (activity as AppCompatActivity).supportFragmentManager,
                    ActionTestBottom.TAG
                )
            }
        }
    }

    private fun showProgress(isVisible: Boolean) {
        Log.i(TAG, "showProgress called with param = $isVisible")
        binding.progressbar.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    private fun showError(throwable: Throwable) {
        Log.d(TAG, "showError() called with: throwable = $throwable")
        Snackbar.make(binding.root, throwable.toString(), BaseTransientBottomBar.LENGTH_SHORT)
            .show()
    }

    private fun showQizizz(quizizz: List<QuizEntity>) {
        val listTests = listOf(getSpecQuizItems(quizizz), getQuizItems(quizizz))

        binding.rcView.adapter = GroupieAdapter().apply { addAll(listTests) }
        //binding.rcView.adapter = GroupieAdapter().apply { add(getSpecQuizItems(quizizz)) }
    }

    private fun getSpecQuizItems(quizizz: List<QuizEntity>): BindableItem<*> {
        return SpecTestContainer(
            getString(R.string.specCourses),
            quizizz.map { SpecTestItem(it, clickListener) }
        )
    }

    private fun getQuizItems(quizizz: List<QuizEntity>): BindableItem<*> {
        return TestContainer(
            getString(R.string.tests),
            quizizz.map { TestItem(it, clickListener) }
        )
    }

    private fun observeLiveData() {
        viewModel.getErrorLiveData().observe(viewLifecycleOwner, this::showError)
        viewModel.getProgressLiveData().observe(viewLifecycleOwner, this::showProgress)
        viewModel.getQuizzesLiveData().observe(viewLifecycleOwner, this::showQizizz)
    }

    private fun onTestClick(url: String) {
        val bottomDialogFrag = ActionTestBottom.newInstance()
        if(activity != null){
            bottomDialogFrag.show(
                requireActivity().supportFragmentManager, ActionTestBottom.TAG
            )
        }
    }

    companion object {
        const val TAG = "TestFragment"
        private const val TAG_ADD = "$TAG ADD"
        private const val TAG_ERROR = "$TAG ERROR"
        private const val TAG_PROGRESS = "$TAG PROGRESS"

        /**
         * Получение объекта [TestsFragment]
         */
        fun newInstance() = TestsFragment()
    }
}

