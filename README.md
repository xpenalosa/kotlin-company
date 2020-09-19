# Simulating a company with Kotlin
![Language](https://img.shields.io/github/languages/top/xpenalosa/kotlin-company)
![Commit](https://img.shields.io/github/last-commit/xpenalosa/kotlin-company)
![CodeFactor](https://www.codefactor.io/repository/github/xpenalosa/kotlin-company/badge)

Using this repository to learn Kotlin.

---
Project rundown:
* A company can set up multiple shops in different cities.
* The shops have their own stock, and it can run out.
* Stocks get refilled periodically, with random amounts of products.
* Customers can place orders on any shop.
    * Order cost depends on products bought, shipping distance and weight penalty.
    * Customer receives the orders after a certain amount of real time, which depends on distance between the shop and the customer.
---

Possible evolutions of the project:
1. Convert project to a web-based solution
2. Enable console interaction via preset commands (e.g. `Shop 4 Check`, `Customer 1 PlaceOrder`)