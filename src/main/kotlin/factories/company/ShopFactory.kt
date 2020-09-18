package factories.company

import actors.City
import actors.product.Product
import actors.company.Shop
import factories.BaseFactory

object ShopFactory : BaseFactory() {

    override fun instantiate(vararg args: Any): Shop = Shop(
        id = nextId.getAndAdd(1),
        city = args[0] as City,
        supportedProducts = args[1] as Set<Product>
    )

}