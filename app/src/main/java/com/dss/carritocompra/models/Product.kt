package com.dss.carritocompra.models

data class Product(
    var id: Long,     // Actualizada en el fetchProducts()
    val name: String,
    val price: Double,
    val _links: Links // Links para mapear _links
)

// Clases de datos para obtener la id del producto
// extraída del enlace dado por la api .../product/{id}
data class Links(
    val self: Href
)

data class Href(
    val href: String
)
