package factories.company

import actors.City
import actors.company.Shop
import actors.product.Product
import factories.BaseFactory

object ShopFactory : BaseFactory() {

    override fun instantiate(vararg args: Any): Shop {
        val shop = Shop(
            id = nextId.getAndAdd(1),
            city = args[0] as City,
            supportedProducts = args[1] as Set<Product>
        )
        createdObjects[shop.id] = shop
        return shop
    }

}