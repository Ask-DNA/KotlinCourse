package lab3

import java.math.BigDecimal
import java.math.MathContext
import kotlin.math.PI

val originPoint = Point(BigDecimal.ZERO, BigDecimal.ZERO)

interface Shape {
    fun calcPerimeter(): BigDecimal
    fun calcArea(): BigDecimal
}

class Circle(val radius: BigDecimal, val centre: Point = originPoint): Shape {

    init {
        if (radius.compareTo(BigDecimal.ZERO) != 1)
            throw IllegalArgumentException("Radius must be positive")
    }

    override fun calcPerimeter(): BigDecimal = BigDecimal(2) * BigDecimal(PI) * radius

    override fun calcArea(): BigDecimal = BigDecimal(PI) * radius.pow(2)

    override fun toString(): String = "Circle: centrePoint$centre; radius = $radius"
}

open class Rectangle(val p1: Point, val p2: Point, val p3: Point, val p4: Point): Shape {

    init {
        if (!isRectangle())
            throw IllegalArgumentException("All angles must be right")
    }

    private fun isRectangle(): Boolean {
        val line1 = Line(p1, p2)
        val line2 = Line(p2, p3)
        val line3 = Line(p3, p4)
        val line4 = Line(p4, p1)
        if (!line1.parallel(line3) || !line2.parallel(line4) || !line1.perpendicular(line2))
            return false
        return true
    }

    constructor(height: BigDecimal, width: BigDecimal, lowerLeft: Point = originPoint): this(
        lowerLeft,
        Point(lowerLeft.x,lowerLeft.y + height),
        Point(lowerLeft.x + width,lowerLeft.y + height),
        Point(lowerLeft.x + width, lowerLeft.y)
    ) {
        if (height.compareTo(BigDecimal.ZERO) != 1 || width.compareTo(BigDecimal.ZERO) != 1)
            throw IllegalArgumentException("Sides must be greater than 0")
    }

    override fun calcPerimeter(): BigDecimal = Line(p1, p2).length * BigDecimal(2) + Line(p2, p3).length * BigDecimal(2)

    override fun calcArea(): BigDecimal = Line(p1, p2).length * Line(p2, p3).length

    override fun toString(): String = "Rectangle: p1$p1; p2$p2; p3$p3; p4$p4"
}

class Square(p1: Point, p2: Point, p3: Point, p4: Point): Rectangle(p1, p2, p3, p4) {

    init {
        if (!isSquare())
            throw IllegalArgumentException("Sides must be equal")
    }

    private fun isSquare(): Boolean {
        if (Line(p1, p2).length.compareTo(Line(p2, p3).length) != 0)
            return false
        return true
    }

    constructor(side: BigDecimal, lowerLeft: Point = originPoint): this(
        lowerLeft,
        Point(lowerLeft.x, lowerLeft.y + side),
        Point(lowerLeft.x + side, lowerLeft.y + side),
        Point(lowerLeft.x + side, lowerLeft.y)
    ) {
        if (side.compareTo(BigDecimal.ZERO) != 1)
            throw IllegalArgumentException("Sides must be greater than 0")
    }

    override fun toString(): String = "Square: : p1$p1; p2$p2; p3$p3; p4$p4"
}

class Triangle(val p1: Point, val p2: Point, val p3: Point): Shape {

    init {
        val exception = IllegalArgumentException("Triangle must be specified by three different points, which must not be inline")
        try {
            if (!isTriangle())
                throw exception
        }
        catch (e: Exception) {
            throw exception
        }
    }

    private fun isTriangle(): Boolean {
        if (Line(p1, p2).inLine(p3))
            return false
        return true
    }

    override fun calcPerimeter(): BigDecimal = Line(p1, p2).length + Line(p2, p3).length + Line(p3, p1).length

    override fun calcArea(): BigDecimal {
        val p = calcPerimeter() / BigDecimal(2)
        val l1 = Line(p1, p2).length
        val l2 = Line(p2, p3).length
        val l3 = Line(p3, p1).length
        return (p * (p - l1) * (p - l2) * (p - l3)).sqrt(MathContext.DECIMAL32)
    }

    override fun toString(): String = "Triangle: : p1$p1; p2$p2; p3$p3"
}

