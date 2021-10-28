package lab2

import kotlin.math.*

// region constants
val parsingErrorException = Exception("Parsing error")
val calculationErrorException = Exception("Calculation error")

const val SPACE_SYMBOL = ' '
const val OPEN_BRACKET = '('
const val CLOSE_BRACKET = ')'

val functionsList = listOf("abs", "sqrt", "lg", "ln", "sin", "cos", "tg",
    "ctg", "asin", "acos", "atg", "actg")

const val PI_STR = "pi"
const val E_STR = "e"

const val INCREMENT_SYMBOL = '+'
const val DECREMENT_SYMBOL = '-'
const val MULTIPLICATION_SYMBOL = '*'
const val DIVISION_SYMBOL = '/'
const val POW_SYMBOL = '^'

const val UNARY_DECREMENT_SYMBOL = '~'

val binaryOperatorsList = listOf(INCREMENT_SYMBOL, DECREMENT_SYMBOL, MULTIPLICATION_SYMBOL,
    DIVISION_SYMBOL, POW_SYMBOL)
// endregion

class Calculator(formulaString: String) {

    private var formulaRoot: FormulaNode? = null

    init {
        formulaRoot = createTree(formulaString)
    }

    fun calculate(): Double {
        val ret = formulaRoot?.getValue()?:throw parsingErrorException
        if (!ret.isFinite())
            throw calculationErrorException
        return ret
    }

    fun prefix(): String = formulaRoot?.toString()?:throw parsingErrorException

    private fun createTree(formula: String): FormulaNode? {
        if (!bracketsBalanced(formula))
            return null
        val ret = parseString(formula)
        if (!ret.validate())
            return null
        return ret
    }

    private fun bracketsBalanced(formula: String): Boolean {
        var balance = 0
        for (i in formula.indices) {
            if (formula[i]=='(')
                balance += 1
            else if (formula[i]==')')
                balance -= 1
            if (balance < 0)
                return false
        }
        return balance == 0
    }

    private fun prepareString(formula: String): String {
        var ret = StringBuilder()
        formula.forEach { ret.append(it.lowercaseChar()) }
        ret = StringBuilder(ret.filter { !it.isWhitespace() })

        //translating constants because algorithm needs to know difference between "log-1" (unary -) and "e-1" (binary minus)
        ret = StringBuilder(ret.replace(PI_STR.toRegex(), PI.toString()))
        ret = StringBuilder(ret.replace(E_STR.toRegex(), E.toString()))

        /*var i = 1
        while (i < ret.length) {
            if (ret[i - 1] == CLOSE_BRACKET && ret[i] != CLOSE_BRACKET) {
                ret.insert(i, MULTIPLICATION_SYMBOL)
                i += 1
            }
            else if (ret[i - 1].isDigit() && (ret[i] == OPEN_BRACKET || ret[i].isLetter())) {
                ret.insert(i, MULTIPLICATION_SYMBOL)
                i += 1
            }
            i += 1
        }*/

        return ret.toString()
    }

    private fun removeOuterBrackets(formula: String): String {
        if (formula.isEmpty() || formula.first() != OPEN_BRACKET || formula.last() != CLOSE_BRACKET)
            return formula
        val ret = StringBuilder(formula.drop(1).dropLast(1))

        return if(bracketsBalanced(ret.toString()))
            removeOuterBrackets(ret.toString())
        else {
            ret.insert(0, OPEN_BRACKET)
            ret.append(CLOSE_BRACKET)
            ret.toString()
        }
    }

    private fun parseString(formula: String): FormulaNode {
        val str = removeOuterBrackets(prepareString(formula))
        var bracketsDepth = 0
        var minPriority = 3
        var curPriority: Int
        var splittingPos = -1
        // searching for min priority binary operation
        for (i in (str.length - 1) downTo 0) {
            when (str[i]) {
                OPEN_BRACKET -> bracketsDepth += 1
                CLOSE_BRACKET -> bracketsDepth -= 1
                else -> {
                    if(bracketsDepth == 0) {
                        curPriority = when (str[i]) {
                            DECREMENT_SYMBOL, INCREMENT_SYMBOL -> {
                                if (i == 0)
                                    3 // symbol is unary operator
                                else if (binaryOperatorsList.contains(str[i - 1]) || str[i - 1].isLetter())
                                    3 // symbol is unary operator
                                else
                                    0 // symbol is binary operator
                            }
                            MULTIPLICATION_SYMBOL, DIVISION_SYMBOL -> 1
                            POW_SYMBOL -> 2
                            else -> 3
                        }
                        if(curPriority < minPriority) {
                            minPriority = curPriority
                            splittingPos = i
                        }
                    }

                }
            }
        }

        val ret: FormulaNode
        if (splittingPos >= 0) { // splitting position was founded
            val operatorStr = str[splittingPos]
            ret = BinaryOperationNode(operatorStr)
            ret.lChild = createTree(str.substring(0, splittingPos))
            ret.rChild = createTree(str.substring(splittingPos + 1, str.lastIndex + 1))
            return ret
        }
        for (i in functionsList.indices) { // there is no binary operators: checking in case of functions
            if (str.startsWith(functionsList[i])) {
                ret = UnaryOperationNode(functionsList[i])
                ret.child = createTree(str.drop(functionsList[i].length))
                return ret
            }
        }
        return ValueNode(str) // there is no functions or binary operators: trying to create value node
    }
}

abstract class FormulaNode {

    protected var valid: Boolean = true

    abstract fun getValue(): Double

    abstract fun validate(): Boolean
}

class ValueNode(dataStr: String): FormulaNode() {

    private var data: Double

    init {
        try {
            data = dataStr.toDouble()
        }
        catch (e: Exception)
        {
            data = Double.NaN
            valid = false
        }
    }

    override fun getValue(): Double = data

    override fun validate(): Boolean = valid

    override fun toString(): String {
        if(data < 0)
            return UNARY_DECREMENT_SYMBOL.toString() + SPACE_SYMBOL.toString() + (-data).toString()
        return data.toString()
    }
}

class UnaryOperationNode(private var operation: String): FormulaNode() {

    var child: FormulaNode? = null

    init {
        if(!functionsList.contains(operation))
            valid = false
    }

    override fun getValue(): Double {
        if (!validate())
            return Double.NaN
        return when (operation) {
            functionsList[0] -> child!!.getValue().absoluteValue  // abs
            functionsList[1] -> sqrt(child!!.getValue())          // sqrt
            functionsList[2] -> log10(child!!.getValue())         // lg
            functionsList[3] -> ln(child!!.getValue())            // ln
            functionsList[4] -> sin(child!!.getValue())           // sin
            functionsList[5] -> cos(child!!.getValue())           // cos
            functionsList[6] -> tan(child!!.getValue())           // tan
            functionsList[7] -> 1 / tan(child!!.getValue())       // ctg
            functionsList[8] -> asin(child!!.getValue())          // asin
            functionsList[9] -> acos(child!!.getValue())         // acos
            functionsList[10] -> atan(child!!.getValue())         // atg
            functionsList[11] -> atan(-child!!.getValue()) + PI/2 // actg
            else -> Double.NaN
        }
    }

    override fun validate(): Boolean = valid && child?.validate()?:false

    override fun toString(): String {
        return if (validate())
            operation + SPACE_SYMBOL + child!!.toString()
        else
            throw parsingErrorException
    }
}

class BinaryOperationNode(private val operation: Char): FormulaNode() {

    var lChild: FormulaNode? = null
    var rChild: FormulaNode? = null

    init {
        if(!binaryOperatorsList.contains(operation))
            valid = false
    }

    override fun getValue(): Double {
        if (!validate())
            return Double.NaN
        return when (operation) {
            INCREMENT_SYMBOL -> lChild!!.getValue() + rChild!!.getValue()
            DECREMENT_SYMBOL -> lChild!!.getValue() - rChild!!.getValue()
            MULTIPLICATION_SYMBOL -> lChild!!.getValue() * rChild!!.getValue()
            DIVISION_SYMBOL -> lChild!!.getValue() / rChild!!.getValue()
            POW_SYMBOL -> lChild!!.getValue().pow(rChild!!.getValue())
            else -> Double.NaN
        }
    }

    override fun validate(): Boolean = valid && lChild?.validate()?:false && rChild?.validate()?:false

    override fun toString(): String {
        return if (validate())
            operation.toString() + SPACE_SYMBOL + lChild.toString() + SPACE_SYMBOL + rChild.toString()
        else
            throw parsingErrorException
    }
}