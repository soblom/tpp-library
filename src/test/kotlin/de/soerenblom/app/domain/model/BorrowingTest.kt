package de.soerenblom.app.domain.model

import de.soerenblom.app.domain.model.entities.Book
import de.soerenblom.app.domain.model.entities.Member
import de.soerenblom.app.domain.model.repositories.BorrowRepository
import de.soerenblom.app.domain.model.useCases.BorrowLogEntry
import de.soerenblom.app.domain.model.useCases.LogEntryDate
import de.soerenblom.app.domain.model.useCases.borrowBook
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import java.time.LocalDateTime

class BorrowingTest {

    private val sampleBook = Book("Wonderful book", "MacAuthorton", 1978)
    private val sampleMember = Member("Sir BookALot", 12312)
    private val sampleDate = LogEntryDate.of(2020, 3, 1)

    @Test
    fun whenBorrowingABookForFirstTime_thenNewBorrowEntryIsReturned() {
        val sampleEntry = BorrowLogEntry.Borrow(sampleMember, sampleBook, sampleDate)

        val borrowRepo = Mockito.mock(BorrowRepository::class.java)
        Mockito.`when`(borrowRepo.entriesForBook(sampleEntry.book)).thenReturn(emptySequence())
        Mockito.`when`(borrowRepo.append(sampleEntry)).thenReturn(sampleEntry)

        val result = borrowBook(sampleBook, sampleMember, borrowRepo, sampleDate)
        assertEquals(result, sampleEntry)
    }

    @Test
    fun whenBookAlreadyBorrowed_thenMostRecentBorrowEntryIsReturned() {
        val borrowed1 = BorrowLogEntry.Borrow(sampleMember, sampleBook, sampleDate)
        val returned1 = BorrowLogEntry.Return(sampleBook, sampleDate.plusDays(1))
        val borrowed2 = BorrowLogEntry.Borrow(sampleMember, sampleBook, sampleDate.plusDays(2))

        val borrowRepo = Mockito.mock(BorrowRepository::class.java)
        Mockito.`when`(borrowRepo.entriesForBook(borrowed1.book))
            .thenReturn(sequenceOf(borrowed2, borrowed1, returned1))

        val result = borrowBook(sampleBook, sampleMember, borrowRepo, sampleDate.plusDays(10))
        assertEquals(result, borrowed1)

    }

}