package lab3

import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode

// Using BigDecimal to minimize loss of precision

class Point(_x: BigDecimal, _y: BigDecimal) {

    // Using class instead of data class to be able to override equals method

    val x: BigDecimal
    val y: BigDecimal

    init{
        // Setting scale explicitly to avoid errors due to rounding
        x = _x.setScale(4, RoundingMode.DOWN)
        y = _y.setScale(4, RoundingMode.DOWN)
    }

    override fun equals(other: Any?): Boolean {
        // Using compareTo because BigDecimal.equals returns false for same values in case of different scale
        val otherP = other as? Point ?: return false
        return x.compareTo(otherP.x) == 0 && y.compareTo(otherP.y) == 0
    }

    override fun hashCode(): Int = x.hashCode() + y.hashCode()

    override fun toString(): String = "($x; $y)"
}

class Line(val p1: Point, val p2: Point) {

    val length: BigDecimal

    // Line equation: y = slope * x + b
    // Presented as a fraction to avoid rounding errors in operations below
    private var slopeNumerator: BigDecimal
    private var slopeDenominator: BigDecimal
    private val slopeSign: Boolean

    init {
        if (p1 == p2)
            throw IllegalArgumentException("Line must be specified by two different points")
        length = ((p2.x - p1.x).pow(2) + (p2.y - p1.y).pow(2)).sqrt(MathContext.DECIMAL32)

        slopeNumerator = p1.y - p2.y
        slopeDenominator = p1.x - p2.x
        val tempSign: Boolean = slopeNumerator.compareTo(BigDecimal.ZERO) == -1
        slopeSign = (tempSign != (slopeDenominator.compareTo(BigDecimal.ZERO) == -1))

        slopeNumerator = slopeNumerator.abs()
        slopeDenominator = slopeDenominator.abs()
    }

    fun parallel(line: Line): Boolean {
        // Lines are parallel if their slopes are equal
        return if (slopeDenominator.compareTo(BigDecimal.ZERO) == 0)
            line.slopeDenominator.compareTo(BigDecimal.ZERO) == 0
        else if (slopeNumerator.compareTo(BigDecimal.ZERO) == 0)
            line.slopeNumerator.compareTo(BigDecimal.ZERO) == 0
        else
        {
            if (line.slopeDenominator.compareTo(BigDecimal.ZERO) == 0 || line.slopeNumerator.compareTo(BigDecimal.ZERO) == 0)
                false
            else {
                val temp1 = slopeNumerator.divide(line.slopeNumerator, MathContext.DECIMAL32)
                val temp2 = slopeDenominator.divide(line.slopeDenominator, MathContext.DECIMAL32)
                (slopeSign == line.slopeSign) && (temp1 == temp2)
            }
        }
    }

    fun perpendicular(line: Line): Boolean {
        // Lines are perpendicular if their slopes' product is -1
        return if (slopeDenominator.compareTo(BigDecimal.ZERO) == 0) {
            if (line.slopeDenominator.compareTo(BigDecimal.ZERO) == 0)
                false
            else
                line.slopeNumerator.compareTo(BigDecimal.ZERO) == 0
        }
        else {
            if (line.slopeDenominator.compareTo(BigDecimal.ZERO) == 0)
                slopeNumerator.compareTo(BigDecimal.ZERO) == 0
            else {
                val tempSign = slopeSign != line.slopeSign
                val temp1 = slopeNumerator * line.slopeNumerator
                val temp2 = slopeDenominator * line.slopeDenominator
                tempSign && temp1.compareTo(temp2) == 0
            }
        }
    }

    fun inLine(p: Point): Boolean {
        return if (p == p1 || p == p2)
            true
        else
            parallel(Line(p1, p))
    }
}
