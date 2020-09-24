package actors

import java.time.Duration
import kotlin.math.abs

data class City(val id: Long, val name: String, val latitude: Int, val longitude: Int) {

    companion object {
        private var delayPerDistanceUnit: Duration = Duration.ofSeconds(1)

        private fun getDeliveryForDistance(distance: Int): Int = (delayPerDistanceUnit.toMillis() * distance).toInt()
    }

    fun getDistanceTo(other: City): Int = abs(longitude - other.longitude) + abs(latitude - other.latitude)
    fun getDeliveryDelayTo(other: City): Int = getDeliveryForDistance(getDistanceTo(other))
}