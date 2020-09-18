package actors.company

import actors.City
import actors.product.Inventory
import actors.product.Product
import java.io.File

data class Shop(val id: Long, val city: City, val supportedProducts: Set<Product>) {
    val inventory = Inventory()

    init {
        supportedProducts.forEach { inventory[it] = 0 }
    }

    fun readInventoryFromFile(filename: String) {
        File(filename).forEachLine { line ->
            val (product_name, amount) = line.split(",")
            val product = supportedProducts.find { it.name == product_name }
            if (product != null) {
                inventory.addProduct(product, amount.toInt())
            }
        }
    }


}
