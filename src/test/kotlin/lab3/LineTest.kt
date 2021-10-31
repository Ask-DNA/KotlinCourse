package lab3

import org.junit.Assert.*
import org.junit.Test
import java.math.BigDecimal
import java.math.RoundingMode

class LineTest {

    @Test
    fun lengthTest() {
        var actual = Line(
            Point(BigDecimal("-1.45"),BigDecimal("-1")),
            Point(BigDecimal("-1.45"), BigDecimal("9.19"))
        ).length.setScale(2, RoundingMode.HALF_EVEN)
        var expected = BigDecimal("10.19")
        assertTrue(expected.compareTo(actual) == 0)

        actual = Line(
            Point(BigDecimal("-1"),BigDecimal("-1.14")),
            Point(BigDecimal("3.9"), BigDecimal("-1.14"))
        ).length.setScale(1, RoundingMode.HALF_EVEN)
        expected = BigDecimal("4.9")
        assertTrue(expected.compareTo(actual) == 0)

        actual = Line(
            Point(BigDecimal(-1),BigDecimal(-1.14)),
            Point(BigDecimal(3), BigDecimal(1.86))
        ).length.setScale(0, RoundingMode.HALF_EVEN)
        expected = BigDecimal("5")
        assertTrue(expected.compareTo(actual) == 0)
    }

    @Test
    fun parallelTest() {
        var line1 = Line(
            Point(BigDecimal("-1.45"),BigDecimal("-1")),
            Point(BigDecimal("-1.45"), BigDecimal("9.19"))
        )
        var line2 = Line(
            Point(BigDecimal("0.95"),BigDecimal("0.1")),
            Point(BigDecimal("0.95"), BigDecimal("9.19"))
        )
        var line3 = Line(
            Point(BigDecimal("-1.45"),BigDecimal("-1")),
            Point(BigDecimal("-2.45"), BigDecimal("9.19"))
        )
        assertTrue(line1.parallel(line2))
        assertFalse(line1.parallel(line3))

        line1 = Line(
            Point(BigDecimal("-1"),BigDecimal("-1.14")),
            Point(BigDecimal("3.9"), BigDecimal("-1.14"))
        )
        line2 = Line(
            Point(BigDecimal("0.95"),BigDecimal("2.1")),
            Point(BigDecimal("1.5"), BigDecimal("2.1"))
        )
        line3 = Line(
            Point(BigDecimal("-1.45"),BigDecimal("-1")),
            Point(BigDecimal("-2.45"), BigDecimal("9.19"))
        )
        assertTrue(line1.parallel(line2))
        assertFalse(line1.parallel(line3))

        line1 = Line(
            Point(BigDecimal("-1"),BigDecimal("-1.14")),
            Point(BigDecimal("3"), BigDecimal("1.86"))
        )
        line2 = Line(
            Point(BigDecimal.ZERO, BigDecimal.ZERO),
            Point(BigDecimal("4"), BigDecimal("3"))
        )
        line3 = Line(
            Point(BigDecimal.ZERO, BigDecimal.ZERO),
            Point(BigDecimal("3"), BigDecimal("4"))
        )
        assertTrue(line1.parallel(line2))
        assertFalse(line1.parallel(line3))
    }

    @Test
    fun perpendicularTest() {
        var line1 = Line(
            Point(BigDecimal("-1.45"),BigDecimal("-1")),
            Point(BigDecimal("-1.45"), BigDecimal("9.19"))
        )
        var line2 = Line(
            Point(BigDecimal("-1"),BigDecimal("-1.14")),
            Point(BigDecimal("3.9"), BigDecimal("-1.14"))
        )
        var line3 = Line(
            Point(BigDecimal("-1.45"),BigDecimal("-1")),
            Point(BigDecimal("-2.45"), BigDecimal("9.19"))
        )
        assertTrue(line1.perpendicular(line2))
        assertFalse(line1.perpendicular(line3))

        line1 = Line(
            Point(BigDecimal("-1"),BigDecimal("-1.14")),
            Point(BigDecimal("3"), BigDecimal("1.86"))
        )
        line2 = Line(
            Point(BigDecimal.ZERO,BigDecimal.ZERO),
            Point(BigDecimal("-3"), BigDecimal("4"))
        )
        line3 = Line(
            Point(BigDecimal.ZERO,BigDecimal.ZERO),
            Point(BigDecimal("3.3"), BigDecimal("4"))
        )
        assertTrue(line1.perpendicular(line2))
        assertFalse(line1.perpendicular(line3))
    }

    @Test
    fun inLineTest() {
        val line1 = Line(
            Point(BigDecimal.ZERO,BigDecimal.ZERO),
            Point(BigDecimal("2"), BigDecimal("3"))
        )
        val line2 = Line(
            Point(BigDecimal("-1"),BigDecimal("6")),
            Point(BigDecimal("1"), BigDecimal("4"))
        )
        val line3 = Line(
            Point(BigDecimal("-2"),BigDecimal("1")),
            Point(BigDecimal("-1"), BigDecimal("3"))
        )
        val p = Point(BigDecimal("2"), BigDecimal("3"))
        assertTrue(line1.inLine(p))
        assertTrue(line2.inLine(p))
        assertFalse(line3.inLine(p))
    }

    @Test
    fun exceptionTest() {
        val expected = "Line must be specified by two different points"
        var actual = ""
        try {
            Line(Point(BigDecimal.ONE, BigDecimal.ONE), Point(BigDecimal.ONE, BigDecimal.ONE))
        }
        catch (e: Exception) {
            actual = e.message?:""
        }
        assertEquals(expected, actual)
    }
}