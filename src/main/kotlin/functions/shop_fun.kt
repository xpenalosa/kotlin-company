package functions

import dataclasses.Product
import dataclasses.Shop
import java.io.File


fun Shop.readInventoryFromFile(filename: String) {
    File(filename).forEachLine {
        val (product_name, amount) = it.split(",")
        // TODO: Read product from database
        val product = Product(product_name,1.0, 1.0)
        inventory.add(product to amount.toInt())
    }
}