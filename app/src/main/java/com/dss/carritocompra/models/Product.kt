package com.dss.carritocompra.models

import java.io.Serializable

data class Product(
    var id: Long,
    val name: String,
    val price: Double,
    val _links: Links
) : Serializable

// Clases de datos para obtener la id del producto
// extraída del enlace dado por la api .../product/{id}
data class Links(
    val self: Href
) : Serializable

data class Href(
    val href: String
) : Serializable