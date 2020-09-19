package factories.company

import actors.City
import actors.company.Order
import factories.BaseFactory

object OrderFactory : BaseFactory() {

    override fun instantiate(vararg args: Any): Order {
        val order = Order(
            id = nextId.getAndAdd(1),
            sourceCity = args[0] as City,
            destinationCity = args[1] as City
        )
        createdObjects[order.id] = order
        return order
    }
}