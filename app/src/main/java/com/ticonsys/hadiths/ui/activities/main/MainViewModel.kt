package com.ticonsys.hadiths.ui.activities.main

import androidx.lifecycle.ViewModel
import com.ticonsys.hadiths.data.repositories.HadithRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: HadithRepository
): ViewModel(){

    fun fetchBooks() = repository.loadBooks(50, 1)
    fun fetchChapters(collectionName: String) = repository.fetchChapters(collectionName)

}