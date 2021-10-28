package lab2

import org.junit.Test

import org.junit.Assert.*
import kotlin.math.*

class CalculatorTest {

    @Test
    fun binarySimpleCalculations() {
        val calc = Calculator("8 / 2")
        val expectedValue: Double = 8.0 / 2.0
        val expectedString = "/ 8.0 2.0"
        assertEquals(expectedValue, calc.calculate(), 10.0.pow(-10))
        assertEquals(expectedString, calc.prefix())
    }

    @Test
    fun unarySimpleCalculations() {
        var calc = Calculator("cos pi")
        var expectedValue: Double = cos(PI)
        var expectedString = "cos $PI"
        assertEquals(expectedValue, calc.calculate(), 10.0.pow(-10))
        assertEquals(expectedString, calc.prefix())

        calc = Calculator("-e")
        expectedValue = -E
        expectedString = "~ $E"
        assertEquals(expectedValue, calc.calculate(), 10.0.pow(-10))
        assertEquals(expectedString, calc.prefix())
    }

    @Test
    fun comboCalculations() {
        val calc = Calculator("cos pi + 4^-2 * lg +e")
        val expectedValue: Double = cos(PI) + 4.0.pow(-2) * log10(E)
        val expectedString = "+ cos $PI * ^ 4.0 ~ 2.0 lg $E"
        assertEquals(expectedValue, calc.calculate(), 10.0.pow(-10))
        assertEquals(expectedString, calc.prefix())
    }

    @Test
    fun bracketsCalculations() {
        val calc = Calculator("cos(pi + 4)^-2 * lg +e")
        val expectedValue: Double = cos(PI + 4.0).pow(-2) * log10(E)
        val expectedString = "* ^ cos + $PI 4.0 ~ 2.0 lg $E"
        assertEquals(expectedValue, calc.calculate(), 10.0.pow(-10))
        assertEquals(expectedString, calc.prefix())
    }

    @Test
    fun calculationError() {
        val calc = Calculator("1 / 0")
        val expected = "Calculation error"
        var actual = ""
        try {
            calc.calculate()
        }
        catch (e: Exception) {
            actual = e.message?:""
        }
        assertEquals(expected, actual)
    }

    @Test
    fun parsingError() {
        var calc = Calculator("cos(pi + 4))^-2 * lg +e")
        val expected = "Parsing error"
        var actual = ""
        try {
            calc.calculate()
        }
        catch (e: Exception) {
            actual = e.message?:""
        }
        assertEquals(expected, actual)

        calc = Calculator("cos(pi + 4)(^-2) * lg +e")
        try {
            calc.calculate()
        }
        catch (e: Exception) {
            actual = e.message?:""
        }
        assertEquals(expected, actual)

        calc = Calculator("make 9309 great again")
        try {
            calc.calculate()
        }
        catch (e: Exception) {
            actual = e.message?:""
        }
        assertEquals(expected, actual)
    }
}