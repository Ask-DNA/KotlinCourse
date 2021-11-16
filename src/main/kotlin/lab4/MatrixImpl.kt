package lab4

open class MatrixImpl(_matrix: Array<DoubleArray>) : Matrix {
    protected open val matrix = copyFrom(_matrix)
    override val rows: Int
        get() = matrix.size
    override val columns: Int
        get() = matrix[0].size

    protected fun copyFrom(origin: Array<DoubleArray>): Array<DoubleArray> {
        if (origin.isEmpty())
            throw Exception("Matrix can't be empty")
        if (origin[0].isEmpty())
            throw Exception("Matrix can't be empty")
        origin.forEach {
            if (it.size != origin[0].size)
                throw Exception("Each row must contain the same number of elements")
        }

        val newMatrix = Array(origin.size) { DoubleArray(origin[0].size) }
        for (i in origin.indices)
            for (j in origin[0].indices)
                newMatrix[i][j] = origin[i][j]
        return newMatrix
    }

    protected fun copyFrom(origin: MatrixImpl): Array<DoubleArray> {
        return copyFrom(origin.matrix)
    }

    protected fun indexCheck(i: Int, j: Int) {
        if (i >= rows)
            throw IllegalArgumentException("Index i = $i must be lower than number of rows = $rows")
        if (i < 0)
            throw IllegalArgumentException("Index i = $i must be positive or zero")
        if (j >= columns)
            throw IllegalArgumentException("Index j = $j must be lower than number of columns = $columns")
        if (j < 0)
            throw IllegalArgumentException("Index j = $j must be positive or zero")
    }

    override fun get(i: Int, j: Int): Double {
        indexCheck(i, j)
        return matrix[i][j]
    }

    override fun plus(other: Matrix): Matrix {
        if (rows != other.rows || columns != other.columns)
            throw ArithmeticException("Matrices must be of the same dimensions")
        val newMatrix = Array(rows) { DoubleArray(columns) }

        for (i in 0 until rows)
            for (j in 0 until columns)
                newMatrix[i][j] = this[i, j] + other[i, j]
        return MatrixImpl(newMatrix)
    }

    override fun minus(other: Matrix): Matrix = this + (other * -1.0)

    override fun times(other: Matrix): Matrix {
        if (columns != other.rows)
            throw ArithmeticException(
                "Number of columns of the first matrix must be equal to number" +
                        "of rows of the second matrix"
            )
        val newMatrix = Array(rows) { DoubleArray(other.columns) }
        for (i in newMatrix.indices)
            for (j in newMatrix[0].indices) {
                newMatrix[i][j] = 0.0
                for (k in 0 until columns)
                    newMatrix[i][j] += this[i, k] * other[k, j]
            }
        return MatrixImpl(newMatrix)
    }

    override fun times(scalar: Double): Matrix {
        val newMatrix = Array(rows) { DoubleArray(columns) }
        for (i in 0 until rows)
            for (j in 0 until columns)
                newMatrix[i][j] = this[i, j] * scalar
        return MatrixImpl(newMatrix)
    }

    override fun div(scalar: Double): Matrix = this * (1.0 / scalar)

    override fun equals(other: Any?): Boolean {
        val otherM = other as? Matrix ?: return false
        if (rows != otherM.rows || columns != otherM.columns)
            return false
        for (i in matrix.indices)
            for (j in matrix[0].indices)
                if (this[i, j] != otherM[i, j])
                    return false
        return true
    }

    override fun hashCode(): Int {
        var hash = 0
        for (i in matrix.indices)
            for (j in matrix[0].indices)
                hash += this[i, j].toInt() + i + j
        return hash
    }

    override fun toString(): String {
        var ret = StringBuilder()
        for (i in 0 until rows) {
            ret.append('{')
            for (j in 0 until columns) {
                ret.append(this[i, j].toString() + "; ")
            }
            ret = StringBuilder(ret.dropLast(2))
            ret.append('}' + System.lineSeparator())
        }
        return ret.toString()
    }
}

class MutableMatrixImpl(_matrix: Array<DoubleArray>) : MutableMatrix, MatrixImpl(_matrix) {

    override var matrix: Array<DoubleArray> = copyFrom(_matrix)

    override fun set(i: Int, j: Int, value: Double) {
        indexCheck(i, j)
        matrix[i][j] = value
    }

    override fun plusAssign(other: Matrix) {
        matrix = copyFrom((this + other) as MatrixImpl)
    }

    override fun minusAssign(other: Matrix) {
        matrix = copyFrom((this - other) as MatrixImpl)
    }

    override fun timesAssign(other: Matrix) {
        matrix = copyFrom((this * other) as MatrixImpl)
    }

    override fun timesAssign(scalar: Double) {
        matrix = copyFrom((this * scalar) as MatrixImpl)
    }

    override fun divAssign(scalar: Double) {
        matrix = copyFrom((this / scalar) as MatrixImpl)
    }
}