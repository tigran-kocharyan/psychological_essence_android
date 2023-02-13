package ru.hse.pe.presentation.content.type.courses.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TopAbbBarViewModel : ViewModel() {
    val title: MutableLiveData<String> by lazy{
        MutableLiveData<String>()
    }
}