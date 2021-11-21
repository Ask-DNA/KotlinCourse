package lab4

import org.junit.Assert.*
import org.junit.Test
import kotlin.test.assertFailsWith

class MatrixImplTest {

    @Test
    fun initTest() {
        val matrixArr = arrayOf(
            doubleArrayOf(1.0, 2.0, 3.0),
            doubleArrayOf(4.0, 5.0, 6.0)
        )
        val matrix: Matrix = MatrixImpl(matrixArr)
        val expected = "{1.0; 2.0; 3.0}" + System.lineSeparator() + "{4.0; 5.0; 6.0}" + System.lineSeparator()
        assertEquals(expected, matrix.toString())
    }

    @Test
    fun initExceptionTest() {
        var matrixArr = arrayOf(
            doubleArrayOf(1.0, 2.0, 3.0),
            doubleArrayOf(4.0, 5.0)
        )
        assertFailsWith<IllegalArgumentException> {
            MatrixImpl(matrixArr)
        }

        matrixArr = emptyArray()
        assertFailsWith<IllegalArgumentException> {
            MatrixImpl(matrixArr)
        }
    }

    @Test
    fun equalsTest() {
        var matrixArr = arrayOf(
            doubleArrayOf(1.0, 2.0, 3.0),
            doubleArrayOf(4.0, 5.0, 6.0)
        )
        val matrix1: Matrix = MatrixImpl(matrixArr)
        val matrix2: MutableMatrix = MutableMatrixImpl(matrixArr)
        matrixArr = arrayOf(
            doubleArrayOf(1.0, 2.0),
            doubleArrayOf(4.0, 5.0)
        )
        val matrix3: Matrix = MatrixImpl(matrixArr)

        assertTrue(matrix1 == matrix2)
        assertEquals(matrix1.hashCode(), matrix2.hashCode())
        assertFalse(matrix1 == matrix3)
        matrix2[1, 1] = 4.0
        assertFalse(matrix1 == matrix3)
    }

    @Test
    fun plusMinusTest() {
        var matrixArr = arrayOf(
            doubleArrayOf(1.0, 2.0, 3.0),
            doubleArrayOf(4.0, 5.0, 6.0)
        )
        val matrix1: Matrix = MatrixImpl(matrixArr)

        matrixArr = arrayOf(
            doubleArrayOf(2.0, 3.0, 4.0),
            doubleArrayOf(5.0, 6.0, 7.0)
        )
        val matrix2: Matrix = MatrixImpl(matrixArr)

        matrixArr = arrayOf(
            doubleArrayOf(3.0, 5.0, 7.0),
            doubleArrayOf(9.0, 11.0, 13.0)
        )
        val matrix3: Matrix = MatrixImpl(matrixArr)

        assertEquals(matrix3, matrix1 + matrix2)
        assertEquals(matrix1, matrix3 - matrix2)
    }

    @Test
    fun plusMinusExceptionTest() {
        var matrixArr = arrayOf(
            doubleArrayOf(1.0, 2.0, 3.0),
            doubleArrayOf(4.0, 5.0, 6.0)
        )
        val matrix1: Matrix = MatrixImpl(matrixArr)

        matrixArr = arrayOf(
            doubleArrayOf(2.0, 3.0),
            doubleArrayOf(5.0, 6.0)
        )
        val matrix2: Matrix = MatrixImpl(matrixArr)

        assertFailsWith<ArithmeticException> {
            matrix1 + matrix2
        }
        assertFailsWith<ArithmeticException> {
            matrix1 - matrix2
        }
    }

    @Test
    fun timesTest() {
        var matrixArr = arrayOf(
            doubleArrayOf(1.0, 2.0, 3.0),
            doubleArrayOf(4.0, 5.0, 6.0)
        )
        val matrix1: Matrix = MatrixImpl(matrixArr)

        matrixArr = arrayOf(
            doubleArrayOf(1.0, 2.0),
            doubleArrayOf(3.0, 4.0),
            doubleArrayOf(5.0, 6.0)
        )
        val matrix2: Matrix = MatrixImpl(matrixArr)

        matrixArr = arrayOf(
            doubleArrayOf(22.0, 28.0),
            doubleArrayOf(49.0, 64.0)
        )
        val matrix3: Matrix = MatrixImpl(matrixArr)

        assertEquals(matrix3, matrix1 * matrix2)
    }

    @Test
    fun timesExceptionTest() {
        var matrixArr = arrayOf(
            doubleArrayOf(1.0, 2.0, 3.0),
            doubleArrayOf(4.0, 5.0, 6.0),
        )
        val matrix1: Matrix = MatrixImpl(matrixArr)

        matrixArr = arrayOf(
            doubleArrayOf(2.0, 3.0),
            doubleArrayOf(5.0, 6.0)
        )
        val matrix2: Matrix = MatrixImpl(matrixArr)

        assertFailsWith<ArithmeticException> {
            matrix1 * matrix2
        }
    }

    @Test
    fun timesDivScalarTest() {
        var matrixArr = arrayOf(
            doubleArrayOf(1.0, 2.0),
            doubleArrayOf(4.0, 5.0)
        )
        val matrix1: Matrix = MatrixImpl(matrixArr)

        matrixArr = arrayOf(
            doubleArrayOf(15.0, 30.0),
            doubleArrayOf(60.0, 75.0)
        )
        val matrix2: Matrix = MatrixImpl(matrixArr)

        matrixArr = arrayOf(
            doubleArrayOf(0.5, 1.0),
            doubleArrayOf(2.0, 2.5)
        )
        val matrix3: Matrix = MatrixImpl(matrixArr)

        assertEquals(matrix2, matrix1 * 15.0)
        assertEquals(matrix3, matrix1 / 2.0)
    }
}