package actors.company

import actors.City
import actors.product.Product
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import utility.CountingMap

data class Order(val id: Long, val sourceCity: City, val destinationCity: City) {

    val inventory = CountingMap<Product>()
    private var status: OrderStatus = OrderStatus.CART

    enum class OrderStatus {
        CART, SHIPPED, DELIVERED
    }

    fun getCost(): Double = getProductCost() + getShippingCost()
    fun getProductCost(): Double = inventory.toList().fold(0.0) { part, elem -> part + elem.first.price * elem.second }
    fun getTotalWeight(): Double = inventory.toList().fold(0.0) { part, elem -> part + elem.first.weight * elem.second }
    fun getShippingCost(): Double {
        var shippingCost = sourceCity.getDistanceTo(destinationCity) * 1.0
        if (getTotalWeight() > 30.0) shippingCost += 10.0
        return shippingCost
    }

    fun addProduct(product: Product, amount: Int = 1): Order {
        if (!canModify()) throw IllegalStateException("Order has already been shipped")
        inventory.addCount(product, amount)
        return this
    }

    fun subtractProduct(product: Product, amount: Int = 1): Order {
        if (!canModify()) throw IllegalStateException("Order has already been shipped")
        inventory.subtractCount(product, amount)
        return this
    }

    fun dispatch(): Order {
        status = OrderStatus.SHIPPED
        GlobalScope.launch {
            deliveryDelayCoroutine()
            status = OrderStatus.DELIVERED
        }
        return this
    }

    suspend fun deliveryDelayCoroutine() {
        // Order is delivered after N minutes, depending on the distance between both cities
        delay(sourceCity.getDeliveryDelayTo(destinationCity) * 60 * 1000L)
    }

    private fun canModify(): Boolean = status == OrderStatus.CART
}