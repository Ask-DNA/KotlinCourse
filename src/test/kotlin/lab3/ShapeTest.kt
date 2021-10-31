package lab3

import org.junit.Assert.*
import org.junit.Test
import java.math.BigDecimal
import java.math.MathContext
import kotlin.math.PI

class ShapeTest {

    @Test
    fun circleTest() {
        val radius = BigDecimal("10.5")
        val centre = Point(BigDecimal("5.3"), BigDecimal("-3.16"))

        val shape = Circle(radius, centre)
        val perimeter = BigDecimal("2") * BigDecimal(PI) * radius
        val area = BigDecimal(PI) * radius.pow(2)

        assertTrue(perimeter.compareTo(shape.calcPerimeter()) == 0)
        assertTrue(area.compareTo(shape.calcArea()) == 0)
    }

    @Test
    fun circleExceptionTest() {
        var actual = ""
        val expected = "Radius must be positive"
        try {
            Circle(BigDecimal.ZERO)
        }
        catch (e: Exception) {
            actual = e.message?:""
        }
        assertEquals(expected, actual)
    }

    @Test
    fun rectangleTest() {
        val p1 = Point(BigDecimal("-3.43"), BigDecimal("1.11"))
        val p2 = Point(BigDecimal("-1.43"), BigDecimal("3.11"))
        val p3 = Point(BigDecimal("1.57"), BigDecimal("0.11"))
        val p4 = Point(BigDecimal("-0.43"), BigDecimal("-1.89"))

        var shape = Rectangle(p1, p2, p3, p4)
        var perimeter = Line(p1, p2).length * BigDecimal("2") + Line(p2, p3).length * BigDecimal("2")
        var area = Line(p1, p2).length * Line(p2, p3).length

        assertTrue(perimeter.compareTo(shape.calcPerimeter()) == 0)
        assertTrue(area.compareTo(shape.calcArea()) == 0)

        shape = Rectangle(BigDecimal.TEN, BigDecimal("20.14"))
        perimeter = BigDecimal.TEN * BigDecimal("2") + BigDecimal("20.14") * BigDecimal("2")
        area = BigDecimal.TEN * BigDecimal("20.14")

        assertTrue(perimeter.compareTo(shape.calcPerimeter()) == 0)
        assertTrue(area.compareTo(shape.calcArea()) == 0)
    }

    @Test
    fun rectangleExceptionTest() {
        val p1 = Point(BigDecimal("-3.43"), BigDecimal("1.11"))
        val p2 = Point(BigDecimal("-1.43"), BigDecimal("3.11"))
        val p3 = Point(BigDecimal("1.57"), BigDecimal("0.11"))
        val p4 = Point(BigDecimal("-0.43"), BigDecimal("1.89"))

        var actual = ""
        var expected = "All angles must be right"
        try {
            Rectangle(p1, p2, p3, p4)
        }
        catch (e: Exception) {
            actual = e.message?:""
        }
        assertEquals(expected, actual)

        expected = "Line must be specified by two different points"
        try {
            Rectangle(p1, p2, p3, p3)
        }
        catch (e: Exception) {
            actual = e.message?:""
        }
        assertEquals(expected, actual)

        expected = "Sides must be greater than 0"
        try {
            Rectangle(BigDecimal.TEN, BigDecimal("-1"))
        }
        catch (e: Exception) {
            actual = e.message?:""
        }
        assertEquals(expected, actual)
    }

    @Test
    fun squareTest() {
        val p1 = Point(BigDecimal("-2.43"), BigDecimal("0.11"))
        val p2 = Point(BigDecimal("-0.43"), BigDecimal("2.11"))
        val p3 = Point(BigDecimal("1.57"), BigDecimal("0.11"))
        val p4 = Point(BigDecimal("-0.43"), BigDecimal("-1.89"))

        var shape = Square(p1, p2, p3, p4)
        var perimeter = Line(p1, p2).length * BigDecimal("4")
        var area = Line(p1, p2).length.pow(2)

        assertTrue(perimeter.compareTo(shape.calcPerimeter()) == 0)
        assertTrue(area.compareTo(shape.calcArea()) == 0)

        shape = Square(BigDecimal("20.14"))
        perimeter = BigDecimal("20.14") * BigDecimal("4")
        area = BigDecimal("20.14").pow(2)

        assertTrue(perimeter.compareTo(shape.calcPerimeter()) == 0)
        assertTrue(area.compareTo(shape.calcArea()) == 0)
    }

    @Test
    fun squareExceptionTest() {
        val p1 = Point(BigDecimal("-3.43"), BigDecimal("1.11"))
        val p2 = Point(BigDecimal("-1.43"), BigDecimal("3.11"))
        val p3 = Point(BigDecimal("1.57"), BigDecimal("0.11"))
        val p4 = Point(BigDecimal("-0.43"), BigDecimal("-1.89"))

        var actual = ""
        var expected = "All angles must be right"
        try {
            Square(p1, p2, p3, Point(BigDecimal.ZERO, BigDecimal.ZERO))
        }
        catch (e: Exception) {
            actual = e.message?:""
        }
        assertEquals(expected, actual)

        actual = ""
        expected = "Sides must be equal"
        try {
            Square(p1, p2, p3, p4)
        }
        catch (e: Exception) {
            actual = e.message?:""
        }
        assertEquals(expected, actual)

        expected = "Line must be specified by two different points"
        try {
            Square(p1, p2, p3, p3)
        }
        catch (e: Exception) {
            actual = e.message?:""
        }
        assertEquals(expected, actual)

        expected = "Sides must be greater than 0"
        try {
            Square(BigDecimal("-1"))
        }
        catch (e: Exception) {
            actual = e.message?:""
        }
        assertEquals(expected, actual)
    }

    @Test
    fun triangleTest() {
        val p1 = Point(BigDecimal("-2.43"), BigDecimal("0.11"))
        val p2 = Point(BigDecimal("-0.43"), BigDecimal("2.11"))
        val p3 = Point(BigDecimal("2.57"), BigDecimal("-0.89"))

        var shape = Triangle(p1, p2, p3)
        var perimeter = Line(p1, p2).length + Line(p2, p3).length + Line(p3, p1).length

        val p = perimeter / BigDecimal(2)
        val l1 = Line(p1, p2).length
        val l2 = Line(p2, p3).length
        val l3 = Line(p3, p1).length
        var area = (p * (p - l1) * (p - l2) * (p - l3)).sqrt(MathContext.DECIMAL32)

        assertTrue(perimeter.compareTo(shape.calcPerimeter()) == 0)
        assertTrue(area.compareTo(shape.calcArea()) == 0)
    }

    @Test
    fun triangleExceptionTest() {
        val p1 = Point(BigDecimal("-3.43"), BigDecimal("1.11"))
        val p2 = Point(BigDecimal("-1.43"), BigDecimal("3.11"))

        var actual = ""
        var expected = "Triangle must be specified by three different points, which must not be inline"
        try {
            Triangle(p1, p2, p2)
        }
        catch (e: Exception) {
            actual = e.message?:""
        }
        assertEquals(expected, actual)
    }
}