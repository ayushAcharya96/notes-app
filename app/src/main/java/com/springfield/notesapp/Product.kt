package com.springfield.notesapp

import java.io.Serializable


data class Product(
        val name: String?,
        val price: Double?,
        val isAvailable: Boolean?
) {
}