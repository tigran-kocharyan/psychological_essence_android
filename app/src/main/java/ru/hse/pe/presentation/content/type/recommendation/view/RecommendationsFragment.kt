package ru.hse.pe.presentation.content.type.recommendation.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.viewbinding.ViewBinding
import com.google.android.material.snackbar.Snackbar
import com.xwray.groupie.GroupieAdapter
import com.xwray.groupie.viewbinding.BindableItem
import ru.hse.pe.App
import ru.hse.pe.R
import ru.hse.pe.SharedViewModel
import ru.hse.pe.databinding.FragmentRecommendationsBinding
import ru.hse.pe.domain.interactor.ContentInteractor
import ru.hse.pe.domain.model.ContentEntity
import ru.hse.pe.domain.model.RecommendationEntity
import ru.hse.pe.presentation.MainActivity
import ru.hse.pe.presentation.content.item.BookItem
import ru.hse.pe.presentation.content.item.MovieItem
import ru.hse.pe.presentation.content.item.SeriesItem
import ru.hse.pe.presentation.content.viewmodel.ContentViewModel
import ru.hse.pe.presentation.content.viewmodel.ContentViewModelFactory
import ru.hse.pe.utils.Utils.setCommonAnimations
import ru.hse.pe.utils.callback.ContentClickListener
import ru.hse.pe.utils.container.HorizontalContentContainer
import ru.hse.pe.utils.container.VerticalContentContainer
import ru.hse.pe.utils.scheduler.SchedulersProvider
import javax.inject.Inject

class RecommendationsFragment : Fragment() {
    @Inject
    lateinit var interactor: ContentInteractor

    @Inject
    lateinit var schedulers: SchedulersProvider

    private lateinit var binding: FragmentRecommendationsBinding

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
                    .setCommonAnimations()
                    .addToBackStack(null)
                    .add(R.id.fragment_container, RecommendationFragment.newInstance(), TAG)
                    .commit()
            }
        }
    }

    private var bookClickListener = object : ContentClickListener {
        override fun onContentClick(content: ContentEntity, position: Int) {
            if (content is RecommendationEntity) {
                sharedViewModel.setRecommendation(content)
                (activity as AppCompatActivity).supportFragmentManager
                    .beginTransaction()
                    .setCommonAnimations()
                    .addToBackStack(null)
                    .add(R.id.fragment_container, BookFragment.newInstance(), TAG)
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
        (activity as MainActivity).isBottomNavVisible(false)
        viewModel.getRecommendations()
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

    private fun showRecommendations(recommendations: List<RecommendationEntity>) {
        val categories = recommendations.groupBy { it.category }
        val content = arrayListOf<BindableItem<*>>()
        categories.forEach { entry ->
            content.add(entry.key.getCategoryContent(entry.value))
        }
        binding.itemsContainer.adapter = GroupieAdapter().apply { addAll(content) }
    }

    private fun observeLiveData() {
        viewModel.getErrorLiveData().observe(viewLifecycleOwner, this::showError)
        viewModel.getProgressLiveData().observe(viewLifecycleOwner, this::showProgress)
        viewModel.getRecommendationsLiveData()
            .observe(viewLifecycleOwner, this::showRecommendations)
    }


    private fun <T : ViewBinding> getHorizontalCategory(
        title: String,
        items: List<RecommendationEntity>,
        cast: (RecommendationEntity) -> BindableItem<T>
    ) = HorizontalContentContainer(title, items.map(cast))

    private fun <T : ViewBinding> getVerticalCategory(
        title: String,
        items: List<RecommendationEntity>,
        cast: (RecommendationEntity) -> BindableItem<T>
    ) = VerticalContentContainer(title, items.map(cast))

    private fun String.getCategoryContent(list: List<RecommendationEntity>): BindableItem<*> =
        when (this) {
            "Книги" -> getHorizontalCategory(this, list) { BookItem(it, bookClickListener) }
            "Фильмы" -> getHorizontalCategory(this, list) { MovieItem(it, clickListener) }
            "Сериалы" -> getVerticalCategory(this, list) { SeriesItem(it, clickListener) }
            else -> getVerticalCategory(getString(R.string.recommendation), list) {
                BookItem(
                    it,
                    clickListener
                )
            }
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