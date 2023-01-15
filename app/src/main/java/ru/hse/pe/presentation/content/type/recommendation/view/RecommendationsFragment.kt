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
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.xwray.groupie.GroupieAdapter
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

    private var bookClickListener = object : ContentClickListener {
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
        showDummyAdaptter()
        (activity as MainActivity).isBottomNavVisible(false)
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

//    private fun showRecommendations(recommendations: List<RecommendationEntity>) {
//        adapter.updateData(ArrayList(recommendations))
//    }

    private fun observeLiveData() {
        viewModel.getErrorLiveData().observe(viewLifecycleOwner, this::showError)
        viewModel.getProgressLiveData().observe(viewLifecycleOwner, this::showProgress)
//        viewModel.getRecommendationsLiveData()
//            .observe(viewLifecycleOwner, this::showRecommendations)
    }

    private fun showDummyAdaptter() {
        val movies = listOf(
            getBooks(),
            getMovies(),
            getSeries()
        )

        binding.itemsContainer.adapter = GroupieAdapter().apply { addAll(movies) }
    }

    private fun getBooks(): HorizontalContentContainer {
        return HorizontalContentContainer(
            "Книги",
            listOf(
                BookItem(
                    RecommendationEntity(
                        id = 1,
                        author = "Ксюша",
                        title = "Дар психотерапии Ялом",
                        category = "",
                        year = "",
                        type = "Борьба со страхом",
                        country = "США",
                        content = "Книга состоит из 85 глав-рекомендаций. Каждая глава посвящена одному из правил, принципов, описанию инструментов психологического консультирования.\\n\\n\\\"Дар психотерапии\\\" позволяет взглянуть на профессию психотерапевта изнутри благодаря множеству описанных в ней случаев из практики автора.",
                        imageUrls = arrayListOf("https://psychological-essence.github.io/images/Books/сказать жизни да.jpeg"),
                    ),
                    bookClickListener
                ),
                BookItem(
                    RecommendationEntity(
                        id = 1,
                        author = "Ксюша",
                        title = "Дар психотерапии Ялом",
                        category = "",
                        year = "",
                        type = "Борьба со страхом",
                        country = "США",
                        content = "Книга состоит из 85 глав-рекомендаций. Каждая глава посвящена одному из правил, принципов, описанию инструментов психологического консультирования.\\n\\n\\\"Дар психотерапии\\\" позволяет взглянуть на профессию психотерапевта изнутри благодаря множеству описанных в ней случаев из практики автора.",
                        imageUrls = arrayListOf("https://psychological-essence.github.io/images/Books/сказать жизни да.jpeg"),
                    ),
                    bookClickListener
                ),
                BookItem(
                    RecommendationEntity(
                        id = 1,
                        author = "Ксюша",
                        title = "Дар психотерапии Ялом",
                        category = "",
                        year = "",
                        type = "Борьба со страхом",
                        country = "США",
                        content = "Книга состоит из 85 глав-рекомендаций. Каждая глава посвящена одному из правил, принципов, описанию инструментов психологического консультирования.\\n\\n\\\"Дар психотерапии\\\" позволяет взглянуть на профессию психотерапевта изнутри благодаря множеству описанных в ней случаев из практики автора.",
                        imageUrls = arrayListOf("https://psychological-essence.github.io/images/Books/сказать жизни да.jpeg"),
                    ),
                    bookClickListener
                ),
                BookItem(
                    RecommendationEntity(
                        id = 1,
                        author = "Ксюша",
                        title = "Дар психотерапии Ялом",
                        category = "",
                        year = "",
                        type = "Борьба со страхом",
                        country = "США",
                        content = "Книга состоит из 85 глав-рекомендаций. Каждая глава посвящена одному из правил, принципов, описанию инструментов психологического консультирования.\\n\\n\\\"Дар психотерапии\\\" позволяет взглянуть на профессию психотерапевта изнутри благодаря множеству описанных в ней случаев из практики автора.",
                        imageUrls = arrayListOf("https://psychological-essence.github.io/images/Books/сказать жизни да.jpeg"),
                    ),
                    bookClickListener
                )
            )
        )
    }

    private fun getMovies(): HorizontalContentContainer {
        return HorizontalContentContainer(
            "Фильмы",
            listOf(
                MovieItem(
                    RecommendationEntity(
                        id = 1,
                        author = "Ксюша",
                        "Дар психотерапии Ялом",
                        category = "Фильмы",
                        year = "2001",
                        "США",
                        "1 ч. 30 мин.",
                        content = "Фильм с элементами фэнтези, который повествует о человеке с диссоциативным расстройством личности. История частично основана на жизни реального человека – Билли Миллигана.",
                        imageUrls = arrayListOf("https://psychological-essence.github.io/images/Books/сказать жизни да.jpeg"),
                        requiresSubscription = false
                    ),
                    clickListener
                ),
                MovieItem(
                    RecommendationEntity(
                        id = 1,
                        author = "Ксюша",
                        "Дар психотерапии Ялом",
                        category = "Фильмы",
                        year = "2001",
                        "США",
                        "1 ч. 30 мин.",
                        content = "Фильм с элементами фэнтези, который повествует о человеке с диссоциативным расстройством личности. История частично основана на жизни реального человека – Билли Миллигана.",
                        imageUrls = arrayListOf("https://psychological-essence.github.io/images/Books/сказать жизни да.jpeg"),
                        requiresSubscription = false
                    ),
                    clickListener
                ),
                MovieItem(
                    RecommendationEntity(
                        id = 1,
                        author = "Ксюша",
                        "Дар психотерапии Ялом",
                        category = "Фильмы",
                        year = "2001",
                        "США",
                        "1 ч. 30 мин.",
                        content = "Фильм с элементами фэнтези, который повествует о человеке с диссоциативным расстройством личности. История частично основана на жизни реального человека – Билли Миллигана.",
                        imageUrls = arrayListOf("https://psychological-essence.github.io/images/Books/сказать жизни да.jpeg"),
                        requiresSubscription = false
                    ),
                    clickListener
                ),
                MovieItem(
                    RecommendationEntity(
                        id = 1,
                        author = "Ксюша",
                        "Дар психотерапии Ялом",
                        category = "Фильмы",
                        year = "2001",
                        "США",
                        "1 ч. 30 мин.",
                        content = "Фильм с элементами фэнтези, который повествует о человеке с диссоциативным расстройством личности. История частично основана на жизни реального человека – Билли Миллигана.",
                        imageUrls = arrayListOf("https://psychological-essence.github.io/images/Books/сказать жизни да.jpeg"),
                        requiresSubscription = false
                    ),
                    clickListener
                ),
                MovieItem(
                    RecommendationEntity(
                        id = 1,
                        author = "Ксюша",
                        "Дар психотерапии Ялом",
                        category = "Фильмы",
                        year = "2001",
                        "США",
                        "1 ч. 30 мин.",
                        content = "Фильм с элементами фэнтези, который повествует о человеке с диссоциативным расстройством личности. История частично основана на жизни реального человека – Билли Миллигана.",
                        imageUrls = arrayListOf("https://psychological-essence.github.io/images/Books/сказать жизни да.jpeg"),
                        requiresSubscription = false
                    ),
                    clickListener
                ),
                MovieItem(
                    RecommendationEntity(
                        id = 1,
                        author = "Ксюша",
                        "Дар психотерапии Ялом",
                        category = "Фильмы",
                        year = "2001",
                        "США",
                        "1 ч. 30 мин.",
                        content = "Фильм с элементами фэнтези, который повествует о человеке с диссоциативным расстройством личности. История частично основана на жизни реального человека – Билли Миллигана.",
                        imageUrls = arrayListOf("https://psychological-essence.github.io/images/Books/сказать жизни да.jpeg"),
                        requiresSubscription = false
                    ),
                    clickListener
                )
            )
        )
    }

    private fun getSeries(): VerticalContentContainer {
        return VerticalContentContainer(
            "Сериалы",
            listOf(
                SeriesItem(
                    RecommendationEntity(
                        1,
                        "Ксюша",
                        "Дар психотерапии Ялом",
                        "Фильмы",
                        "2001",
                        "США",
                        episodes = "25",
                        content = "Фильм с элементами фэнтези, который повествует о человеке с диссоциативным расстройством личности. История частично основана на жизни реального человека – Билли Миллигана.",
                        imageUrls = arrayListOf("https://psychological-essence.github.io/images/Series/алиенист.webp"),
                        requiresSubscription = false
                    ),
                    clickListener
                ),
                SeriesItem(
                    RecommendationEntity(
                        1,
                        "Ксюша",
                        "Дар психотерапии Ялом",
                        "Фильмы",
                        "2001",
                        "США",
                        episodes = "25",
                        content = "Фильм с элементами фэнтези, который повествует о человеке с диссоциативным расстройством личности. История частично основана на жизни реального человека – Билли Миллигана.",
                        imageUrls = arrayListOf("https://psychological-essence.github.io/images/Series/алиенист.webp"),
                        requiresSubscription = false
                    ),
                    clickListener
                ),
                SeriesItem(
                    RecommendationEntity(
                        1,
                        "Ксюша",
                        "Дар психотерапии Ялом",
                        "Фильмы",
                        "2001",
                        "США",
                        episodes = "25",
                        content = "Фильм с элементами фэнтези, который повествует о человеке с диссоциативным расстройством личности. История частично основана на жизни реального человека – Билли Миллигана.",
                        imageUrls = arrayListOf("https://psychological-essence.github.io/images/Series/алиенист.webp"),
                        requiresSubscription = false
                    ),
                    clickListener
                ),
                SeriesItem(
                    RecommendationEntity(
                        1,
                        "Ксюша",
                        "Дар психотерапии Ялом",
                        "Фильмы",
                        "2001",
                        "США",
                        episodes = "25",
                        content = "Фильм с элементами фэнтези, который повествует о человеке с диссоциативным расстройством личности. История частично основана на жизни реального человека – Билли Миллигана.",
                        imageUrls = arrayListOf("https://psychological-essence.github.io/images/Series/алиенист.webp"),
                        requiresSubscription = false
                    ),
                    clickListener
                ),
                SeriesItem(
                    RecommendationEntity(
                        1,
                        "Ксюша",
                        "Дар психотерапии Ялом",
                        "Фильмы",
                        "2001",
                        "США",
                        episodes = "25",
                        content = "Фильм с элементами фэнтези, который повествует о человеке с диссоциативным расстройством личности. История частично основана на жизни реального человека – Билли Миллигана.",
                        imageUrls = arrayListOf("https://psychological-essence.github.io/images/Series/алиенист.webp"),
                        requiresSubscription = false
                    ),
                    clickListener
                )
            )
        )
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