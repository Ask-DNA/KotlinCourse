package lab1

fun main(){
    val text= mutableListOf(
        "When in disgrace with fortune and man's eyes",
        "I all alone beweep my outcast state,",
        "And trouble deaf heaven with my bootless cries,",
        "And look upon myself, and curse my fate...",
    )
    text.alignText(Alignment.CENTER,50)
    text.forEach{ println(it) }
}