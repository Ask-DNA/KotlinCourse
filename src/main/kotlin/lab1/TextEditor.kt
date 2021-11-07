package lab1

private const val SPACE_SYMBOL = ' '
private const val SPACE_STRING = " "
private const val TAB_SYMBOL = '\t'
private const val EMPTY_STRING = ""

private val Punctuation = listOf('.', ',', '!', '?', ':', ';', '\"', '\'', '(', ')', '[', ']')

enum class Alignment {
    LEFT,
    RIGHT,
    CENTER,
    JUSTIFY,
    NONE
}

// region Char extensions

private fun Char.isPunctuation(): Boolean = Punctuation.contains(this)

// endregion

// region String extensions

private fun String.standardize(): String {
    if (isEmpty())
        return EMPTY_STRING

    var temp = replace(TAB_SYMBOL, SPACE_SYMBOL)         // replaces all tabs by spaces
    if (temp.all { it == SPACE_SYMBOL })
        return EMPTY_STRING
    temp = temp.replace("\\s+".toRegex(), SPACE_STRING) // removes multiple space symbols
    if (temp.last() == SPACE_SYMBOL)                    // removes right indent
        temp = temp.dropLast(1)

    return temp
}

private fun String.increaseLeftIndent(indentSize: Int): String {
    if (isEmpty())
        return EMPTY_STRING
    return SPACE_STRING.repeat(indentSize) + this
}

private fun String.dropLeftIndent(): String {
    if (isEmpty())
        return EMPTY_STRING
    return dropWhile { it.isWhitespace() }
}

private fun String.justify(width: Int): String {
    if (isEmpty())
        return EMPTY_STRING

    var difference = width - length
    val ret = StringBuilder(this)
    var index = 0
    var containWhitespaces = false
    var sameWhitespace = false // using for even distribution of spaces

    while (difference > 0) {
        if (ret[index].isWhitespace()) {
            if (!sameWhitespace) {
                containWhitespaces = true
                sameWhitespace = true
                ret.insert(index, SPACE_SYMBOL)
                index += 1
                difference -= 1
            }
        } else {
            sameWhitespace = false
        }

        if (index == ret.length - 1) {
            if (!containWhitespaces)
                break //string doesn't contain spaces, so it can't be justified
            index = 0
        } else
            index += 1
    }
    return ret.toString()
}

private fun String.getSplitPoint(width: Int): Int {
    if (length <= width)
        return -1
    // split point can't be:
    // greater than width
    // letter before punctuation symbol
    // part of punctuation sequence (groups up to 3 symbols in case of using long constructions: "example!!!!!!!")
    var splitIndex = -1
    var spareSplitIndex = 2 // in case of not finding splitIndex
    for (i in 2 until width) {
        if (this[i].isPunctuation()) {
            if (this[i - 1].isPunctuation() && this[i - 2].isPunctuation())
                splitIndex = i
        } else if (this[i].isWhitespace()) {
            splitIndex = i
        } else {
            if (!this[i + 1].isPunctuation())
                spareSplitIndex = i
        }
    }
    return if (splitIndex != -1)
        splitIndex + 1
    else
        spareSplitIndex + 1
}

// endregion

// region MutableList<String> extensions

fun MutableList<String>.alignText(alignment: Alignment, width: Int) {
    if (isEmpty())
        return
    if (width < 5)
        throw IllegalArgumentException("Width can't be less than 5")

    var range = 0 until size
    for (i in range)
        this[i] = this[i].standardize()
    wordWrap(width)
    range = 0 until size

    when (alignment) {
        Alignment.LEFT -> {
            for (i in range) {
                this[i] = this[i].dropLeftIndent()
            }
        }
        Alignment.RIGHT -> {
            for (i in range) {
                this[i] = this[i].increaseLeftIndent(width - this[i].length)
            }
        }
        Alignment.CENTER -> {
            for (i in range) {
                this[i] = this[i].increaseLeftIndent((width - this[i].length) / 2)
            }
        }
        Alignment.JUSTIFY -> {
            for (i in range)
                this[i] = this[i].justify(width)
        }
        else -> return
    }
}

private fun MutableList<String>.wordWrap(width: Int) {
    var index = 0
    var splitPoint: Int
    var substr: String

    // using do-while because list size may change during process
    do {
        splitPoint = this[index].getSplitPoint(width)
        if (splitPoint > 0) {
            // if wrapped substring exists
            substr = this[index].substring(splitPoint, this[index].lastIndex + 1)
            this[index] = this[index].substring(0, splitPoint).standardize()

            if (index + 1 == size)                        // end of list: create new line of wrapped part
                add(substr)
            else if (this[index + 1].isEmpty())           // next string is empty: substring will be inserted between current and next
                add(index + 1, substr)
            else if (this[index + 1].first() == SPACE_SYMBOL) // next string starts with indention: same as previous case
                add(index + 1, substr)
            else                                         // else: substring will be appended to next string
                this[index + 1] = substr + SPACE_SYMBOL + this[index + 1]
        }
        index += 1
    } while (index < size)
}

// endregion