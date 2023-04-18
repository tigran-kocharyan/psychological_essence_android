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
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import org.json.JSONObject
import ru.hse.pe.App
import ru.hse.pe.SharedViewModel
import ru.hse.pe.databinding.FragmentTestResultBinding
import ru.hse.pe.domain.interactor.ContentInteractor
import ru.hse.pe.domain.model.QuizAnswerEntity
import ru.hse.pe.domain.model.QuizResultEntity
import ru.hse.pe.presentation.content.type.test.ui.compose.Test
import ru.hse.pe.presentation.content.viewmodel.ContentViewModel
import ru.hse.pe.presentation.content.viewmodel.ContentViewModelFactory
import ru.hse.pe.utils.scheduler.SchedulersProvider
import javax.inject.Inject


class TestResultFragment : Fragment() {
    private lateinit var binding: FragmentTestResultBinding
    private lateinit var result: QuizAnswerEntity

    @Inject
    lateinit var interactor: ContentInteractor

    @Inject
    lateinit var schedulers: SchedulersProvider

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
        binding = FragmentTestResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeLiveData()
        result = QuizAnswerEntity(
            sharedViewModel.quiz.value?.id,
            FirebaseAuth.getInstance().currentUser?.uid.toString(),
            Test.userAnswersString
        )
        viewModel.getQuizResult(result)
        binding.error.setOnClickListener { viewModel.getQuizResult(result) }
        binding.finish.setOnClickListener {
            (activity as AppCompatActivity).supportFragmentManager.popBackStack()
        }
    }

    companion object {
        const val TAG = "TestResultFragment"

        /**
         * Получение объекта [TestResultFragment]
         */
        fun newInstance() = TestResultFragment()
    }

    private fun showProgress(isVisible: Boolean) {
        Log.i(TestsFragment.TAG, "showProgress called with param = $isVisible")
        binding.progressbar.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    private fun showError(throwable: Throwable) {
        Log.d(TestsFragment.TAG, "showError() called with: throwable = $throwable")
        binding.error.visibility = View.VISIBLE
        Snackbar.make(binding.root, throwable.toString(), BaseTransientBottomBar.LENGTH_SHORT)
            .show()
    }

    private fun showResults(quizResultEntity: QuizResultEntity) {
        binding.result.text = quizResultEntity.content
        binding.error.visibility = View.GONE
    }

    private fun observeLiveData() {
        viewModel.getErrorLiveData().observe(viewLifecycleOwner, this::showError)
        viewModel.getProgressLiveData().observe(viewLifecycleOwner, this::showProgress)
        viewModel.getQuizResultLiveData().observe(viewLifecycleOwner, this::showResults)
    }
}

