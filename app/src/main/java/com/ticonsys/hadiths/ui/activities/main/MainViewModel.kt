package com.ticonsys.hadiths.ui.activities.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ticonsys.hadiths.data.db.entities.Book
import com.ticonsys.hadiths.data.db.entities.Chapter
import com.ticonsys.hadiths.data.db.entities.Hadith
import com.ticonsys.hadiths.data.repositories.HadithRepository
import com.ticonsys.hadiths.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: HadithRepository
) : ViewModel() {

    private val _books = MutableLiveData<Resource<List<Book>>>()
    val books: LiveData<Resource<List<Book>>>
        get() = _books

    fun fetchBooks() {
        viewModelScope.launch {
            repository.loadBooks(50, 1).collect { books ->
                _books.value = books
            }
        }
    }

    private val _chapters = MutableLiveData<Resource<List<Chapter>>>()
    val chapters: LiveData<Resource<List<Chapter>>>
        get() = _chapters

    fun fetchChapters(collectionName: String) {
        viewModelScope.launch {
            repository.fetchChapters(collectionName).collect { chapters ->
                _chapters.value = chapters
            }
        }
    }

    private val _hadithList = MutableLiveData<Resource<List<Hadith>>>()
    val hadithList: LiveData<Resource<List<Hadith>>>
        get() = _hadithList

    fun getHadith(collectionName: String, bookNumber: String){
        viewModelScope.launch {
            repository.getHadith(collectionName, bookNumber).collect { hadith ->
                _hadithList.value = hadith
            }
        }
    }

    private val _hadith = MutableLiveData<Resource<Hadith>>()
    val hadith: LiveData<Resource<Hadith>>
        get() = _hadith

    fun getHadithDetail(collectionName: String, hadithNumber: String){
        viewModelScope.launch {
            repository.getHadithDetail(collectionName, hadithNumber).collect { hadith ->
                _hadith.value = hadith
            }
        }
    }

}