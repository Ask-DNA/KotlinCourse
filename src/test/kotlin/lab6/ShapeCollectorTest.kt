package lab6

import lab3.*
import org.junit.Assert.assertEquals
import org.junit.Test
import java.math.BigDecimal

class ShapeCollectorTest {

    @Test
    fun areaSort() {
        val collector = ShapeCollector<Shape>()
        val list = listOf(
            Square(BigDecimal("4")),
            Rectangle(BigDecimal("3"), BigDecimal("4")),
            Triangle(
                Point(BigDecimal.ZERO, BigDecimal.ZERO),
                Point(BigDecimal.ZERO, BigDecimal("3")),
                Point(BigDecimal("4"), BigDecimal.ZERO)
            ),
            Circle(BigDecimal("1"), Point(BigDecimal.ZERO, BigDecimal.ZERO))
        )
        collector.addAll(list)
        val expected = listOf(list[3], list[2], list[1], list[0])
        assertEquals(expected, collector.getAllSorted(ShapeComparators.areaAsc))
        assertEquals(expected.reversed(), collector.getAllSorted(ShapeComparators.areaDesc))
    }

    @Test
    fun perimeterSort() {
        val collector = ShapeCollector<Shape>()
        val list = listOf(
            Square(BigDecimal("4")),
            Rectangle(BigDecimal("3"), BigDecimal("4")),
            Triangle(
                Point(BigDecimal.ZERO, BigDecimal.ZERO),
                Point(BigDecimal.ZERO, BigDecimal("3")),
                Point(BigDecimal("4"), BigDecimal.ZERO)
            ),
            Circle(BigDecimal("1"), Point(BigDecimal.ZERO, BigDecimal.ZERO))
        )
        collector.addAll(list)
        val expected = listOf(list[3], list[2], list[1], list[0])
        assertEquals(expected, collector.getAllSorted(ShapeComparators.perimeterAsc))
        assertEquals(expected.reversed(), collector.getAllSorted(ShapeComparators.perimeterDesc))
    }

    @Test
    fun radiusSort() {
        val collector = ShapeCollector<Circle>()
        val list = listOf(
            Circle(BigDecimal("1"), Point(BigDecimal.ZERO, BigDecimal.ZERO)),
            Circle(BigDecimal("2"), Point(BigDecimal.ZERO, BigDecimal.ZERO)),
            Circle(BigDecimal("3"), Point(BigDecimal.ZERO, BigDecimal.ZERO)),
            Circle(BigDecimal("4"), Point(BigDecimal.ZERO, BigDecimal.ZERO))
        )
        collector.addAll(list)
        val expected = listOf(list[0], list[1], list[2], list[3])
        assertEquals(expected, collector.getAllSorted(ShapeComparators.radiusAsc))
        assertEquals(expected.reversed(), collector.getAllSorted(ShapeComparators.radiusDesc))
    }

    @Test
    fun getAllByClass() {
        val collector = ShapeCollector<Shape>()
        val list = listOf(
            Square(BigDecimal("4")),
            Rectangle(BigDecimal("3"), BigDecimal("4")),
            Circle(BigDecimal("1"), Point(BigDecimal.ZERO, BigDecimal.ZERO)),
            Circle(BigDecimal("2"), Point(BigDecimal.ZERO, BigDecimal.ZERO)),
            Triangle(
                Point(BigDecimal.ZERO, BigDecimal.ZERO),
                Point(BigDecimal.ZERO, BigDecimal("3")),
                Point(BigDecimal("4"), BigDecimal.ZERO)
            ),
            Circle(BigDecimal("3"), Point(BigDecimal.ZERO, BigDecimal.ZERO)),
            Circle(BigDecimal("4"), Point(BigDecimal.ZERO, BigDecimal.ZERO))
        )
        collector.addAll(list)
        val expected = listOf(list[2], list[3], list[5], list[6])
        assertEquals(expected, collector.getAllByClass(Circle::class.java))
        assertEquals(emptyList<Shape>(), collector.getAllByClass(ShapeCollector::class.java))
    }
}