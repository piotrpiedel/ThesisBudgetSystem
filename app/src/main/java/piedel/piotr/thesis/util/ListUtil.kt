package piedel.piotr.thesis.util

fun <T> List<T>.toPair(): List<Pair<T, T>> {
    require(this.size % 2 == 0) { "Can't make pairs from odd number of list items!" }
    if (this.size > 2 && this.size % 2 == 0) {
        val listOfPairs: MutableList<Pair<T, T>> = mutableListOf()
        for (index in this.indices step 2) {
            if (index % 2 == 0)
                listOfPairs.add(Pair(this[index], this[index + 1]))
        }
        return listOfPairs
    } else return listOf(Pair(this[0], this[1]))
}
