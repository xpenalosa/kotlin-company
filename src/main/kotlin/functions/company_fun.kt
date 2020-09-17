package functions

import dataclasses.Company
import dataclasses.Shop

fun Company.addShop(shop: Shop) = shops.add(shop)