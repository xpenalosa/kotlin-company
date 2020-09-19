package utility

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.function.Executable

internal class CountingMapTest {

    private val countingMap: CountingMap<String> = CountingMap()

    @BeforeEach
    fun beforeEach() {
        countingMap.clear()
    }

    @Test
    fun `initializeCount should add an entry with count equal to 0`() {
        val key = "Key"
        countingMap.initializeCount(key)

        Assertions.assertAll(
            Executable { Assertions.assertTrue(countingMap.containsKey(key)) },
            Executable { Assertions.assertEquals(0, countingMap[key]) },
        )
    }

    @Test
    fun `initializeCount _other_ should add an entry for each item in the collection`() {
        val keys: List<String> = listOf<String>("KeyA", "KeyB", "KeyC")
        countingMap.initializeCount(keys)

        keys.forEach{
            Assertions.assertTrue(countingMap.containsKey(it))
            Assertions.assertEquals(0, countingMap[it])
        }
    }

    @Test
    fun `addCount _key_ should create a new entry with count = 1 if it does not exist`() {
        val key = "Key"
        Assertions.assertFalse(countingMap.containsKey(key))
        countingMap.addCount(key)
        Assertions.assertAll(
            Executable { Assertions.assertTrue(countingMap.containsKey(key)) },
            Executable { Assertions.assertEquals(1, countingMap[key]) },
        )
    }

    @Test
    fun `addCount _key, count_ should create a new entry with the correct count if the entry does not exist`() {
        val key = "Key"
        val count = 5
        Assertions.assertFalse(countingMap.containsKey(key))
        countingMap.addCount(key, count)
        Assertions.assertAll(
            Executable { Assertions.assertTrue(countingMap.containsKey(key)) },
            Executable { Assertions.assertEquals(count, countingMap[key]) },
        )
    }

    @Test
    fun `addCount _key, count_ should add the correct count if the entry exists`() {
        val key = "Key"
        countingMap.addCount(key, 1)
        countingMap.addCount(key, 5)
        Assertions.assertAll(
            Executable { Assertions.assertTrue(countingMap.containsKey(key)) },
            Executable { Assertions.assertEquals(6, countingMap[key]) },
        )
    }

    @Test
    fun `addCount _other_ should add all the elements correctly`() {
        countingMap.addCount("Carrots", 3)
        countingMap.addCount("Apples", 1)
        val other = CountingMap<String>()
        other.addCount("Potatoes", 5)
        other.addCount("Carrots", 2)

        countingMap.addCount(other)
        Assertions.assertAll(
            Executable { Assertions.assertTrue(countingMap.containsKey("Carrots")) },
            Executable { Assertions.assertTrue(countingMap.containsKey("Apples")) },
            Executable { Assertions.assertTrue(countingMap.containsKey("Potatoes")) },
            Executable { Assertions.assertEquals(5, countingMap["Carrots"]) },
            Executable { Assertions.assertEquals(1, countingMap["Apples"]) },
            Executable { Assertions.assertEquals(5, countingMap["Potatoes"]) }
        )
    }

    @Test
    fun `subtractCount _key_ should return 0 if the entry does not exist`() {
        val key = "Key"
        Assertions.assertFalse(countingMap.containsKey(key))

        val returnCount = countingMap.subtractCount(key)
        Assertions.assertAll(
            Executable { Assertions.assertFalse(countingMap.containsKey(key)) },
            Executable { Assertions.assertEquals(0, returnCount) },
        )
    }

    @Test
    fun `subtractCount _key, count_ should return 0 if the entry does not exist`() {
        val key = "Key"
        Assertions.assertFalse(countingMap.containsKey(key))

        val returnCount = countingMap.subtractCount(key, 5)
        Assertions.assertAll(
            Executable { Assertions.assertFalse(countingMap.containsKey(key)) },
            Executable { Assertions.assertEquals(0, returnCount) },
        )
    }

    @Test
    fun `subtractCount _key, count_ should return _count_ if the count for the entry is larger`() {
        val key = "Key"
        countingMap.addCount(key, 4)

        val returnCount = countingMap.subtractCount(key, 2)
        Assertions.assertAll(
            Executable { Assertions.assertTrue(countingMap.containsKey(key)) },
            Executable { Assertions.assertEquals(2, returnCount) },
        )
    }

    @Test
    fun `subtractCount _key, count_ should return the count for the entry if _count_ is larger`() {
        val key = "Key"
        countingMap.addCount(key, 4)

        val returnCount = countingMap.subtractCount(key, 5)
        Assertions.assertAll(
            Executable { Assertions.assertTrue(countingMap.containsKey(key)) },
            Executable { Assertions.assertEquals(4, returnCount) },
        )
    }

    @Test
    fun `subtractCount _other_ subtracts all the elements correctly`() {
        countingMap.addCount("Carrots", 1)
        countingMap.addCount("Apples", 5)
        countingMap.addCount("Potatoes", 3)

        val other = CountingMap<String>()
        other.addCount("Apples", 10)
        other.addCount("Potatoes", 2)

        val returnCount = countingMap.subtractCount(other)
        Assertions.assertAll(
            Executable { Assertions.assertTrue(countingMap.containsKey("Carrots")) },
            Executable { Assertions.assertTrue(countingMap.containsKey("Apples")) },
            Executable { Assertions.assertTrue(countingMap.containsKey("Potatoes")) },
            Executable { Assertions.assertEquals(1, countingMap["Carrots"]) },
            Executable { Assertions.assertEquals(0, countingMap["Apples"]) },
            Executable { Assertions.assertEquals(1, countingMap["Potatoes"]) },

            Executable { Assertions.assertTrue(returnCount.containsKey("Carrots")) },
            Executable { Assertions.assertTrue(returnCount.containsKey("Apples")) },
            Executable { Assertions.assertTrue(returnCount.containsKey("Potatoes")) },
            Executable { Assertions.assertEquals(0, returnCount["Carrots"]) },
            Executable { Assertions.assertEquals(5, returnCount["Apples"]) },
            Executable { Assertions.assertEquals(2, returnCount["Potatoes"]) },
        )
    }

    @Test
    fun `plus should return a merged CountingMap `() {
        countingMap.addCount("Carrots", 2)
        countingMap.addCount("Apples", 1)

        val other = CountingMap<String>()
        other.addCount("Carrots", 5)
        other.addCount("Potatoes", 5)

        val returnCount = countingMap + other
        Assertions.assertAll(
            Executable { Assertions.assertTrue(returnCount.containsKey("Carrots")) },
            Executable { Assertions.assertTrue(returnCount.containsKey("Apples")) },
            Executable { Assertions.assertTrue(returnCount.containsKey("Potatoes")) },
            Executable { Assertions.assertEquals(7, returnCount["Carrots"]) },
            Executable { Assertions.assertEquals(1, returnCount["Apples"]) },
            Executable { Assertions.assertEquals(5, returnCount["Potatoes"]) },
        )
    }

    @Test
    fun `minus should return a new CountingMap with the counts for each element equal to the first map's minus the second's`() {
        countingMap.addCount("Carrots", 2)
        countingMap.addCount("Apples", 1)

        val other = CountingMap<String>()
        other.addCount("Carrots", 5)
        other.addCount("Potatoes", 5)

        val returnCount = countingMap - other
        Assertions.assertAll(
            Executable { Assertions.assertTrue(returnCount.containsKey("Carrots")) },
            Executable { Assertions.assertTrue(returnCount.containsKey("Apples")) },
            Executable { Assertions.assertTrue(returnCount.containsKey("Potatoes")) },
            Executable { Assertions.assertEquals(0, returnCount["Carrots"]) },
            Executable { Assertions.assertEquals(1, returnCount["Apples"]) },
            Executable { Assertions.assertEquals(0, returnCount["Potatoes"]) }
        )
    }

    @Test
    fun `resetCounts should reset all counts to 0`() {
        countingMap.addCount("Carrots", 2)
        countingMap.addCount("Apples", 1)

        countingMap.resetCounts()
        Assertions.assertAll(
            Executable { Assertions.assertTrue(countingMap.containsKey("Carrots")) },
            Executable { Assertions.assertTrue(countingMap.containsKey("Apples")) },
            Executable { Assertions.assertEquals(0, countingMap["Carrots"]) },
            Executable { Assertions.assertEquals(0, countingMap["Apples"]) }
        )
    }

    @Test
    fun `totalCount should return the sum of all counts`() {
        countingMap.addCount("Carrots", 10)
        countingMap.addCount("Apples", 12)
        countingMap.addCount("Bananas", 5)

        Assertions.assertEquals(27, countingMap.totalCount())
    }
}