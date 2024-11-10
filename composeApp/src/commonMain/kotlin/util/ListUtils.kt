package util

fun <E> MutableList<E>.addNotNull(item: E?): List<E> {
    if (item != null) {
        this.add(item)
    }

    return this
}