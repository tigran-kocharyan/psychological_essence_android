package ru.hse.pe.presentation.shop

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.snackbar.Snackbar
import ru.hse.pe.App
import ru.hse.pe.SharedViewModel
import ru.hse.pe.databinding.FragmentSubscriptionBinding
import ru.hse.pe.presentation.MainActivity


/**
 * [Fragment] to display the subscription.
 *
 * @author Kocharyan Tigran
 */
class SubscriptionFragment : Fragment() {
    private lateinit var binding: FragmentSubscriptionBinding
    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSubscriptionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity?.applicationContext as App).getAppComponent().inject(this)
        (activity as MainActivity).isBottomNavVisible(true)
        binding.bannerMonth.apply {
            this.setOnClickListener {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("${SHOP_URL}${sharedViewModel.uid.value ?: ""}")
                    )
                )
            }
        }
        binding.bannerYear.apply {
            this.setOnClickListener {
                Snackbar.make(
                    this,
                    "На данный момент доступна только ежемесячная подписка!",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
    }


    companion object {
        const val TAG = "SubscriptionFragment"
        private const val SHOP_URL = "https://psessence.ru/payment/link_uid/"

        /**
         * Получение объекта [SubscriptionFragment]
         */
        fun newInstance() = SubscriptionFragment()
    }
}