package de.soerenblom.app.domain.model.useCases

import de.soerenblom.app.domain.model.entities.Book
import de.soerenblom.app.domain.model.entities.Member
import de.soerenblom.app.domain.model.repositories.BorrowRepository
import java.time.LocalDate

/*
Members want to borrow books to take them home.
A book has to be available (aka not already borrowed) to be borrowed. Let's assume for this example
that we keep track of currently borrowed books log, where we record that a books has been
borrowed and that is has been returned.
 */

sealed class BorrowLogEntry {
    abstract val book: Book
    abstract val entryDate: LogEntryDate

    data class Borrow(
        val member: Member,
        override val book: Book,
        override val entryDate: LogEntryDate
    ) : BorrowLogEntry()

    data class Return(
        override val book: Book,
        override val entryDate: LogEntryDate
    ) :
        BorrowLogEntry()
}


typealias BorrowingLog = Sequence<BorrowLogEntry>

typealias LogEntryDate = LocalDate

/*
The two use cases around borrowing a book is to borrow and to return a book.
Borrowing means, that a member has found a book they want to take home. The library needs to
make a record of this in the BorrowingLog. An important precondition is that the book is actually
not already borrowed.
*/

fun borrowBook(
    book: Book, member: Member, log: BorrowRepository, date: LogEntryDate
): BorrowLogEntry {
    val sortedEntries = log.entriesForBook(book).sortedByDescending { it.entryDate }
    return when (val lastEntry = sortedEntries.firstOrNull()) {
        is BorrowLogEntry.Borrow -> lastEntry
        else -> {
            val newBorrowEntry = BorrowLogEntry.Borrow(member, book, date)
            return log.append(newBorrowEntry)
        }
    }
}

//Returning is an operation where a return entry is made in the borrow log

typealias returning = (BorrowingLog, BorrowLogEntry.Return) -> BorrowingLog

