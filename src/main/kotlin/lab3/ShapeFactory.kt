package lab3

import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.random.Random

private fun Random.nextBigDecimal(from: Double = 0.1, until: Double = 100.0, scale: Int = 2): BigDecimal =
    BigDecimal(nextDouble(from, until)).setScale(scale, RoundingMode.DOWN)

private fun Random.nextPoint(from: Double = -100.0, until: Double = 100.0, scale: Int = 2): Point =
    Point(nextBigDecimal(from, until, scale), nextBigDecimal(from, until, scale))

interface ShapeFactory {
    fun createCircle(radius: BigDecimal, centre: Point = originPoint): Circle
    fun createRectangle(p1: Point, p2: Point, p3: Point, p4: Point): Rectangle
    fun createRectangle(height: BigDecimal, width: BigDecimal, lowerLeft: Point = originPoint): Rectangle
    fun createSquare(p1: Point, p2: Point, p3: Point, p4: Point): Square
    fun createSquare(side: BigDecimal, lowerLeft: Point = originPoint): Square
    fun createTriangle(p1: Point, p2: Point, p3: Point): Triangle

    fun createRandomCircle(): Circle
    fun createRandomRectangle(): Rectangle
    fun createRandomSquare(): Square
    fun createRandomTriangle(): Triangle

    fun createRandomShape(): Shape
}

class ShapeFactoryImpl : ShapeFactory {
    override fun createCircle(radius: BigDecimal, centre: Point): Circle = Circle(radius, centre)
    override fun createRectangle(p1: Point, p2: Point, p3: Point, p4: Point): Rectangle = Rectangle(p1, p2, p3, p4)
    override fun createRectangle(height: BigDecimal, width: BigDecimal, lowerLeft: Point): Rectangle =
        Rectangle(height, width, lowerLeft)

    override fun createSquare(p1: Point, p2: Point, p3: Point, p4: Point): Square = Square(p1, p2, p3, p4)
    override fun createSquare(side: BigDecimal, lowerLeft: Point): Square = Square(side, lowerLeft)
    override fun createTriangle(p1: Point, p2: Point, p3: Point): Triangle = Triangle(p1, p2, p3)

    override fun createRandomCircle(): Circle {
        val radius = Random.nextBigDecimal()
        val centre = Random.nextPoint()
        return Circle(radius, centre)
    }

    override fun createRandomRectangle(): Rectangle {
        val height = Random.nextBigDecimal()
        val width = Random.nextBigDecimal()
        val lowerLeft = Random.nextPoint()
        return Rectangle(height, width, lowerLeft)
    }

    override fun createRandomSquare(): Square {
        val side = Random.nextBigDecimal()
        val lowerLeft = Random.nextPoint()
        return Square(side, lowerLeft)
    }

    override fun createRandomTriangle(): Triangle {
        var p1: Point
        var p2: Point
        var p3: Point
        do {
            p1 = Random.nextPoint()
            p2 = Random.nextPoint()
            p3 = Random.nextPoint()
            if (p1 != p2) {
                if (!Line(p1, p2).inLine(p3))
                    break
            }
        } while (true)
        return Triangle(p1, p2, p3)
    }

    override fun createRandomShape(): Shape {
        return when (Random.nextInt(0, 4)) {
            0 -> createRandomCircle()
            1 -> createRandomRectangle()
            2 -> createRandomSquare()
            else -> createRandomTriangle()
        }
    }
}