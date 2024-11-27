package com.ti4all.agendaapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "Autores")
data class Autor (
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val nome: String,
    val nacionalidade: String,
    val biografia: String,
    val dataNascimento: Date = Date()
)