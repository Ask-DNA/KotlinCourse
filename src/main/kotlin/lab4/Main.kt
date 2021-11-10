package lab4

fun main() {
    var matrix = arrayOf(
        doubleArrayOf(1.0, 2.0, 3.0),
        doubleArrayOf(4.0, 5.0, 6.0)
    )
    val m1: Matrix = MatrixImpl(matrix)

    matrix = arrayOf(
        doubleArrayOf(1.0, 2.0),
        doubleArrayOf(3.0, 4.0),
        doubleArrayOf(5.0, 6.0)
    )
    val m2: Matrix = MatrixImpl(matrix)

    matrix = arrayOf(
        doubleArrayOf(6.0, 5.0),
        doubleArrayOf(4.0, 3.0),
        doubleArrayOf(2.0, 1.0)
    )
    val m3: MutableMatrix = MutableMatrixImpl(matrix)

    println((m1 * 3.0).toString())
    println((m1 / 2.0).toString())
    println((m1 * m2).toString())

    println((m2 + m3).toString())
    println((m2 - m3).toString())

    m3 *= m1
    println(m3.toString())
}