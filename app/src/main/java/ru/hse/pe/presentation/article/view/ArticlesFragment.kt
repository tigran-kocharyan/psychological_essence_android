package ru.hse.pe.presentation.article.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import ru.hse.pe.presentation.content.adapter.ContentAdapter
import ru.hse.pe.App
import ru.hse.pe.R
import ru.hse.pe.SharedViewModel
import ru.hse.pe.databinding.FragmentArticlesBinding
import ru.hse.pe.domain.interactor.ContentInteractor
import ru.hse.pe.domain.model.ArticleEntity
import ru.hse.pe.presentation.MainActivity
import ru.hse.pe.presentation.content.view.article.viewmodel.ContentViewModel
import ru.hse.pe.utils.callback.ContentClickListener
import ru.hse.pe.utils.scheduler.SchedulersProvider
import javax.inject.Inject

class ArticlesFragment : Fragment() {
    private lateinit var binding: FragmentArticlesBinding
    private lateinit var viewModel: ContentViewModel
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private lateinit var adapter: ContentAdapter

    @Inject
    lateinit var interactor: ContentInteractor

    @Inject
    lateinit var schedulers: SchedulersProvider

    private var clickListener = object : ContentClickListener {
        override fun onArticleClick(article: ArticleEntity, position: Int) {
            sharedViewModel.setArticle(article)
            (activity as AppCompatActivity).supportFragmentManager
                .beginTransaction()
                .setCustomAnimations(
                    R.anim.slide_in,
                    R.anim.fade_out,
                    R.anim.pop_enter,
                    R.anim.pop_exit
                )
                .addToBackStack(null)
                .add(R.id.fragment_container, ArticleFragment.newInstance(), TAG)
                .commit()
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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        createViewModel()
        observeLiveData()
        createAdapter()
        (activity as MainActivity).isBottomNavVisible(false)
    }

    private fun createViewModel() {
        viewModel = ContentViewModel(schedulers, interactor)
    }

    private fun createAdapter() {
        adapter = ContentAdapter(ArrayList(), clickListener)
        binding.articles.layoutManager = LinearLayoutManager(context)
        binding.articles.adapter = adapter
        viewModel.getArticles()
    }

    private fun showProgress(isVisible: Boolean) {
        Log.i(TAG, "showProgress called with param = $isVisible")
        binding.progressbar.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    private fun showError(throwable: Throwable) {
        Log.d(TAG, "showError() called with: throwable = $throwable")
        Snackbar.make(binding.root, throwable.toString(), BaseTransientBottomBar.LENGTH_SHORT).show()
    }

    private fun showArticles(articles: List<ArticleEntity>) {
        adapter.updateData(ArrayList(articles))
    }

    private fun observeLiveData() {
        viewModel.getErrorLiveData().observe(viewLifecycleOwner, this::showError)
        viewModel.getProgressLiveData().observe(viewLifecycleOwner, this::showProgress)
        viewModel.getArticlesLiveData().observe(viewLifecycleOwner, this::showArticles)
    }

    companion object {
        const val TAG = "ArticlesFragment"
        private const val TAG_ADD = "$TAG ADD"
        private const val TAG_ERROR = "$TAG ERROR"
        private const val TAG_PROGRESS = "$TAG PROGRESS"

        /**
         * Получение объекта [ArticlesFragment]
         */
        fun newInstance() = ArticlesFragment()
    }
}