package lab5

import java.time.Year

class Book(val title: String, val authors: List<Author>, val year: Year, val genres: List<Genre>) {
    constructor(title: String, author: Author, year: Year, genres: List<Genre>) :
            this(title, listOf(author), year, genres)

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
        if (!authors.containsAll(otherBook.authors) || !otherBook.authors.containsAll(authors))
            return false
        if (!genres.containsAll(otherBook.genres) || !otherBook.genres.containsAll(genres))
            return false
        return true
    }

    override fun hashCode(): Int {
        var ret = title.hashCode() - year.hashCode()
        authors.forEach { ret -= it.hashCode() }
        genres.forEach { ret += it.hashCode() }
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
    object Available : Status()
    data class UsedBy(val user: User) : Status() {
        override fun toString(): String = "Used by ${user.id}"
    }

    object ComingSoon : Status()
    object Restoration : Status()

    override fun toString(): String {
        return when (this) {
            is Available -> "Available"
            is ComingSoon -> "Coming soon"
            is Restoration -> "Restoration"
            else -> ""
        }
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
    private val books: MutableMap<Book, Status> = emptyMap<Book, Status>().toMutableMap()
    private val users: MutableList<User> = emptyList<User>().toMutableList()

    override fun findBooks(predicate: (Book) -> Boolean) = books.keys.toList().filter(predicate)
    override fun getAllBooks(): List<Book> = books.keys.toList()
    override fun getAllAvailableBooks(): List<Book> = findBooks { books[it] == Status.Available }

    override fun getBookStatus(book: Book): Status = books[book] ?: throw Exception("Book not found")
    override fun getAllBookStatuses(): Map<Book, Status> = books.toMap()

    override fun setBookStatus(book: Book, status: Status) {
        if (!books.contains(book))
            throw Exception("Book not found")
        if (status is Status.UsedBy || books[book] is Status.UsedBy)
            throw Exception("Taking and returning books implemented by the same name functions")
        books[book] = status
    }

    override fun addBook(book: Book, status: Status) {
        if (books.contains(book))
            throw Exception("Book was already added")
        if (status is Status.UsedBy)
            throw Exception("Taking and returning books implemented by the same name functions")
        books[book] = status
    }

    override fun registerUser(id: String) {
        if (users.contains(User(id)))
            throw Exception("User was already registered")
        users.add(User(id))
    }

    override fun unregisterUser(user: User) {
        if (!users.contains(user))
            throw Exception("User not found")
        if (findTakenBooks(user).isNotEmpty())
            throw Exception("User can't be unregistered while he has debts")
        users.remove(user)
    }

    override fun returnBook(book: Book) {
        if (!books.contains(book))
            throw Exception("Book not found")
        if (books[book] !is Status.UsedBy)
            throw Exception("Book wasn't taken")
        books[book] = Status.Available
    }

    override fun takeBook(user: User, book: Book) {
        if (!users.contains(user))
            throw Exception("User not found")
        if (!books.contains(book))
            throw Exception("Book not found")
        if (findTakenBooks(user).size >= 3)
            throw Exception("User can't take more than 3 books at the same time")
        if (books[book] != Status.Available)
            throw Exception("Book is unavailable")
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