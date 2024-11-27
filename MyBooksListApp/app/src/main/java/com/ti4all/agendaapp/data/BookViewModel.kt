package com.ti4all.agendaapp.data

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class BookViewModel : ViewModel() {
    private val _livros = mutableStateOf<List<Livro>>(emptyList())
    val livros: State<List<Livro>> = _livros

     fun fetchBooks(query: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.searchBooks(query)
                _livros.value = response.items?.map { it.toLivro() } ?: emptyList()
            } catch (e: Exception) {
                Log.e("BookViewModel", "Erro ao buscar livros ${e.message}")
            }
        }
    }
}