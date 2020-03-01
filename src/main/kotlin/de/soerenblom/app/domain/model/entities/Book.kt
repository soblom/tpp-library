package de.soerenblom.app.domain.model.entities

data class Book(val title: BookTitle, val author: AuthorName, val yearPublished: Year)

typealias LibraryCollection = Collection<Book>

typealias BookTitle = String
typealias AuthorName = String
typealias Year = Int

