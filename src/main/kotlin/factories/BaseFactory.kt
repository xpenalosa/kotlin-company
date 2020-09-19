package factories

import java.util.concurrent.atomic.AtomicLong

abstract class BaseFactory {
    val nextId: AtomicLong = AtomicLong(0)
    val createdObjects: HashMap<Long, Any> = HashMap()

    abstract fun instantiate(vararg args: Any): Any
    fun getObjectWithId(id: Long): Any? = createdObjects[id]
}