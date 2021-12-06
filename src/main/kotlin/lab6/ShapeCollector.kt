package lab6

import lab3.Shape

class ShapeCollector<T : Shape> {

    private val allShapes = mutableListOf<T>()

    fun add(new: T) {
        allShapes.add(new)
    }

    fun addAll(new: List<T>) {
        allShapes.addAll(new)
    }

    fun getAll(): List<T> = allShapes.toList()

    fun getAllSorted(comparator: Comparator<in T>): List<T> = allShapes.sortedWith(comparator)

    fun getAllByClass(clazz: Class<out T>): List<T> = allShapes.filter { clazz.isInstance(it) }
}