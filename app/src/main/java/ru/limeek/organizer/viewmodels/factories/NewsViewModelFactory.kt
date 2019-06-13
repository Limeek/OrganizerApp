package ru.limeek.organizer.viewmodels.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.limeek.organizer.data.repository.NewsRepository
import ru.limeek.organizer.viewmodels.NewsViewModel
import javax.inject.Inject

class NewsViewModelFactory @Inject constructor(private var newsRepo: NewsRepository): ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(NewsViewModel::class.java)){
            return NewsViewModel(newsRepo) as T
        }
        throw IllegalStateException()
    }
}