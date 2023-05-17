package ru.hse.pe.presentation.content.type.technique

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
import ru.hse.pe.databinding.FragmentArticlesBinding
import ru.hse.pe.domain.interactor.ContentInteractor
import ru.hse.pe.domain.model.ArticleEntity
import ru.hse.pe.domain.model.ContentEntity
import ru.hse.pe.presentation.MainActivity
import ru.hse.pe.presentation.content.item.ArticleItem
import ru.hse.pe.presentation.content.item.SubscriptionItem
import ru.hse.pe.presentation.content.type.article.view.ArticleFragment
import ru.hse.pe.presentation.content.viewmodel.ContentViewModel
import ru.hse.pe.presentation.content.viewmodel.ContentViewModelFactory
import ru.hse.pe.utils.Utils
import ru.hse.pe.utils.Utils.getHorizontalCategory
import ru.hse.pe.utils.Utils.setCommonAnimations
import ru.hse.pe.utils.callback.ContentClickListener
import ru.hse.pe.utils.container.VerticalContentContainer
import ru.hse.pe.utils.scheduler.SchedulersProvider
import javax.inject.Inject

class TechniquesFragment : Fragment() {
    @Inject
    lateinit var interactor: ContentInteractor

    @Inject
    lateinit var schedulers: SchedulersProvider

    private lateinit var binding: FragmentArticlesBinding
    private val viewModel: ContentViewModel by viewModels {
        ContentViewModelFactory(
            schedulers,
            interactor
        )
    }
    private val sharedViewModel: SharedViewModel by activityViewModels()

    private var clickListener = object : ContentClickListener {
        override fun onContentClick(content: ContentEntity, position: Int) {
            if (content is ArticleEntity) {
                sharedViewModel.setArticle(content)
                (activity as AppCompatActivity).supportFragmentManager
                    .beginTransaction()
                    .setCommonAnimations()
                    .addToBackStack(null)
                    .add(R.id.fragment_container, ArticleFragment.newInstance(), TAG)
                    .commit()
            }
        }
    }

    private var subscriptionClickListener = object : ContentClickListener {
        override fun onContentClick(content: ContentEntity, position: Int) {
            if (content is ArticleEntity) {
                Utils.getSnackbar(binding.root, getString(R.string.techniques_subscribe))
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
        binding = FragmentArticlesBinding.inflate(inflater, container, false)
        binding.barTitle.text = getString(R.string.techniques)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeLiveData()
        viewModel.getTechniques()
        (activity as MainActivity).isBottomNavVisible(false)
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

    private fun showTechniques(techniques: List<ArticleEntity>) {
        val (subscription, free) = techniques.partition { it.requiresSubscription }
        val categories = free.groupBy { it.category }
        val content = arrayListOf<BindableItem<*>>()
        content.add(
            getHorizontalCategory(
                getString(R.string.techniques_more),
                subscription
            ) { SubscriptionItem(it, subscriptionClickListener) })
        categories.forEach { entry ->
            content.add(entry.key.getCategoryContent(entry.value))
        }
        binding.itemsContainer.adapter = GroupieAdapter().apply { addAll(content) }
    }

    private fun getTechniqueItems(articles: List<ArticleEntity>): BindableItem<*> {
        return VerticalContentContainer(
            getString(R.string.techniques_subscription),
            articles.map { ArticleItem(it, clickListener) }
        )
    }

    private fun observeLiveData() {
        viewModel.getErrorLiveData().observe(viewLifecycleOwner, this::showError)
        viewModel.getProgressLiveData().observe(viewLifecycleOwner, this::showProgress)
        viewModel.getTechniquesLiveData().observe(viewLifecycleOwner, this::showTechniques)
    }

    private fun String.getCategoryContent(list: List<ArticleEntity>): BindableItem<*> =
        getHorizontalCategory(this, list) { SubscriptionItem(it, clickListener) }

    companion object {
        const val TAG = "TechniquesFragment"
        private const val TAG_ADD = "$TAG ADD"
        private const val TAG_ERROR = "$TAG ERROR"
        private const val TAG_PROGRESS = "$TAG PROGRESS"

        /**
         * Получение объекта [TechniquesFragment]
         */
        fun newInstance() = TechniquesFragment()
    }
}