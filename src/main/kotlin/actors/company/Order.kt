package actors.company

import actors.City
import actors.product.Product
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import utility.CountingMap

data class Order(val id: Long, val sourceCity: City, val destinationCity: City) {

    val inventory = CountingMap<Product>()
    private var status: OrderStatus = OrderStatus.CART

    companion object {
        const val maxShipmentWeight: Double = 30.0
        const val extraWeightCost: Double = 10.0
    }

    enum class OrderStatus {
        CART, SHIPPED, DELIVERED
    }

    fun getCost(): Double = getProductCost() + getShippingCost()
    fun getProductCost(): Double = inventory.toList().fold(0.0) { part, elem -> part + elem.first.price * elem.second }
    fun getTotalWeight(): Double = inventory.toList().fold(0.0) { part, elem -> part + elem.first.weight * elem.second }
    fun getShippingCost(): Double {
        var shippingCost = sourceCity.getDistanceTo(destinationCity).toDouble()
        if (getTotalWeight() > maxShipmentWeight) shippingCost += extraWeightCost
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

    fun dispatch(): Job {
        if (!canModify()) throw IllegalStateException("Order has already been shipped")
        status = OrderStatus.SHIPPED
        return GlobalScope.launch {
            delay(sourceCity.getDeliveryDelayTo(destinationCity).toLong())
            status = OrderStatus.DELIVERED
        }
    }

    fun getStatus(): OrderStatus = status

    private fun canModify(): Boolean = status == OrderStatus.CART

}