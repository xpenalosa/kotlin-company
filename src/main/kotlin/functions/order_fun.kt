package functions

import dataclasses.City
import dataclasses.Order
import dataclasses.Product

fun Order.getCost(): Double = getProductCost() + getShippingCost()

fun Order.getShippingCost(): Double {
    var shippingCost = sourceCity.getDistanceTo(destCity) * 1.0
    if (getWeight() > 30.0) shippingCost += 10.0
    return shippingCost
}

fun Order.getProductCost(): Double = products.sumByDouble { it.price }

fun Order.getWeight(): Double = products.sumByDouble { it.weight }

fun Order.addProduct(product: Product, amount: Int): Order {
    if (shipped) throw IllegalStateException("Order has already been shipped")
    for (i in 0..amount) products.add(product)
    return this
}

fun Order.shipTo(city: City): Order {
    if (shipped) throw IllegalStateException("Order has already been shipped")
    this.destCity = city
    return this
}

fun Order.complete(): Order {
    shipped = true
    return this
}