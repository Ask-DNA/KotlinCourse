package lab1

import org.junit.Assert.assertEquals
import org.junit.Test

class TextEditorKtTest {

    @Test
    fun alignNone() {
        val text = mutableListOf(
            "   \tABC   AB ", // standardisation case: removing multispaces, tabs and right indent
            "ABC AB",      // standardisation case: nothing
            "    ",
            " Word   word",  // wrapping case: nothing
            "   Word   longword", // wrapping case: wrapping with creating new string
            " ReallyLongWord", // wrapping case: word wrapping
            " Word abc!!!",    // wrapping case: punctuation wrapping with connecting
            "abc      "
        )
        val expected = mutableListOf(
            " ABC AB",
            "ABC AB",
            "",
            " Word word",
            " Word",
            "longword",
            " ReallyLon",
            "gWord",
            " Word",
            "abc!!! abc"
        )
        text.alignText(Alignment.NONE, 10)
        assertEquals(expected, text)
    }

    @Test
    fun alignLeft() {
        val text = mutableListOf(
            "   \tABC   AB ",
            "ABC AB",
            "    ",
            " Word   word",
            "   Word   longword",
            " Word abc!!!",
            "abc    "
        )
        val expected = mutableListOf(
            "ABC AB",
            "ABC AB",
            "",
            "Word word",
            "Word",
            "longword",
            "Word",
            "abc!!! abc"
        )
        text.alignText(Alignment.LEFT, 10)
        assertEquals(expected, text)
    }

    @Test
    fun alignRight() {
        val text = mutableListOf(
            "   \tABC   AB ",
            "ABC AB",
            "    ",
            " Word   word",
            "   Word   longword",
            " Word abc!!!",
            "abc    "
        )
        val expected = mutableListOf(
            "    ABC AB",
            "    ABC AB",
            "",
            " Word word",
            "      Word",
            "  longword",
            "      Word",
            "abc!!! abc"
        )
        text.alignText(Alignment.RIGHT, 10)
        assertEquals(expected, text)
    }

    @Test
    fun alignCenter() {
        val text = mutableListOf(
            "   \tABC   AB ",
            "ABC AB",
            "    ",
            "Word   word",
            "   Word   longword",
            " Word abc!!!",
            "abc    "
        )
        val expected = mutableListOf(
            "  ABC AB",
            "  ABC AB",
            "",
            "Word word",
            "   Word",
            " longword",
            "   Word",
            "abc!!! abc"
        )
        text.alignText(Alignment.CENTER, 10)
        assertEquals(expected, text)
    }

    @Test
    fun alignJustify() {
        val text = mutableListOf(
            "   \tABC   AB ",
            "ABC AB",
            "    ",
            "Word   word",
            "   Word   longword",
            " Word abc!!!",
            "abc    "
        )
        val expected = mutableListOf(
            "   ABC  AB",
            "ABC     AB",
            "",
            "Word  word",
            "      Word",
            "longword", // won't justify strings if there is no whitespaces
            "      Word",
            "abc!!! abc"
        )
        text.alignText(Alignment.JUSTIFY, 10)
        assertEquals(expected, text)
    }

    @Test
    fun alignException() {
        val text = mutableListOf("word")
        val expected = "Width can't be less than 5"
        var actual = ""
        try {
            text.alignText(Alignment.LEFT, 4)
        } catch (e: Exception) {
            actual = e.message ?: ""
        }
        assertEquals(expected, actual)
    }
}