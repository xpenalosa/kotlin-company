package actors.company

import actors.City
import actors.product.Product
import factories.company.OrderFactory
import utility.CountingMap
import java.io.File

data class Shop(val id: Long, val city: City, val supportedProducts: Set<Product>) {
    val inventory = CountingMap<Product>()

    init {
        supportedProducts.forEach { inventory[it] = 0 }
    }

    fun readInventoryFromFile(filename: String) {
        File(filename).forEachLine { line ->
            val (product_name, amount) = line.split(",")
            val product = supportedProducts.find { it.name == product_name }
            if (product != null) {
                inventory.addCount(product, amount.toInt())
            }
        }
    }

    fun createNewOrder(customerCity: City): Order = OrderFactory.instantiate(city, customerCity)
    fun getAvailableProducts(): CountingMap<Product> = inventory.filter { it.value > 0 } as CountingMap<Product>
    fun addToOrder(order: Order, product: Product, amount: Int): Boolean {
        if (inventory.getOrDefault(product, Int.MIN_VALUE) >= amount) {
            order.addProduct(product, amount)
            return true
        }
        return false
    }

    fun removeFromOrder(order: Order, product: Product, amount: Int) {
        order.subtractProduct(product, amount)
    }

}
