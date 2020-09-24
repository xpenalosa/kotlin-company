package actors

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.function.Executable

internal class CityTest {

    @Test
    fun `getDistanceTo should return Manhattan distance between both cities using latitude and longitude`() {
        val city1: City = City(1, "City1", 0, 0)
        val city2: City = City(2, "City2", 1, 1)
        val city3: City = City(3, "City3", 5, 1)

        Assertions.assertAll(
            Executable { Assertions.assertSame(2, city1.getDistanceTo(city2)) },
            Executable { Assertions.assertSame(6, city1.getDistanceTo(city3)) },
            Executable { Assertions.assertSame(4, city2.getDistanceTo(city3)) }
        )
    }

    @Test
    fun `getDistanceTo should be commutative`() {
        val city1: City = City(1, "City1", 0, 0)
        val city2: City = City(2, "City2", 5, 7)

        Assertions.assertSame(city2.getDistanceTo(city1), city1.getDistanceTo(city2))
    }

    @Test
    fun `getDeliveryDelayTo should linearly relate to distance`() {
        val city1: City = City(1, "City1", 0, 0)
        val city2: City = City(2, "City2", 1, 1)
        val city3: City = City(3, "City3", 5, 1)

        Assertions.assertAll(
            Executable { Assertions.assertTrue(city1.getDeliveryDelayTo(city2) < city1.getDeliveryDelayTo(city3)) },
            Executable { Assertions.assertEquals(city1.getDeliveryDelayTo(city2), city2.getDeliveryDelayTo(city1)) },
        )

    }
}