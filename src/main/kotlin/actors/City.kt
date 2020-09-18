package actors

import kotlin.math.abs

data class City(val id: Long, val name: String, val latitude: Int, val longitude: Int){

    fun getDistanceTo(other: City): Int = abs(longitude - other.longitude) + abs(latitude - other.latitude)
    fun getDeliveryDelayTo(other: City): Int = if (this == other) 1 else getDistanceTo(other)
}