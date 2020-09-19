package factories.customer

import actors.City
import actors.customer.Customer
import factories.BaseFactory

object CustomerFactory : BaseFactory() {

    override fun instantiate(vararg args: Any): Customer {
        val customer = Customer(
            id = nextId.getAndAdd(1),
            name = args[0] as String,
            city = args[1] as City
        )
        createdObjects[customer.id] = customer
        return customer
    }

}