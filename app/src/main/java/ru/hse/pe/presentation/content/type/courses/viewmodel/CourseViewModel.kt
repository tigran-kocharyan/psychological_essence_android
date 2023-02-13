package ru.hse.pe.presentation.content.type.courses.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CourseViewModel : ViewModel(){
    val key: MutableLiveData<String> by lazy{
        MutableLiveData<String>()
    }
}