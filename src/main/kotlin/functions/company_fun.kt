package functions

import dataclasses.City
import dataclasses.Company
import dataclasses.Product
import dataclasses.Shop

fun Company.addCity(city: City) = cities.add(city)

fun Company.createShop(city: City, name: String): Shop {
    if (city !in cities) {
        addCity(city)
    }
    val newShopId = shopId.getAndAdd(1)
    val shop = Shop(newShopId, city)
    shop.readInventoryFromFile("resources/shops/$newShopId.csv")
    shops.add(shop)
    return shop
}

fun Company.shopsInCity(city: City): List<Shop>? = cityToShopMap()[city]
private fun Company.cityToShopMap(): Map<City, List<Shop>> = shops.groupBy(Shop::city)

fun Company.getTotalStock(): List<Pair<Product, Int>> = shops
    .flatMap(Shop::inventory)
    .groupBy(Pair<Product, Int>::first)
    .mapValues { it.value.sumBy(Pair<Product, Int>::second) }
    .toList()


