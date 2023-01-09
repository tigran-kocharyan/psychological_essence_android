//package ru.hse.pe.presentation.content.view.article.viewmodel
//
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.ViewModelProvider
//import ru.hse.pe.utils.scheduler.SchedulersProvider
//
///**
// * Фабрика для ViewModel [ArticleViewModel]
// */
//class ArticleViewModelFactory(
//    private val schedulersProvider: SchedulersProvider
//) : ViewModelProvider.Factory {
//
//    override fun <T : ViewModel> create(modelClass: Class<T>): T {
//        return ArticleViewModel(
//            schedulersProvider
//        ) as T
//    }
//}
