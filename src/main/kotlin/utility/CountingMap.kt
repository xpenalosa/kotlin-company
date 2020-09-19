package utility

import java.lang.Integer.min

open class CountingMap<K> : HashMap<K, Int>() {

    fun initializeCount(key: K) = put(key, 0)
    fun initializeCount(other: Collection<K>) = other.forEach{initializeCount(it)}

    fun addCount(key: K, count: Int = 1) = put(key, (count + this.getOrDefault(key, 0)).coerceAtLeast(0))
    fun addCount(other: CountingMap<K>) = other.forEach { addCount(it.key, it.value) }

    fun subtractCount(key: K, count: Int = 1): Int {
        val subtractedCount = min(count, this.getOrDefault(key, 0))
        if (this.containsKey(key)) {
            addCount(key, -subtractedCount)
        }
        return subtractedCount
    }

    fun subtractCount(other: CountingMap<K>): CountingMap<K> {
        val newCm = CountingMap<K>()
        newCm.initializeCount(this.keys)
        newCm.initializeCount(other.keys)
        other.forEach {
            newCm.addCount(it.key, subtractCount(it.key, it.value))
        }
        return newCm
    }

    operator fun plus(other: CountingMap<K>): CountingMap<K> {
        val newCm = CountingMap<K>()
        newCm.addCount(this)
        newCm.addCount(other)
        return newCm
    }

    operator fun minus(other: CountingMap<K>): CountingMap<K> {
        val newCm = CountingMap<K>()
        newCm.initializeCount(other.keys)
        newCm.addCount(this)
        newCm.subtractCount(other)
        return newCm
    }

    fun resetCounts() = this.replaceAll { _, _ -> 0 }
    fun totalCount(): Int = this.values.sum()
}