package factories.customer

import actors.City
import factories.CityFactory
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

internal class CustomerFactoryTest {

    private var city: City = CityFactory.instantiate("City", 0, 0)

    @Test
    fun `Consecutive instantiations should have different ids`() {
        val customer1 = CustomerFactory.instantiate("CustomerA", city)
        val customer2 = CustomerFactory.instantiate("CustomerB", city)

        Assertions.assertNotSame(customer1.id, customer2.id)
    }

    @Test
    fun `Customer name should be set correctly`() {
        val customerName = "Customer"
        val customer = CustomerFactory.instantiate(customerName, city)

        Assertions.assertSame(customerName, customer.name)
    }

    @Test
    fun `Customer city should be set correctly`() {
        val customer = CustomerFactory.instantiate("Customer", city)

        Assertions.assertSame(city, customer.city)
    }

}