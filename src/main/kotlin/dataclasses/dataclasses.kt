package dataclasses

import java.io.File

data class Company(val name: String) {

    val shops = ArrayList<Shop>()

}


data class City(val name: String) {}
data class Shop(val id: Long, val city: City) {
    val inventory = ArrayList<Pair<Product, Int>>()
}
data class Client(val id: Long, val name: String, val city: City) {}
data class Product(val name: String, val price: Double = 1.0, val weight: Double = 1.0) {}
data class Order(val products: ArrayList<Product>, val sourceCity: City, var shipped: Boolean = false) {
    lateinit var destCity: City
}