package actors.product

class Inventory: HashMap<Product, Int>() {

    fun addProduct(product: Product, amount: Int = 1) = this.replace(product, amount + this[product]!!)
    fun bulkAddProduct(other: HashMap<Product, Int>) = other.forEach{ this.addProduct(it.key, it.value)}
    fun subtractProduct(product: Product, amount: Int = 1) = this.addProduct(product, -amount)
    fun bulkSubtractProduct(other: HashMap<Product, Int>) = other.forEach{ this.subtractProduct(it.key, it.value)}

}
