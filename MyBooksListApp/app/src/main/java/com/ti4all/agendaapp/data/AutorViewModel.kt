package com.ti4all.agendaapp.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ti4all.agendaapp.MyBooksListApplication
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class AutorViewModel : ViewModel() {
    private val autorDao = MyBooksListApplication.instance.database.autorDao()
    private val _autorList = MutableStateFlow<List<Autor>>(emptyList())

    init {
        listarTodos()
    }

    fun listarTodos() {
        viewModelScope.launch {
            _autorList.value = autorDao.listarTodos()
        }
    }

    fun inserir(autor: Autor) {
        viewModelScope.launch {
            autorDao.inserir(autor)
            listarTodos()
        }
    }

    fun atualizar(autor: Autor) {
        viewModelScope.launch {
            autorDao.atualizar(autor)
            listarTodos()
        }
    }

    fun deletar(id: Int) {
        viewModelScope.launch {
            autorDao.deletar(id)
            listarTodos()
        }
    }

    val autorList = _autorList
}
