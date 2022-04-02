package com.gensor.cyklobazar.models

import java.math.BigDecimal

interface Product {
    fun getName() : String
    fun getPrice() : BigDecimal
}