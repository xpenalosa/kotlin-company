package factories

import java.util.concurrent.atomic.AtomicLong

abstract class BaseFactory {
    val nextId: AtomicLong = AtomicLong(0)

    abstract fun instantiate(vararg args: Any): Any
}