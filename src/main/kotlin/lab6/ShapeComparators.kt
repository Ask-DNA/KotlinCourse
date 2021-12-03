package lab6

import lab3.Circle
import lab3.Shape

object ShapeComparators {
    val perimeterAsc = compareBy<Shape> { it.calcPerimeter() }
    val areaAsc = compareBy<Shape> { it.calcArea() }
    val radiusAsc = compareBy<Circle> { it.radius }
    val perimeterDesc = compareByDescending<Shape> { it.calcPerimeter() }
    val areaDesc = compareByDescending<Shape> { it.calcArea() }
    val radiusDesc = compareByDescending<Circle> { it.radius }
}