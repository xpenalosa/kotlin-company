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
        productSet.add(Product("Prod1", 1.0, 1.0))
        productSet.add(Product("Prod2", 1.0, 2.0))
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

        Assertions.assertSame(city, shop.city)
    }

    @Test
    fun `Shop supported products should be initiated correctly`() {
        val shop = ShopFactory.instantiate(city, productSet)

        Assertions.assertAll(
            Executable { Assertions.assertNotNull(shop.supportedProducts) },
            Executable { Assertions.assertTrue(productSet.first() in shop.supportedProducts) },
            Executable { Assertions.assertSame(productSet, shop.supportedProducts) }
        )
    }

    @Test
    fun `Shop by id should return the created object`() {
        val shop = ShopFactory.instantiate(city, productSet)

        Assertions.assertSame(shop, ShopFactory.getObjectWithId(shop.id))
    }
}