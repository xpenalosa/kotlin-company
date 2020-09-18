package factories

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

internal class CityFactoryTest {

    @Test
    fun `Consecutive instantiations should have different ids`() {
        val city1 = CityFactory.instantiate("CityA", 0, 0)
        val city2 = CityFactory.instantiate("CityB", 0, 1)

        Assertions.assertNotSame(city1.id, city2.id)
    }

    @Test
    fun `City name should be set correctly`() {
        val cityName = "City"
        val city = CityFactory.instantiate(cityName, 0, 0)

        Assertions.assertSame(cityName, city.name)
    }

    @Test
    fun `City latitude should be set correctly`() {
        val cityLat = 1
        val city = CityFactory.instantiate("City", cityLat, 0)

        Assertions.assertSame(cityLat, city.latitude)
    }

    @Test
    fun `City longitude should be set correctly`() {
        val cityLon = 1
        val city = CityFactory.instantiate("City", 0, cityLon)

        Assertions.assertSame(cityLon, city.longitude)
    }
}