package actors.company

import actors.City
import actors.product.Product
import factories.CityFactory
import factories.company.OrderFactory
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.function.Executable

internal class OrderTest {

    private val sourceCity: City = CityFactory.instantiate("Source", 0, 0)
    private val destinationCity: City = CityFactory.instantiate("Destination", 0, 1)
    private lateinit var order: Order

    private val appleProduct = Product("Apple", 1.0, 0.5)
    private val bananaProduct = Product("Banana", 2.0, 1.0)
    private val carrotProduct = Product("Carrot", 0.7, 0.6)
    private val appleAmount = 5
    private val bananaAmount = 2
    private val carrotAmount = 8

    @BeforeEach
    fun beforeEach() {
        order = OrderFactory.instantiate(sourceCity, destinationCity)
        order.addProduct(appleProduct, appleAmount)
        order.addProduct(bananaProduct, bananaAmount)
        order.addProduct(carrotProduct, carrotAmount)
    }

    @Test
    fun `getCost should return the sum of product costs and shipping costs`() {
        val productCost: Double =
            appleProduct.price * appleAmount + bananaProduct.price * bananaAmount + carrotProduct.price * carrotAmount
        val shippingCost: Double = sourceCity.getDistanceTo(destinationCity).toDouble()
        Assertions.assertEquals(productCost+shippingCost, order.getCost(), 0.001)

    }

    @Test
    fun `getProductCost should return the sum of the price of each product times the amount of product`() {
        val productCost: Double =
            appleProduct.price * appleAmount + bananaProduct.price * bananaAmount + carrotProduct.price * carrotAmount
        Assertions.assertEquals(productCost, order.getProductCost(), 0.001)
    }

    @Test
    fun `getProductWeight should return the sum of the weight of each product times the amount of product`() {
        val totalWeight: Double =
            appleProduct.weight * appleAmount + bananaProduct.weight * bananaAmount + carrotProduct.weight * carrotAmount
        Assertions.assertEquals(totalWeight, order.getTotalWeight(), 0.001)
    }

    @Test
    fun `getShippingCost should only increase if the order exceeds the weight threshold`() {
        val initialShippingCost = order.getShippingCost()
        order.addProduct(Product("Light item", 1.0, 0.01))
        val orderPlusLightItemShippingCost = order.getShippingCost()
        order.addProduct(Product("Heavy item", 1.0, Order.maxShipmentWeight*2))
        Assertions.assertAll(
            Executable {Assertions.assertEquals(initialShippingCost, orderPlusLightItemShippingCost, 0.001)},
            Executable {Assertions.assertEquals(Order.extraWeightCost, order.getShippingCost() - initialShippingCost, 0.001)}
        )
    }

    @Test
    fun `addProduct _product_ with an existing product should correctly modify the amount of product in the order`() {
        order.addProduct(appleProduct)
        val productCost: Double =
            appleProduct.price * (appleAmount + 1) + bananaProduct.price * bananaAmount + carrotProduct.price * carrotAmount
        val totalWeight: Double =
            appleProduct.weight * (appleAmount + 1) + bananaProduct.weight * bananaAmount + carrotProduct.weight * carrotAmount

        Assertions.assertAll(
            Executable { Assertions.assertEquals(appleAmount + 1, order.inventory[appleProduct]) },
            Executable { Assertions.assertEquals(productCost, order.getProductCost(), 0.001) },
            Executable { Assertions.assertEquals(totalWeight, order.getTotalWeight(), 0.001) },
        )
    }

    @Test
    fun `addProduct _product_ with a new product should correctly add the new product to the order`() {
        val diceProduct = Product("Dice", 5.0, 0.1)
        order.addProduct(diceProduct)
        val productCost: Double =
            appleProduct.price * appleAmount + bananaProduct.price * bananaAmount + carrotProduct.price * carrotAmount + diceProduct.price
        val totalWeight: Double =
            appleProduct.weight * appleAmount + bananaProduct.weight * bananaAmount + carrotProduct.weight * carrotAmount + diceProduct.weight

        Assertions.assertAll(
            Executable { Assertions.assertEquals(1, order.inventory[diceProduct]) },
            Executable { Assertions.assertEquals(productCost, order.getProductCost(), 0.001) },
            Executable { Assertions.assertEquals(totalWeight, order.getTotalWeight(), 0.001) },
        )
    }

    @Test
    fun `addProduct _product, count_ with an existing product should correctly modify the amount of product in the order`() {
        val productAmount = 10
        order.addProduct(appleProduct, productAmount)
        val productCost: Double =
            appleProduct.price * (appleAmount + productAmount) + bananaProduct.price * bananaAmount + carrotProduct.price * carrotAmount
        val totalWeight: Double =
            appleProduct.weight * (appleAmount + productAmount) + bananaProduct.weight * bananaAmount + carrotProduct.weight * carrotAmount

        Assertions.assertAll(
            Executable { Assertions.assertEquals(appleAmount + productAmount, order.inventory[appleProduct]) },
            Executable { Assertions.assertEquals(productCost, order.getProductCost(), 0.001) },
            Executable { Assertions.assertEquals(totalWeight, order.getTotalWeight(), 0.001) },
        )
    }

    @Test
    fun `addProduct _product, amount_ with a new product should correctly add the new product to the order`() {
        val diceProduct = Product("Dice", 5.0, 0.1)
        val productAmount = 7
        order.addProduct(diceProduct, productAmount)
        val productCost: Double =
            appleProduct.price * appleAmount + bananaProduct.price * bananaAmount + carrotProduct.price * carrotAmount + diceProduct.price * productAmount
        val totalWeight: Double =
            appleProduct.weight * appleAmount + bananaProduct.weight * bananaAmount + carrotProduct.weight * carrotAmount + diceProduct.weight * productAmount

        Assertions.assertAll(
            Executable { Assertions.assertEquals(productAmount, order.inventory[diceProduct]) },
            Executable { Assertions.assertEquals(productCost, order.getProductCost(), 0.001) },
            Executable { Assertions.assertEquals(totalWeight, order.getTotalWeight(), 0.001) },
        )
    }

    @Test
    fun `subtractProduct _product_ with an existing product should correctly modify the amount of product in the order`() {
        order.subtractProduct(appleProduct)
        val productCost: Double =
            appleProduct.price * (appleAmount - 1) + bananaProduct.price * bananaAmount + carrotProduct.price * carrotAmount
        val totalWeight: Double =
            appleProduct.weight * (appleAmount - 1) + bananaProduct.weight * bananaAmount + carrotProduct.weight * carrotAmount

        Assertions.assertAll(
            Executable { Assertions.assertEquals(appleAmount - 1, order.inventory[appleProduct]) },
            Executable { Assertions.assertEquals(productCost, order.getProductCost(), 0.001) },
            Executable { Assertions.assertEquals(totalWeight, order.getTotalWeight(), 0.001) },
        )
    }

    @Test
    fun `subtractProduct _product_ with a product not in the order should not modify the order`() {
        val diceProduct = Product("Dice", 5.0, 0.1)
        order.subtractProduct(diceProduct)
        val productCost: Double =
            appleProduct.price * appleAmount + bananaProduct.price * bananaAmount + carrotProduct.price * carrotAmount
        val totalWeight: Double =
            appleProduct.weight * appleAmount + bananaProduct.weight * bananaAmount + carrotProduct.weight * carrotAmount

        Assertions.assertAll(
            Executable { Assertions.assertFalse(order.inventory.containsKey(diceProduct)) },
            Executable { Assertions.assertEquals(productCost, order.getProductCost(), 0.001) },
            Executable { Assertions.assertEquals(totalWeight, order.getTotalWeight(), 0.001) },
        )
    }

    @Test
    fun `subtractProduct _product, count_ with an existing product should correctly modify the amount of product in the order`() {
        val productAmount = 2
        order.subtractProduct(appleProduct, productAmount)
        val productCost: Double =
            appleProduct.price * (appleAmount - productAmount) + bananaProduct.price * bananaAmount + carrotProduct.price * carrotAmount
        val totalWeight: Double =
            appleProduct.weight * (appleAmount - productAmount) + bananaProduct.weight * bananaAmount + carrotProduct.weight * carrotAmount

        Assertions.assertAll(
            Executable { Assertions.assertEquals(appleAmount - productAmount, order.inventory[appleProduct]) },
            Executable { Assertions.assertEquals(productCost, order.getProductCost(), 0.001) },
            Executable { Assertions.assertEquals(totalWeight, order.getTotalWeight(), 0.001) },
        )
    }

    @Test
    fun `subtractProduct _product, amount_ with a product not in the order should not modify the order`() {
        val diceProduct = Product("Dice", 5.0, 0.1)
        val productAmount = 7
        order.subtractProduct(diceProduct, productAmount)
        val productCost: Double =
            appleProduct.price * appleAmount + bananaProduct.price * bananaAmount + carrotProduct.price * carrotAmount
        val totalWeight: Double =
            appleProduct.weight * appleAmount + bananaProduct.weight * bananaAmount + carrotProduct.weight * carrotAmount

        Assertions.assertAll(
            Executable { Assertions.assertFalse(order.inventory.containsKey(diceProduct)) },
            Executable { Assertions.assertEquals(productCost, order.getProductCost(), 0.001) },
            Executable { Assertions.assertEquals(totalWeight, order.getTotalWeight(), 0.001) },
        )
    }

    @Test
    fun `dispatch should pass all conditions`() {
        // kotlinx-coroutines-test.runBlockingTest has many issues reported
        // and does not seem to work for the time being, so the test must
        // wait for coroutine completion
        runBlocking {
            // Grouping all dispatch tests together to minimize coroutine delay
            val job = order.dispatch()
            Assertions.assertAll(
                // Before job completion
                Executable { `dispatch should prevent adding new products`() },
                Executable { `dispatch should prevent subtracting products`() },
                Executable { `dispatch should prevent dispatching again`() },
                Executable { `dispatch should set order status to Shipped`() },
            )
            job.join()
            Assertions.assertAll(
                // After job completion
                Executable { `dispatch should prevent adding new products`() },
                Executable { `dispatch should prevent subtracting products`() },
                Executable { `dispatch should prevent dispatching again`() },
                Executable { `dispatch should set order status to Delivered after the delivery delay`() },
            )
            Assertions.assertEquals(Order.OrderStatus.DELIVERED, order.getStatus())
        }
    }

    private fun `dispatch should prevent adding new products`() {
        Assertions.assertThrows(IllegalStateException::class.java) { order.addProduct(appleProduct) }
    }

    private fun `dispatch should prevent subtracting products`() {
        Assertions.assertThrows(IllegalStateException::class.java) { order.subtractProduct(appleProduct) }
    }

    private fun `dispatch should prevent dispatching again`() {
        Assertions.assertThrows(IllegalStateException::class.java) { order.dispatch() }
    }

    private fun `dispatch should set order status to Shipped`() {
        Assertions.assertEquals(Order.OrderStatus.SHIPPED, order.getStatus())
    }

    private fun `dispatch should set order status to Delivered after the delivery delay`() {
        Assertions.assertEquals(Order.OrderStatus.DELIVERED, order.getStatus())
    }

}