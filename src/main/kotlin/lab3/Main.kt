package lab3

import java.math.BigDecimal
import java.math.RoundingMode

fun main() {
    val factory: ShapeFactory = ShapeFactoryImpl()
    val shapes = listOf (
        factory.createCircle(BigDecimal(50)),
        factory.createRectangle(BigDecimal(40), BigDecimal(80)),
        factory.createSquare(BigDecimal(50)),
        factory.createTriangle(
            originPoint,
            Point(BigDecimal(30),BigDecimal(60)),
            Point(BigDecimal.ZERO, BigDecimal(90))
        ),
        factory.createRandomCircle(),
        factory.createRandomRectangle(),
        factory.createRandomSquare(),
        factory.createRandomTriangle(),
        factory.createRandomShape()
    )

    val maxAreaShape = shapes.sortedWith(compareBy { it.calcArea() }).last()
    val minAreaShape = shapes.sortedWith(compareBy { it.calcArea() }).first()
    val maxPerimeterShape = shapes.sortedWith(compareBy { it.calcPerimeter() }).last()
    val minPerimeterShape = shapes.sortedWith(compareBy { it.calcPerimeter() }).first()
    var sumArea = BigDecimal.ZERO
    var sumPerimeter = BigDecimal.ZERO
    shapes.forEach {
        sumArea += it.calcArea().setScale(4, RoundingMode.DOWN)
        sumPerimeter += it.calcPerimeter().setScale(4, RoundingMode.DOWN)
    }

    println("For shapes:")
    shapes.forEach {
        println(it.toString())
        println("Area is ${it.calcArea().setScale(4, RoundingMode.DOWN)}")
        println("Perimeter is ${it.calcPerimeter().setScale(4, RoundingMode.DOWN)}")
        println()
    }
    println("Summary area is $sumArea, summary perimeter is $sumPerimeter")
    println("Max area shape is $maxAreaShape")
    println("Min area shape is $minAreaShape")
    println("Max perimeter shape is $maxPerimeterShape")
    println("Min perimeter shape is $minPerimeterShape")
}