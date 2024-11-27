package com.ti4all.agendaapp.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.ti4all.agendaapp.data.Livro

@Dao
interface LivroDao {
    @Insert
    suspend fun inserir(contato: Livro)

    @Update
    suspend fun atualizar(contato: Livro)

    @Query("SELECT * FROM livros")
    suspend fun listarTodos() : List<Livro>

    @Query("DELETE FROM Livros WHERE id = :id")
    suspend fun deletar(id: Int)
}