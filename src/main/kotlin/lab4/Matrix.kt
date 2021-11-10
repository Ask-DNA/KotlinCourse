package lab4

interface Matrix {
    val rows: Int
    val columns: Int
    operator fun get(i: Int, j: Int): Double
    operator fun plus(other: Matrix): Matrix
    operator fun minus(other: Matrix): Matrix
    operator fun times(other: Matrix): Matrix
    operator fun times(scalar: Double): Matrix
    operator fun div(scalar: Double): Matrix
}

interface MutableMatrix : Matrix {
    operator fun set(i: Int, j: Int, value: Double)
    operator fun plusAssign(other: Matrix)
    operator fun minusAssign(other: Matrix)
    operator fun timesAssign(other: Matrix)
    operator fun timesAssign(scalar: Double)
    operator fun divAssign(scalar: Double)
}