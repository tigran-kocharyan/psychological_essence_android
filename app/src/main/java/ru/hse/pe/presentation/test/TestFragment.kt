package ru.hse.pe.presentation.test

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import ru.hse.pe.databinding.*
import ru.hse.pe.domain.interactor.ContentInteractor
import ru.hse.pe.domain.model.QuizEntity
import ru.hse.pe.presentation.MainActivity
import ru.hse.pe.presentation.content.type.fact.view.FactsFragment
import ru.hse.pe.presentation.content.viewmodel.ContentViewModel
import ru.hse.pe.presentation.content.viewmodel.ContentViewModelFactory
import ru.hse.pe.presentation.courses.viewmodel.TopAbbBarViewModel
import ru.hse.pe.presentation.test.bottomSheetFragment.ActionTestBottom
import ru.hse.pe.presentation.test.groupie.TestContainer
import ru.hse.pe.presentation.test.groupie.TestItem
import ru.hse.pe.presentation.test.groupie.SpecTestContainer
import ru.hse.pe.presentation.test.groupie.SpecTestItem
import ru.hse.pe.utils.container.VerticalContentContainer
import ru.hse.pe.utils.scheduler.SchedulersProvider
import javax.inject.Inject


class TestFragment : Fragment() {
    @Inject
    lateinit var interactor: ContentInteractor

    @Inject
    lateinit var schedulers: SchedulersProvider

    private lateinit var binding: FragmentTestBinding
    private val topAppBarModel: TopAbbBarViewModel by viewModels()
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
        binding = FragmentTestBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeLiveData()
        viewModel.getQuizzes()
        (activity as MainActivity).isBottomNavVisible(false)
    }

    private fun getSpecTests(): BindableItem<SpecTestContainerBinding> {
        return SpecTestContainer(
            getString(R.string.specCourses),
            listOf(
                SpecTestItem(
                    "Тест на определение типа личности",
                    24,
                    10,
                    R.drawable.exampl,
                    ::onTestClick,
                ),
                SpecTestItem(
                    "Тест на определение типа личности",
                    24,
                    10,
                    R.drawable.exampl,
                    ::onTestClick
                ),
                SpecTestItem(
                    "Тест на определение типа личности",
                    24,
                    10,
                    R.drawable.exampl,
                    ::onTestClick
                ),
            ),
        )
    }

    private fun showProgress(isVisible: Boolean) {
        Log.i(FactsFragment.TAG, "showProgress called with param = $isVisible")
        binding.progressbarTest.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    private fun showError(throwable: Throwable) {
        Log.d(FactsFragment.TAG, "showError() called with: throwable = $throwable")
        Snackbar.make(binding.root, throwable.toString(), BaseTransientBottomBar.LENGTH_SHORT)
            .show()
    }

    private fun showQuizez(quizez: List<QuizEntity>) {
        binding.itemsContainer.adapter = GroupieAdapter().apply { add(getQuizItems(quizez)) }
    }

    private fun getQuizItems(quizez: List<QuizEntity>): BindableItem<*> {
        return VerticalContentContainer(
            "Факты дня",
            quizez.map { QuizEntity(it, clickListener) }
        )
    }

    private fun observeLiveData() {
        viewModel.getErrorLiveData().observe(viewLifecycleOwner, this::showError)
        viewModel.getProgressLiveData().observe(viewLifecycleOwner, this::showProgress)
        viewModel.getQuizzesLiveData().observe(viewLifecycleOwner, this::showQuizez)
    }

    private fun getAllTests(): BindableItem<TestContainerBinding> {
        return TestContainer(
            getString(R.string.tests),
            listOf(
                TestItem(
                    "Тест на определение типа личности",
                    getString(R.string.tempLorem),
                    24,
                    10,
                    R.drawable.exampl,
                    ::onTestClick,
                ),
                TestItem(
                    "Тест на определение типа личности",
                    getString(R.string.tempLorem),
                    24,
                    10,
                    R.drawable.exampl,
                    ::onTestClick,
                ),
                TestItem(
                    "Тест на определение типа личности",
                    getString(R.string.tempLorem),
                    24,
                    10,
                    R.drawable.exampl,
                    ::onTestClick,
                ),
            ),
        )
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
         * Получение объекта [FactsFragment]
         */
        fun newInstance() = TestFragment()
    }
}



//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivityTestBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        val tests = listOf(getSpecTests(), getAllTests())
//        binding.rcView.adapter = GroupAdapter<GroupieViewHolder>().apply { addAll(tests) }
//
//        openFragment(R.id.topappbar, TopAppBarFragment.newInstance())
//        topAppBarModel.title.value = getString(R.string.tests)
//
//        ContentInteractor().getQuizzes()
//    }
