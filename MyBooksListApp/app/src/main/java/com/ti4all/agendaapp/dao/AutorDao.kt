package com.ti4all.agendaapp.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.ti4all.agendaapp.data.Autor

@Dao
interface AutorDao {
    @Insert
    suspend fun inserir(autor: Autor)

    @Update
    suspend fun atualizar(autor: Autor)

    @Query("SELECT * FROM Autores")
    suspend fun listarTodos() : List<Autor>

    @Query("DELETE FROM Autores WHERE id = :id")
    suspend fun deletar(id: Int)
}