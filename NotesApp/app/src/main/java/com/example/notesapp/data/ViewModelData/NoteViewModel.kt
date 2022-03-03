package com.example.notesapp.data.ViewModelData

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import androidx.room.Query
import com.example.notesapp.data.model.NoteData
import com.example.notesapp.data.model.NoteDataBase
import com.example.notesapp.data.repository.NoteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NoteViewModel(application: Application) : AndroidViewModel(application) {
    private val noteDao = NoteDataBase.getDataBase(application).noteDao()
    private val repository: NoteRepository
    val getAllData: LiveData<List<NoteData>>
    val sortByHighPriority: LiveData<List<NoteData>>
    val sortByLowPriority: LiveData<List<NoteData>>

    init {
        repository = NoteRepository(noteDao)
        getAllData = repository.getAllData
        sortByHighPriority = repository.sortByHighPriority
        sortByLowPriority = repository.sortByLowPriority
    }

    fun insertData(noteData: NoteData) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertData(noteData)
        }
    }

    fun updateData(noteData: NoteData) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateData(noteData)
        }
    }

    fun deleteData(noteData: NoteData) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteData(noteData)

        }
    }
    fun deleteAllData(noteData: NoteData){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllData()
        }
    }
    fun searchDatabase(searchQuery: String): LiveData<List<NoteData>>{
        return repository.searchData(searchQuery)
    }

}