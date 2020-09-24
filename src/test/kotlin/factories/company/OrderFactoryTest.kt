package factories.company

import actors.City
import factories.CityFactory
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

internal class OrderFactoryTest {

    private var srcCity: City = CityFactory.instantiate("City1", 0, 0)
    private var dstCity: City = CityFactory.instantiate("City2", 0, 1)


    @Test
    fun `Consecutive instantiations should have different ids`() {
        val order1 = OrderFactory.instantiate(srcCity, dstCity)
        val order2 = OrderFactory.instantiate(srcCity, dstCity)

        Assertions.assertNotSame(order1.id, order2.id)
    }

    @Test
    fun `Order source city should be set correctly`() {
        val order = OrderFactory.instantiate(srcCity, dstCity)

        Assertions.assertSame(srcCity, order.sourceCity)
    }

    @Test
    fun `Order destination city should be set correctly`() {
        val order = OrderFactory.instantiate(srcCity, dstCity)

        Assertions.assertSame(dstCity, order.destinationCity)
    }

    @Test
    fun `Order by id should return the created object`() {
        val order = OrderFactory.instantiate(srcCity, dstCity)

        Assertions.assertSame(order, OrderFactory.getObjectWithId(order.id))
    }
}