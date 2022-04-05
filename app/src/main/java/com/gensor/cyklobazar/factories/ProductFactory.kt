package com.gensor.cyklobazar.factories

import com.gensor.cyklobazar.models.bikes.EBike
import com.gensor.cyklobazar.models.bikes.MountainBike
import com.gensor.cyklobazar.models.bikes.RoadBike
import com.gensor.cyklobazar.models.parts.Fork
import com.gensor.cyklobazar.models.parts.Wheel

object ProductFactory {


    fun getEbike(data : HashMap<String, Any>) : EBike{
        val bike = EBikeBuilder.Builder()
            .brand(data["brand"] as String)
            .model(data["model"] as String)
            .year(data["year"] as Int)
            .price(data["price"] as Long)
            .motor(data["motor"] as String)
            .kw(data["kw"] as Int)
            .batteryCapacity(data["batteryCapacity"] as Int)
            .userId(data["userId"] as String)
            .build()
        return bike
    }

    fun getMountainBike(data : HashMap<String, Any>) = MountainBike(
        brand = data["brand"] as String,
        model = data["model"] as String,
        year= data["year"] as Int,
        price = data["price"] as Long,
        fork = data["fork"] as String,
        wheelSize = data["wheelSize"] as String,
        dropperPost = data["dropperPost"] as Boolean,
        userId = data["userId"] as String
    )

    fun getRoadBike(data : HashMap<String, Any>) = RoadBike(
        brand = data["brand"] as String,
        model = data["model"] as String,
        year = data["year"] as Int,
        price = data["price"] as Long,
        groupSet = data["groupSet"] as String,
        size = data["size"] as String,
        userId = data["userId"] as String
    )

    fun getFork(data : HashMap<String, Any>) = Fork(
        brand = data["brand"] as String,
        model = data["model"] as String,
        travel = data["travel"] as Int,
        price = data["price"] as Long,
        userId = data["userId"] as String
    )

    fun getWheel(data : HashMap<String, Any>) = Wheel(
        brand = data["brand"] as String,
        model = data["model"] as String,
        size = data["size"] as String,
        material = data["meterial"] as String,
        price = data["price"] as Long,
        userId = data["userId"] as String
    )

}