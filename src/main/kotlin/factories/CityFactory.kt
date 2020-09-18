package factories

import actors.City

object CityFactory : BaseFactory() {

    override fun instantiate(vararg args: Any): City = City(
        id = nextId.getAndAdd(1),
        name = args[0] as String,
        latitude = args[1] as Int,
        longitude = args[2] as Int,
    )

}