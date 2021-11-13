package lab5

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.Year
import kotlin.test.assertFailsWith

class LibraryTest {

    @Test
    fun addBook() {
        val lib = Library()
        val book1 = Book(
            "Идиот",
            Author("Достоевский Ф.М."),
            Year.of(1896),
            listOf(Genre.CLASSIC)
        )
        val book2 = Book(
            "Бесы",
            Author("Достоевский Ф.М."),
            Year.of(1872),
            listOf(Genre.CLASSIC)
        )
        lib.addBook(book1)
        assertFailsWith<Exception> {
            lib.addBook(book1)
        }
        assertFailsWith<Exception> {
            lib.addBook(book2, Status.UsedBy(User("id")))
        }
        lib.addBook(book2, Status.ComingSoon)
        assertTrue(lib.getAllBooks().contains(book1))
        assertTrue(lib.getAllBooks().contains(book2))
        assertEquals(2, lib.getAllBooks().size)
        assertEquals(lib.getBookStatus(book1), Status.Available)
        assertEquals(lib.getBookStatus(book2), Status.ComingSoon)
    }

    @Test
    fun findBooks() {
        val lib = Library()
        val book1 = Book(
            "Идиот",
            Author("Достоевский Ф.М."),
            Year.of(1896),
            listOf(Genre.CLASSIC)
        )
        val book2 = Book(
            "Бесы",
            Author("Достоевский Ф.М."),
            Year.of(1872),
            listOf(Genre.CLASSIC)
        )
        val book3 = Book(
            "Хоббит",
            Author("Толкин Дж.Р.Р."),
            Year.of(1937),
            listOf(Genre.EPIC, Genre.FANTASY)
        )
        lib.addBook(book1, Status.ComingSoon)
        lib.addBook(book2)
        lib.addBook(book3, Status.ComingSoon)

        val expected1 = listOf(book1, book2)
        val expected2 = listOf(book1, book3)
        val res1 = lib.findBooks { it.year < Year.of(1900) }
        val res2 = lib.findBooks { lib.getBookStatus(it) is Status.ComingSoon }
        assertTrue(expected1.containsAll(res1) && res1.containsAll(expected1))
        assertTrue(expected2.containsAll(res2) && res2.containsAll(expected2))
        assertTrue(lib.findBooks { it.year >= Year.of(2000) }.isEmpty())
    }

    @Test
    fun setBookStatus() {
        val lib = Library()
        val book1 = Book(
            "Идиот",
            Author("Достоевский Ф.М."),
            Year.of(1896),
            listOf(Genre.CLASSIC)
        )
        val book2 = Book(
            "Бесы",
            Author("Достоевский Ф.М."),
            Year.of(1872),
            listOf(Genre.CLASSIC)
        )
        lib.addBook(book1, Status.ComingSoon)
        lib.setBookStatus(book1, Status.Restoration)
        assertFailsWith<Exception> { lib.setBookStatus(book2, Status.Available) }
        assertFailsWith<Exception> { lib.setBookStatus(book1, Status.UsedBy(User("id"))) }
        assertEquals(Status.Restoration, lib.getBookStatus(book1))

        lib.setBookStatus(book1, Status.Available)
        lib.registerUser("id")
        lib.takeBook(User("id"), book1)
        assertFailsWith<Exception> { lib.setBookStatus(book1, Status.Available) }
    }


    @Test
    fun registerUser() {
        val lib = Library()
        lib.registerUser("id")
        assertFailsWith<Exception> { lib.registerUser("id") }
    }

    @Test
    fun unregisterUser() {
        val lib = Library()
        val user = User("id")
        val book = Book("title", Author("author"), Year.of(2000), listOf(Genre.FANTASY))

        lib.registerUser("id")
        lib.unregisterUser(user)
        assertFailsWith<Exception> { lib.unregisterUser(user) }

        lib.registerUser("id")
        lib.addBook(book)
        lib.takeBook(user, book)
        assertFailsWith<Exception> { lib.unregisterUser(user) }
    }

    @Test
    fun returnBook() {
        val lib = Library()
        val user = User("id")
        val book1 = Book(
            "Идиот",
            Author("Достоевский Ф.М."),
            Year.of(1896),
            listOf(Genre.CLASSIC)
        )
        val book2 = Book(
            "Бесы",
            Author("Достоевский Ф.М."),
            Year.of(1872),
            listOf(Genre.CLASSIC)
        )
        lib.registerUser("id")
        lib.addBook(book1)
        lib.takeBook(user, book1)
        lib.returnBook(book1)
        assertFailsWith<Exception> { lib.returnBook(book1) }
        assertFailsWith<Exception> { lib.returnBook(book2) }
    }

    @Test
    fun takeBook() {
        val lib = Library()
        val user1 = User("id1")
        val user2 = User("id2")
        val book1 = Book(
            "Идиот",
            Author("Достоевский Ф.М."),
            Year.of(1896),
            listOf(Genre.CLASSIC)
        )
        val book2 = Book(
            "Бесы",
            Author("Достоевский Ф.М."),
            Year.of(1872),
            listOf(Genre.CLASSIC)
        )
        val book3 = Book(
            "Хоббит",
            Author("Толкин Дж.Р.Р."),
            Year.of(1937),
            listOf(Genre.EPIC, Genre.FANTASY)
        )
        val book4 = Book(
            "Собрание сочинений: т.2",
            Author("Артур Конан Дойль"),
            Year.of(1966),
            listOf(Genre.DETECTIVE)
        )
        lib.addBook(book1)
        lib.registerUser("id1")
        assertFailsWith<Exception> { lib.takeBook(user2, book1) }
        assertFailsWith<Exception> { lib.takeBook(user1, book2) }
        lib.registerUser("id2")
        lib.addBook(book2)
        lib.addBook(book3)
        lib.addBook(book4)
        lib.takeBook(user1, book1)
        lib.takeBook(user1, book2)
        lib.takeBook(user1, book3)
        assertFailsWith<Exception> { lib.takeBook(user1, book4) }
        assertFailsWith<Exception> { lib.takeBook(user2, book1) }
    }
}