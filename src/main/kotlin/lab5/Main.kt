package lab5

import java.time.Year
import kotlin.random.Random

fun main() {
    val lib = createLibrary()
    lib.getAllBookStatuses().forEach {
        println("${it.key} | ${it.value}")
    }
    println()
    lib.findBooks { it.authors.contains(Author("Достоевский Ф.М.")) }.forEach {
        println(it)
    }
    println()
    lib.findBooks { it.year >= Year.of(1900) }.forEach {
        println(it)
    }
    println()
    val user = User("UNIQUE_ID")
    lib.registerUser(user.id)
    val temp = lib.getAllAvailableBooks()
    if (temp.isNotEmpty())
        lib.takeBook(user, temp[0])
    lib.getAllBookStatuses().forEach {
        println("${it.key} | ${it.value}")
    }
}

fun createLibrary(): LibraryService {
    val ret = Library()
    val books = createBooks()
    books.forEach {
        when (Random.nextInt(0, 3)) {
            0 -> {
                ret.addBook(it, Status.Available)
            }
            1 -> {
                ret.addBook(it, Status.ComingSoon)
            }
            2 -> {
                ret.addBook(it, Status.Restoration)
            }
            else -> {
            }
        }
    }
    return ret
}

fun createBooks(): List<Book> {
    val ret = mutableListOf(
        Book(
            "Идиот",
            Author("Достоевский Ф.М."),
            Year.of(1896),
            setOf(Genre.CLASSIC)
        )
    )
    ret.add(
        Book(
            "Бесы",
            Author("Достоевский Ф.М."),
            Year.of(1872),
            setOf(Genre.CLASSIC)
        )
    )
    ret.add(
        Book(
            "Оно",
            Author("Стивен Кинг"),
            Year.of(1986),
            setOf(Genre.HORROR)
        )
    )
    ret.add(
        Book(
            "Сияние",
            Author("Стивен Кинг"),
            Year.of(1977),
            setOf(Genre.HORROR)
        )
    )
    ret.add(
        Book(
            "Собрание сочинений: т.1",
            Author("Артур Конан Дойль"),
            Year.of(1966),
            setOf(Genre.DETECTIVE)
        )
    )
    ret.add(
        Book(
            "Собрание сочинений: т.2",
            Author("Артур Конан Дойль"),
            Year.of(1966),
            setOf(Genre.DETECTIVE)
        )
    )
    ret.add(
        Book(
            "Властелин Колец",
            Author("Толкин Дж.Р.Р."),
            Year.of(1954),
            setOf(Genre.EPIC, Genre.FANTASY)
        )
    )
    ret.add(
        Book(
            "Хоббит",
            Author("Толкин Дж.Р.Р."),
            Year.of(1937),
            setOf(Genre.EPIC, Genre.FANTASY)
        )
    )
    return ret
}