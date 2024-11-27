package com.ti4all.agendaapp.models

class VolumeInfo (
    val title: String?,
    val publishedDate: String?,
    val categories: List<String>?,
    val pageCount: Int?,
    val averageRating: Float?,
    val authors: List<String>?,
    val imageLinks: ImageLinks?
) {
    data class ImageLinks(
        val thumbnail: String?
    )
}