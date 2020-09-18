package factories.company

import actors.City
import actors.product.Product
import factories.CityFactory
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.function.Executable

internal class ShopFactoryTest {

    private var city: City = CityFactory.instantiate("City", 0, 0)
    private var productSet = HashSet<Product>()

    init {
        val prod1 = Product("Prod1", 1.0, 1.0)
        val prod2 = Product("Prod2", 1.0, 2.0)
        productSet.add(prod1)
        productSet.add(prod2)
    }

    @Test
    fun `Consecutive instantiations should have different ids`() {
        val shop1 = ShopFactory.instantiate(city, HashSet<Product>())
        val shop2 = ShopFactory.instantiate(city, HashSet<Product>())

        Assertions.assertNotSame(shop1.id, shop2.id)
    }

    @Test
    fun `Shop city should be set correctly`() {
        val shop = ShopFactory.instantiate(city, productSet)

        Assertions.assertAll(
            Executable { Assertions.assertNotNull(shop.supportedProducts) },
            Executable { Assertions.assertTrue(productSet.first() in shop.supportedProducts) },
            Executable { Assertions.assertSame(productSet, shop.supportedProducts) }
        )
    }
}