package com.ti4all.agendaapp.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ti4all.agendaapp.MyBooksListApplication
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LivroViewModel(application: MyBooksListApplication) : ViewModel() {
    private val livroDao = application.database.livroDao()
    private val _livroList = MutableStateFlow<List<Livro>>(emptyList())
    val livroList: StateFlow<List<Livro>> = _livroList

    init {
        listarTodos()
    }

    // Construtor padrão necessário para ViewModelProvider
    @Suppress("unused")
    constructor() : this(MyBooksListApplication.instance)

    fun listarTodos() {
        viewModelScope.launch {
            _livroList.value = livroDao.listarTodos()
        }
    }

    fun inserir(livro: Livro) {
        viewModelScope.launch {
            livroDao.inserir(livro)
            listarTodos()
        }
    }

    fun atualizar(livro: Livro) {
        viewModelScope.launch {
            livroDao.atualizar(livro) // Atualiza o contato no banco de dados
            listarTodos() // Recarrega a lista
        }
    }

    fun deletar(id: Int) {
        viewModelScope.launch {
            livroDao.deletar(id)
            listarTodos()
        }
    }
}