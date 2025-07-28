package com.example.marketmobile

data class Producto(
    var CantidadExistencia: Int = 0,
    var Codigo: String = "",
    var Costo: Double = 0.0,
    var Descripcion: String = ""
) {
    constructor() : this(0, "", 0.0, "")
}