package com.ti4all.agendaapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ti4all.agendaapp.models.BookItem
import java.util.Date
import java.text.SimpleDateFormat
import java.util.Locale

@Entity(tableName = "Livros")
data class Livro(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val titulo: String,
    val dataPublicacao: Date?,
    val categoria: String,
    val numeroDePaginas: Int,
    val avaliacaoMedia: Float,
    val lido: Boolean = false,
    val idAutor: Long,
    val dataInclusao: Date = Date(),
    val imagemCapa: String?
)

// Função de extensão para converter BookItem em Livro
fun BookItem.toLivro(): Livro {
    // Converter a data de publicação de String para Date
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val dataPublicacao: Date? = try {
        volumeInfo.publishedDate?.let { dateFormat.parse(it) }
    } catch (e: Exception) {
        null
    }

    return Livro(
        titulo = volumeInfo.title ?: "Título desconhecido",
        dataPublicacao = dataPublicacao,
        categoria = volumeInfo.categories?.getOrNull(0) ?: "Categoria desconhecida",
        numeroDePaginas = volumeInfo.pageCount ?: 0,
        avaliacaoMedia = volumeInfo.averageRating ?: 0f,
        idAutor = volumeInfo.authors?.firstOrNull()?.hashCode()?.toLong() ?: 0L,
        dataInclusao = Date(),
        imagemCapa = volumeInfo.imageLinks?.thumbnail
    )
}
