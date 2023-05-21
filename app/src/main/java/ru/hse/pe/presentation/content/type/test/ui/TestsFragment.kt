package ru.hse.pe.presentation.content.type.test.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.google.android.material.snackbar.Snackbar
import com.xwray.groupie.GroupieAdapter
import com.xwray.groupie.viewbinding.BindableItem
import ru.hse.pe.App
import ru.hse.pe.R
import ru.hse.pe.SharedViewModel
import ru.hse.pe.databinding.FragmentTestsBinding
import ru.hse.pe.domain.interactor.ContentInteractor
import ru.hse.pe.domain.model.ArticleEntity
import ru.hse.pe.domain.model.ContentEntity
import ru.hse.pe.domain.model.QuizEntity
import ru.hse.pe.presentation.content.item.SpecialTestItem
import ru.hse.pe.presentation.content.item.SubscriptionItem
import ru.hse.pe.presentation.content.item.TestItem
import ru.hse.pe.presentation.content.type.test.ui.sheet.TestPreviewFragment
import ru.hse.pe.presentation.content.viewmodel.ContentViewModel
import ru.hse.pe.presentation.content.viewmodel.ContentViewModelFactory
import ru.hse.pe.utils.Utils
import ru.hse.pe.utils.callback.ContentClickListener
import ru.hse.pe.utils.container.HorizontalContentContainer
import ru.hse.pe.utils.container.VerticalContentContainer
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

    private var subscriptionClickListener = object : ContentClickListener {
        override fun onContentClick(content: ContentEntity, position: Int) {
            if (content is ArticleEntity) {
                Utils.getSnackbar(binding.root, "Данный тест доступен только по подписке!")
                    .show()
            }
        }
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
    }

    private var clickListener = object : ContentClickListener {
        override fun onContentClick(content: ContentEntity, position: Int) {
            if (content is QuizEntity) {
                sharedViewModel.setQuiz(content)
                TestPreviewFragment.newInstance().show(
                    (activity as AppCompatActivity).supportFragmentManager,
                    TestPreviewFragment.TAG
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
        Snackbar.make(binding.root, throwable.toString(), Snackbar.LENGTH_SHORT)
            .show()
    }

    private fun showQizizz(quizizz: List<QuizEntity>) {
//        val listTests = listOf(getSpecialQuizItems(quizizz), getQuizItems(quizizz))
        val (subscription, free) = quizizz.partition { it.requiresSubscription }
        val content = arrayListOf<BindableItem<*>>().apply {
            if (subscription.isNotEmpty()) {
                add(Utils.getHorizontalCategory(
                    "Больше тестов по подписке",
                    subscription
                ) { SubscriptionItem(it, subscriptionClickListener) })
            }
            add(getQuizItems(free))
        }

        val listTests = listOf(content)
        binding.testList.adapter = GroupieAdapter().apply { addAll(content) }
    }

    // берем все тесты, специально подобранные под предпочтения пользователя
    private fun getSpecialQuizItems(quizizz: List<QuizEntity>): BindableItem<*> {
        return HorizontalContentContainer(
            getString(R.string.special_courses),
            quizizz.map { SpecialTestItem(it, clickListener) }
        )
    }

    // берем абсолютно все тесты
    private fun getQuizItems(quizizz: List<QuizEntity>): BindableItem<*> {
        return VerticalContentContainer(
            getString(R.string.tests),
            quizizz.map { TestItem(it, clickListener) }
        )
    }

    private fun observeLiveData() {
        viewModel.getErrorLiveData().observe(viewLifecycleOwner, this::showError)
        viewModel.getProgressLiveData().observe(viewLifecycleOwner, this::showProgress)
        viewModel.getQuizzesLiveData().observe(viewLifecycleOwner, this::showQizizz)
    }

    companion object {
        const val TAG = "TestsFragment"

        /**
         * Получение объекта [TestsFragment]
         */
        fun newInstance() = TestsFragment()
    }
}

