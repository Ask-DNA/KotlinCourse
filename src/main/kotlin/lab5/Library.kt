package lab5

import java.time.Year

val bookNotFoundException = NoSuchElementException("Book not found")
val userNotFoundException = NoSuchElementException("User not found")

class Book(val title: String, val authors: Set<Author>, val year: Year, val genres: Set<Genre>) {
    constructor(title: String, author: Author, year: Year, genres: Set<Genre>) :
            this(title, setOf(author), year, genres)

    override fun toString(): String {
        var ret = StringBuilder("\"$title\" ")
        authors.forEach {
            ret.append("${it.name}, ")
        }
        ret = StringBuilder(ret.dropLast(2))
        ret.append("; ($year); Genres: ")
        genres.forEach {
            ret.append("$it, ")
        }
        ret = StringBuilder(ret.dropLast(2))
        return ret.toString()
    }

    override fun equals(other: Any?): Boolean {
        val otherBook = other as? Book ?: return false
        if (title != otherBook.title || year != otherBook.year)
            return false
        // order of elements in 'authors' and 'genres' isn't taken into account
        if (!authors.containsAll(otherBook.authors) || !otherBook.authors.containsAll(authors))
            return false
        if (!genres.containsAll(otherBook.genres) || !otherBook.genres.containsAll(genres))
            return false
        return true
    }

    override fun hashCode(): Int {
        var ret = title.hashCode() * 2 + year.hashCode() * 3
        authors.forEach { ret += it.hashCode() * 4 }
        genres.forEach { ret += it.hashCode() * 5 }
        return ret
    }
}

data class Author(val name: String)

data class User(val id: String)

enum class Genre {
    CLASSIC,
    EPIC,
    FANTASY,
    DETECTIVE,
    HORROR
}

sealed class Status {
    object Available : Status() {
        override fun toString(): String = "Available"
    }

    data class UsedBy(val user: User) : Status() {
        override fun toString(): String = "Used by ${user.id}"
    }

    object ComingSoon : Status() {
        override fun toString(): String = "Coming soon"
    }

    object Restoration : Status() {
        override fun toString(): String = "Restoration"
    }
}

interface LibraryService {
    fun findBooks(predicate: (Book) -> Boolean): List<Book>
    fun getAllBooks(): List<Book>
    fun getAllAvailableBooks(): List<Book>

    fun getBookStatus(book: Book): Status
    fun getAllBookStatuses(): Map<Book, Status>

    fun setBookStatus(book: Book, status: Status)

    fun addBook(book: Book, status: Status = Status.Available)

    fun registerUser(id: String)
    fun unregisterUser(user: User)

    fun takeBook(user: User, book: Book)
    fun returnBook(book: Book)
}

class Library : LibraryService {
    private val books: MutableMap<Book, Status> = mutableMapOf()
    private val users: MutableList<User> = mutableListOf()

    override fun findBooks(predicate: (Book) -> Boolean) = books.keys.toList().filter(predicate)
    override fun getAllBooks(): List<Book> = books.keys.toList()
    override fun getAllAvailableBooks(): List<Book> = findBooks { books[it] == Status.Available }

    override fun getBookStatus(book: Book): Status = books[book] ?: throw bookNotFoundException
    override fun getAllBookStatuses(): Map<Book, Status> = books.toMap()

    override fun setBookStatus(book: Book, status: Status) {
        if (!books.contains(book))
            throw bookNotFoundException
        if (status is Status.UsedBy || books[book] is Status.UsedBy)
            throw UnsupportedOperationException("Taking and returning books implemented by the same name functions")
        books[book] = status
    }

    override fun addBook(book: Book, status: Status) {
        if (books.contains(book))
            throw IllegalArgumentException("Book was already added")
        if (status is Status.UsedBy)
            throw UnsupportedOperationException("Taking and returning books implemented by the same name functions")
        books[book] = status
    }

    override fun registerUser(id: String) {
        if (users.contains(User(id)))
            throw IllegalArgumentException("User was already registered")
        users.add(User(id))
    }

    override fun unregisterUser(user: User) {
        if (!users.contains(user))
            throw userNotFoundException
        if (findTakenBooks(user).isNotEmpty())
            throw IllegalStateException("User can't be unregistered while he has debts")
        users.remove(user)
    }

    override fun returnBook(book: Book) {
        if (!books.contains(book))
            throw bookNotFoundException
        if (books[book] !is Status.UsedBy)
            throw IllegalStateException("Book wasn't taken")
        books[book] = Status.Available
    }

    override fun takeBook(user: User, book: Book) {
        if (!users.contains(user))
            throw userNotFoundException
        if (!books.contains(book))
            throw bookNotFoundException
        if (findTakenBooks(user).size >= 3)
            throw IllegalStateException("User can't take more than 3 books at the same time")
        if (books[book] != Status.Available)
            throw IllegalStateException("Book is unavailable")
        books[book] = Status.UsedBy(user)
    }

    private fun findTakenBooks(user: User): List<Book> {
        val predicate: (Book) -> Boolean = {
            when (val status = books[it]) {
                is Status.UsedBy -> (status.user == user)
                else -> false
            }
        }
        return findBooks(predicate)
    }
}