package lab6

import lab3.Circle
import lab3.Shape
import lab3.ShapeFactory
import lab3.ShapeFactoryImpl

fun main() {
    val sf: ShapeFactory = ShapeFactoryImpl()
    val collector1 = ShapeCollector<Shape>()
    val collector2 = ShapeCollector<Circle>()
    for (i in 1..5)
        collector1.add(sf.createRandomShape())
    for (i in 1..5)
        collector2.add(sf.createRandomCircle())

    println("For shapes:")
    collector1.getAll().forEach { println(it) }
    println("Area asc sort:")
    collector1.getAllSorted(ShapeComparators.areaAsc).forEach {
        println(it.toString() + "; area = " + it.calcArea())
    }
    println("Area desc sort:")
    collector1.getAllSorted(ShapeComparators.areaDesc).forEach {
        println(it.toString() + "; area = " + it.calcArea())
    }
    println("Perimeter asc sort:")
    collector1.getAllSorted(ShapeComparators.perimeterAsc).forEach {
        println(it.toString() + "; perimeter = " + it.calcArea())
    }
    println("Perimeter desc sort:")
    collector1.getAllSorted(ShapeComparators.perimeterDesc).forEach {
        println(it.toString() + "; perimeter = " + it.calcArea())
    }

    println()
    println("For shapes:")
    collector2.getAll().forEach { println(it) }
    println("Radius asc sort:")
    collector2.getAllSorted(ShapeComparators.radiusAsc).forEach { println(it) }
    println("Radius desc sort:")
    collector2.getAllSorted(ShapeComparators.radiusDesc).forEach { println(it) }
}