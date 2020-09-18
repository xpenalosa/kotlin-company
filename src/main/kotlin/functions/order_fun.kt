package functions

import dataclasses.City
import dataclasses.Order
import dataclasses.Product
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

fun Order.getCost(): Double = getProductCost() + getShippingCost()

fun Order.getShippingCost(): Double {
    var shippingCost = sourceCity.getDistanceTo(destCity) * 1.0
    if (getWeight() > 30.0) shippingCost += 10.0
    return shippingCost
}

fun Order.getProductCost(): Double = products.sumByDouble(Product::price)

fun Order.getWeight(): Double = products.sumByDouble(Product::weight)

fun Order.addProduct(product: Product, amount: Int = 1): Order {
    if (!canModify()) throw IllegalStateException("Order has already been shipped")
    for (i in 0..amount) products.add(product)
    return this
}

fun Order.removeProduct(product: Product, amount: Int = 1): Order {
    if (!canModify()) throw IllegalStateException("Order has already been shipped")
    for (i in 0..amount) products.remove(product)
    return this
}

fun Order.shipTo(city: City): Order {
    if (!canModify()) throw IllegalStateException("Order has already been shipped")
    this.destCity = city
    return this
}

fun Order.dispatch(): Order {
    status = Order.OrderStatus.SHIPPED
    GlobalScope.launch {
        // Order is delivered after N minutes, depending on the distance between both cities
        delay(sourceCity.getDeliveryDelayTo(destCity) * 60 * 1000L)
        status = Order.OrderStatus.DELIVERED
    }
    return this
}

private fun Order.canModify(): Boolean = status == Order.OrderStatus.CART