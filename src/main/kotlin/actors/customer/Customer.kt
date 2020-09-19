package actors.customer

import actors.City
import actors.company.Order
import actors.product.Product
import utility.CountingMap

data class Customer(val id: Long, val name: String, val city: City) {
    private val orders: List<Order> = ArrayList()

    fun getSpentMoney(): Double = orders.sumByDouble(Order::getCost)
    fun getPurchases(): CountingMap<Product> = orders.fold(CountingMap<Product>()){ part, elem -> part + elem.inventory}
    fun getTimesBought(product: Product): Int = getPurchases().getOrDefault(product, 0)
    fun getMostBought(): Product? = getPurchases().maxByOrNull { it.value }?.key
}