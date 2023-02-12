package ru.hse.pe.presentation.content.type.test.ui.topbar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.hse.pe.databinding.FragmentTopAppBarBinding

class TopAppBarFragment : Fragment() {
    private lateinit var binding: FragmentTopAppBarBinding
    private val viewModel: TopAppBarViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentTopAppBarBinding.inflate(inflater, container, false).root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.title.observe(activity as LifecycleOwner) { binding.title.text = it }
        binding.back.setOnClickListener { activity?.finish() }
    }

    companion object {
        @JvmStatic
        fun newInstance() = TopAppBarFragment()
    }
}

class TopAppBarViewModel : ViewModel() {
    val title: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
}