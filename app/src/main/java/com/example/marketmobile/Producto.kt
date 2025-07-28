import java.io.Serializable

data class Producto(
    var CantidadExistencia: Int = 0,
    var CantidadMinima: Int = 0,
    var Categoria: String = "",
    var Codigo: String = "",
    var Costo: Double = 0.0,
    var Descripcion: String = "",
    var Descuento: Int = 0,
    var Imagen: String = "",
    var PrecioVenta: Double = 0.0,
    var Subcategoria: String = ""
) : Serializable
