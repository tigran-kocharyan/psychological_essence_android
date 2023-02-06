package ru.hse.pe.presentation.content.type.test.ui

import Test
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.compose.ui.platform.ComposeView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import ru.hse.pe.App
import ru.hse.pe.R
import ru.hse.pe.SharedViewModel
import ru.hse.pe.databinding.FragmentComposeBinding
import ru.hse.pe.domain.interactor.ContentInteractor
import ru.hse.pe.domain.model.QuizResultEntity
import ru.hse.pe.presentation.MainActivity
import ru.hse.pe.presentation.content.viewmodel.ContentViewModel
import ru.hse.pe.presentation.content.viewmodel.ContentViewModelFactory
import ru.hse.pe.utils.scheduler.SchedulersProvider
import javax.inject.Inject

class ComposeFragment : Fragment() {
    @Inject
    lateinit var interactor: ContentInteractor

    @Inject
    lateinit var schedulers: SchedulersProvider
    lateinit var title: TextView

    private lateinit var binding: FragmentComposeBinding
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
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_compose, container, false).apply {
            findViewById<ComposeView>(R.id.compose).setContent {
                Test(sharedViewModel, viewModel)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeLiveData()

        title = activity?.findViewById(R.id.nameTest)!!
        title.text = sharedViewModel.quiz.value!!.name
        (activity as MainActivity).isBottomNavVisible(false)
    }

    private fun showProgress(isVisible: Boolean) {
        Log.i(TestsFragment.TAG, "showProgress called with param = $isVisible")

        val progressbar = activity?.findViewById<ProgressBar>(R.id.progressbar)
        progressbar!!.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    private fun showError(throwable: Throwable) {
        Log.d(TestsFragment.TAG, "showError() called with: throwable = $throwable")
        Snackbar.make(binding.root, throwable.toString(), BaseTransientBottomBar.LENGTH_SHORT)
            .show()
    }

    private fun showResults(resultEntity: QuizResultEntity) {
        val result = activity?.findViewById<TextView>(R.id.result)
        val finish = activity?.findViewById<Button>(R.id.finish)
        val compose = activity?.findViewById<CardView>(R.id.compose)
        val container = activity?.findViewById<ConstraintLayout>(R.id.container)


        container!!.visibility = View.VISIBLE
        compose!!.visibility = View.GONE

        result!!.text = resultEntity.content.toString()
        title.text = getString(R.string.results)
        finish!!.setOnClickListener{
            activity?.supportFragmentManager
                ?.beginTransaction()
                ?.setCustomAnimations(
                    R.anim.slide_in,
                    R.anim.fade_out,
                    R.anim.pop_enter,
                    R.anim.pop_exit
                )
                ?.add(R.id.fragment_container, TestsFragment.newInstance(), "ComposeFragment")
                ?.addToBackStack(null)
                ?.commit()
        }
    }

    private fun observeLiveData() {
        viewModel.getErrorLiveData().observe(viewLifecycleOwner, this::showError)
        viewModel.getProgressLiveData().observe(viewLifecycleOwner, this::showProgress)
        viewModel.getQuizResultLiveData().observe(viewLifecycleOwner, this::showResults)
    }

    companion object {
        const val TAG = "ComposeFragment"

        /**
         * Получение объекта [ComposeFragment]
         */
        fun newInstance() = ComposeFragment()
    }
}