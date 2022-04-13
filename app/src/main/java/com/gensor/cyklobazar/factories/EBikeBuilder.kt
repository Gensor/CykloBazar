package com.gensor.cyklobazar.factories

import com.gensor.cyklobazar.models.bikes.EBike

class EBikeBuilder private constructor(
    val brand: String?,
    val model: String?,
    val year: Int?,
    val price: Long?,
    val motor: String?,
    val power: Int?,
    val batteryCapacity: Int?,
    val userId: String?,
    val image: String?
) {

    data class Builder(
        var brand: String = "",
        var model: String = "",
        var year: Int = 0,
        var price: Long = 0L,
        var motor: String = "",
        var power: Int = 0,
        var batteryCapacity: Int = 0,
        var userId : String = "",
        var image: String = ""
    ) {
        fun brand(brand: String) = apply { this.brand = brand }
        fun model(model: String) = apply { this.model = model }
        fun year(year: Int) = apply { this.year = year }
        fun price(price : Long) = apply { this.price = price }
        fun motor(motor: String) = apply { this.motor = motor }
        fun kw(power : Int) = apply { this.power = power }
        fun batteryCapacity(batteryCapacity : Int) = apply { this.batteryCapacity = batteryCapacity }
        fun userId(userId: String) = apply { this.userId = userId }
        fun image(imageUrl : String) = apply { this.image = imageUrl }

        fun build() = EBike("", brand, model, year, price, motor, power, batteryCapacity, userId, image)
    }
}