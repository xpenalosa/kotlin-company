package functions

import dataclasses.City
import kotlin.math.abs

fun City.getDistanceTo(other: City): Int = abs(longitude - other.longitude) + abs(latitude - other.latitude)