package ru.hse.pe.presentation.content

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.hse.pe.R
import ru.hse.pe.databinding.FragmentContentBinding
import ru.hse.pe.presentation.MainActivity
import ru.hse.pe.presentation.content.type.article.view.ArticlesFragment
import ru.hse.pe.presentation.content.type.fact.view.FactsFragment
import ru.hse.pe.presentation.content.type.recommendation.view.RecommendationsFragment
import ru.hse.pe.presentation.content.type.technique.TechniquesFragment
import ru.hse.pe.presentation.content.type.test.ui.TestsFragment
import ru.hse.pe.presentation.shop.SubscriptionFragment
import ru.hse.pe.utils.Utils.setCommonAnimations

/**
 * [Fragment] to display the trending events.
 *
 * @author Kocharyan Tigran
 */
class ContentFragment : Fragment() {
    private lateinit var binding: FragmentContentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentContentBinding.inflate(inflater, container, false)
        (activity as MainActivity).isBottomNavVisible(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).isBottomNavVisible(true)
        binding.tests.setOnClickListener {
            setCurrentFragment(
                TestsFragment.newInstance(),
                TestsFragment.TAG
            )
        }
        binding.articles.setOnClickListener {
            setCurrentFragment(
                ArticlesFragment.newInstance(),
                ArticlesFragment.TAG
            )
        }
        binding.recommendations.setOnClickListener {
            setCurrentFragment(
                RecommendationsFragment.newInstance(),
                RecommendationsFragment.TAG
            )
        }
        binding.facts.setOnClickListener {
            setCurrentFragment(
                FactsFragment.newInstance(),
                FactsFragment.TAG
            )
        }
        binding.techniques.setOnClickListener {
            setCurrentFragment(
                TechniquesFragment.newInstance(),
                TechniquesFragment.TAG
            )
        }
        binding.subscription.setOnClickListener {
            (activity as MainActivity).setDefaultMenuItemSelected(R.id.shop)
            (activity as MainActivity).setCurrentFragment(
                SubscriptionFragment.newInstance(),
                SubscriptionFragment.TAG
            )
        }
    }

    private fun setCurrentFragment(fragment: Fragment, tag: String) =
        parentFragmentManager
            .beginTransaction()
            .setCommonAnimations()
            .replace(R.id.fragment_container, fragment, tag)
            .addToBackStack(null)
            .commit()

    companion object {
        const val TAG = "ContentFragment"

        /**
         * Получение объекта [ContentFragment]
         */
        fun newInstance() = ContentFragment()
    }
}