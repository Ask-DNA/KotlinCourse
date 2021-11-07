package lab2

// ignores space symbols
// brackets in functions can be omitted (if function of value/unary operation):
//    sin(cos(+pi)) -> sincos+pi;
//    sin(pi)-1 -> sinpi-1
//    sin3.14 -> sin(3.14)

// functions: abs, sqrt, lg, ln, sin, cos, tg, ctg, asin, acos, atg, actg
// operators: +; -; *; /; ^; ~ (unary minus, using only in prefix notation)

fun main() {
    print("Infix -> ")
    val formula = readLine() ?: ""
    val calc = Calculator(formula)
    try {
        println("Prefix: " + calc.prefix())
        println("Result: " + calc.calculate())
    } catch (e: Exception) {
        println(e.message)
    }
}