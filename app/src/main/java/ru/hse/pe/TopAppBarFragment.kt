package ru.hse.pe

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import ru.hse.pe.databinding.FragmentTopAppBarBinding
import ru.hse.pe.presentation.content.type.courses.viewmodel.TopAbbBarViewModel

class TopAppBarFragment : Fragment() {
    private lateinit var binding: FragmentTopAppBarBinding
    private val dataModel: TopAbbBarViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = FragmentTopAppBarBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        dataModel.title.observe(activity as LifecycleOwner) {
//            binding.titleTopAppBar.text = it
//        }
//
//        binding.imageBack.setOnClickListener{
//            activity?.finish()
//        }
    }

    companion object {
        @JvmStatic fun newInstance() = TopAppBarFragment()
    }
}