package actors.company

import actors.City
import actors.product.Inventory
import actors.product.Product
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class Order(val id: Long, val sourceCity: City) {

    private val inventory = Inventory()
    private var status: OrderStatus = OrderStatus.CART
    lateinit var destCity: City

    enum class OrderStatus {
        CART, SHIPPED, DELIVERED
    }

    fun getCost(): Double = getProductCost() + getShippingCost()

    fun getShippingCost(): Double {
        var shippingCost = sourceCity.getDistanceTo(destCity) * 1.0
        if (getWeight() > 30.0) shippingCost += 10.0
        return shippingCost
    }

    fun getProductCost(): Double = inventory.toList().fold(0.0){ part, elem -> part + elem.first.price * elem.second}

    fun getWeight(): Double = inventory.toList().fold(0.0){ part, elem -> part + elem.first.weight * elem.second}

    fun addProduct(product: Product, amount: Int = 1): Order {
        if (!canModify()) throw IllegalStateException("Order has already been shipped")
        inventory.addProduct(product, amount)
        return this
    }

    fun subtractProduct(product: Product, amount: Int = 1): Order {
        if (!canModify()) throw IllegalStateException("Order has already been shipped")
        inventory.subtractProduct(product, amount)
        return this
    }

    fun shipTo(city: City): Order {
        if (!canModify()) throw IllegalStateException("Order has already been shipped")
        this.destCity = city
        return this
    }

    fun dispatch(): Order {
        status = OrderStatus.SHIPPED
        GlobalScope.launch {
            // Order is delivered after N minutes, depending on the distance between both cities
            delay(sourceCity.getDeliveryDelayTo(destCity) * 60 * 1000L)
            status = OrderStatus.DELIVERED
        }
        return this
    }

    private fun canModify(): Boolean = status == OrderStatus.CART
}