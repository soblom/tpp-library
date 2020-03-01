package de.soerenblom.app.domain.model.repositories

import de.soerenblom.app.domain.model.entities.Book
import de.soerenblom.app.domain.model.useCases.BorrowLogEntry
import de.soerenblom.app.domain.model.useCases.BorrowingLog

interface BorrowRepository: BorrowingLog {
    fun append(entry: BorrowLogEntry): BorrowLogEntry
    fun entriesForBook(book: Book): Sequence<BorrowLogEntry>
}