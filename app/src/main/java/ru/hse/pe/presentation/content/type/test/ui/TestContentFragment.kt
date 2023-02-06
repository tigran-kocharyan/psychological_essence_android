package ru.hse.pe.presentation.content.type.test.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import ru.hse.pe.App
import ru.hse.pe.SharedViewModel
import ru.hse.pe.domain.interactor.ContentInteractor
import ru.hse.pe.presentation.content.type.test.ui.compose.Test
import ru.hse.pe.presentation.content.type.test.utils.theme.TestTheme
import ru.hse.pe.presentation.content.viewmodel.ContentViewModel
import ru.hse.pe.presentation.content.viewmodel.ContentViewModelFactory
import ru.hse.pe.utils.scheduler.SchedulersProvider
import javax.inject.Inject


class TestContentFragment : Fragment() {
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
    ) = ComposeView(requireContext()).apply {
        setContent {
            TestTheme {
                Test(sharedViewModel, viewModel)
            }
        }
    }

    companion object {
        const val TAG = "TestContentFragment"

        /**
         * Получение объекта [TestContentFragment]
         */
        fun newInstance() = TestContentFragment()
    }
}

