package com.ti4all.agendaapp

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ti4all.agendaapp.dao.AutorDao
import com.ti4all.agendaapp.dao.LivroDao
import com.ti4all.agendaapp.data.Autor
import com.ti4all.agendaapp.data.DateConverter
import com.ti4all.agendaapp.data.Livro

@Database(entities = [Autor::class, Livro::class], version = 1)
@TypeConverters(DateConverter::class)
abstract class MyBooksListDatabase : RoomDatabase() {
    abstract fun autorDao() : AutorDao
    abstract fun livroDao() : LivroDao
}
