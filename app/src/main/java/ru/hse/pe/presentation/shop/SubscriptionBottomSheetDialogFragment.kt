package ru.hse.pe.presentation.shop

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import ru.hse.pe.App
import ru.hse.pe.SharedViewModel
import ru.hse.pe.databinding.BottomSheetSubscriptionBinding
import ru.hse.pe.presentation.MainActivity


class SubscriptionBottomSheetDialogFragment : BottomSheetDialogFragment() {
    private lateinit var binding: BottomSheetSubscriptionBinding
    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BottomSheetSubscriptionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity?.applicationContext as App).getAppComponent().inject(this)
        (activity as MainActivity).isBottomNavVisible(false)
        binding.close.setOnClickListener { dismiss() }
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
                dialog?.window?.decorView
                    ?.let { it1 ->
                        Snackbar.make(
                            it1,
                            "На данный момент доступна только ежемесячная подписка!",
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
            }
        }
    }

    companion object {
        const val TAG = "SubscriptionBottomSheetDialogFragment"
        private const val SHOP_URL = "https://psessence.ru/payment/link_uid/"

        /**
         * Получение объекта [SubscriptionBottomSheetDialogFragment]
         */
        fun newInstance(): SubscriptionBottomSheetDialogFragment {
            return SubscriptionBottomSheetDialogFragment()
        }
    }
}