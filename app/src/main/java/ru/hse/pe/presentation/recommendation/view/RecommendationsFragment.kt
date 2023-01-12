package ru.hse.pe.presentation.recommendation.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import ru.hse.pe.App
import ru.hse.pe.R
import ru.hse.pe.SharedViewModel
import ru.hse.pe.databinding.FragmentRecommendationsBinding
import ru.hse.pe.domain.interactor.ContentInteractor
import ru.hse.pe.domain.model.ContentEntity
import ru.hse.pe.domain.model.RecommendationEntity
import ru.hse.pe.presentation.MainActivity
import ru.hse.pe.presentation.content.adapter.ContentAdapter
import ru.hse.pe.presentation.content.viewmodel.ContentViewModel
import ru.hse.pe.presentation.content.viewmodel.ContentViewModelFactory
import ru.hse.pe.utils.callback.ContentClickListener
import ru.hse.pe.utils.scheduler.SchedulersProvider
import javax.inject.Inject

class RecommendationsFragment : Fragment() {
    @Inject
    lateinit var interactor: ContentInteractor

    @Inject
    lateinit var schedulers: SchedulersProvider

    private lateinit var binding: FragmentRecommendationsBinding
    private lateinit var adapter: ContentAdapter
    private val viewModel: ContentViewModel by viewModels {
        ContentViewModelFactory(
            schedulers,
            interactor
        )
    }
    private val sharedViewModel: SharedViewModel by activityViewModels()

    private var clickListener = object : ContentClickListener {
        override fun onContentClick(content: ContentEntity, position: Int) {
            if (content is RecommendationEntity) {
                sharedViewModel.setRecommendation(content)
                (activity as AppCompatActivity).supportFragmentManager
                    .beginTransaction()
                    .setCustomAnimations(
                        R.anim.slide_in,
                        R.anim.fade_out,
                        R.anim.pop_enter,
                        R.anim.pop_exit
                    )
                    .addToBackStack(null)
                    .add(R.id.fragment_container, RecommendationFragment.newInstance(), TAG)
                    .commit()
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
        binding = FragmentRecommendationsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeLiveData()
        createAdapter()
        (activity as MainActivity).isBottomNavVisible(false)
    }

    private fun createAdapter() {
        adapter = ContentAdapter(ArrayList(), clickListener)
        binding.series.layoutManager = LinearLayoutManager(context)
        binding.series.adapter = adapter
        viewModel.getRecommendations()
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

    private fun showRecommendations(recommendations: List<RecommendationEntity>) {
        adapter.updateData(ArrayList(recommendations))
    }

    private fun observeLiveData() {
        viewModel.getErrorLiveData().observe(viewLifecycleOwner, this::showError)
        viewModel.getProgressLiveData().observe(viewLifecycleOwner, this::showProgress)
        viewModel.getRecommendationsLiveData()
            .observe(viewLifecycleOwner, this::showRecommendations)
    }

    companion object {
        const val TAG = "RecommendationsFragment"
        private const val TAG_ADD = "$TAG ADD"
        private const val TAG_ERROR = "$TAG ERROR"
        private const val TAG_PROGRESS = "$TAG PROGRESS"

        /**
         * Получение объекта [RecommendationsFragment]
         */
        fun newInstance() = RecommendationsFragment()
    }
}