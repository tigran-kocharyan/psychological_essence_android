package ru.hse.pe

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class TopAppBarFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_top_app_bar, container, false)
    }

    companion object {
        @JvmStatic fun newInstance() = TopAppBarFragment()
    }
}