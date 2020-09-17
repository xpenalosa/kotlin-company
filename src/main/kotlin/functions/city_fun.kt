package functions

import dataclasses.City

fun City.getDistanceTo(other: City): Double = if (this == other) 0.0 else 1.0