package actors.company

import actors.City
import actors.customer.Customer
import actors.product.Product
import factories.company.ShopFactory
import utility.CountingMap

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

    fun getShopsInCity(city: City): List<Shop>? = getCityToShopMap()[city]
    private fun getCityToShopMap(): Map<City, List<Shop>> = shops.groupBy(Shop::city)

    fun getTotalStock(): List<Pair<Product, Int>> {
        val totalStock = CountingMap<Product>()
        shops.forEach { totalStock.addCount(it.inventory) }
        return totalStock.toList()
    }
}