package actors.company

import actors.City
import actors.customer.Customer
import actors.product.Inventory
import actors.product.Product
import factories.company.ShopFactory

data class Company(val name: String) {

    private val shops = ArrayList<Shop>()
    private val cities = HashSet<City>()
    private val products = HashSet<Product>()
    private val customers = HashSet<Customer>()

    private fun addCity(city: City) = cities.add(city)
    fun registerCustomer(customer: Customer) = customers.add(customer)

    fun createShop(city: City): Shop {
        if (city !in cities) {
            addCity(city)
        }
        val shop = ShopFactory.instantiate(city, products)
        shop.readInventoryFromFile("resources/${this.name}/${shop.id}.csv")
        shops.add(shop)
        return shop
    }

    fun shopsInCity(city: City): List<Shop>? = cityToShopMap()[city]
    private fun cityToShopMap(): Map<City, List<Shop>> = shops.groupBy(Shop::city)

    fun getTotalStock(): List<Pair<Product, Int>> {
        val totalStock = Inventory()
        shops.forEach { totalStock.bulkAddProduct(it.inventory) }
        return totalStock.toList()
    }
}