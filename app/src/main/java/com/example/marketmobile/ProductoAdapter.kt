import android.content.Intent
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.marketmobile.DetallesProducto
import com.example.marketmobile.R

class ProductoAdapter(private val productos: List<Producto>) :
    RecyclerView.Adapter<ProductoAdapter.ProductoViewHolder>() {

    class ProductoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgProducto: ImageView = itemView.findViewById(R.id.imgProducto)
        val textCodigo: TextView = itemView.findViewById(R.id.text_codigo)
        val textDescripcion: TextView = itemView.findViewById(R.id.text_descripcion)
        val textPrecioFinal: TextView = itemView.findViewById(R.id.text_precio_final)
        val textExistencia: TextView = itemView.findViewById(R.id.text_existencia)
        val btnDetalles: ImageView = itemView.findViewById(R.id.btnDetalles)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_producto, parent, false)
        return ProductoViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductoViewHolder, position: Int) {
        val producto = productos[position]
        holder.textCodigo.text = producto.Codigo
        holder.textDescripcion.text = producto.Descripcion

        val descuentoPorcentaje = producto.Descuento
        val precioFinal = producto.PrecioVenta - (producto.PrecioVenta * (descuentoPorcentaje / 100.0))
        holder.textPrecioFinal.text = "$${String.format("%.2f", precioFinal)}"

        holder.textExistencia.text = "Existencia: ${producto.CantidadExistencia}"
        if (producto.CantidadExistencia <= producto.CantidadMinima) {
            holder.textExistencia.setTextColor(holder.itemView.context.getColor(R.color.rojo_advertencia))
        }

        try {
            if (producto.Imagen.isNotEmpty()) {
                val imageBytes = Base64.decode(producto.Imagen, Base64.DEFAULT)
                val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                holder.imgProducto.setImageBitmap(bitmap)
            } else {
                holder.imgProducto.setImageResource(R.drawable.ic_launcher_background)
            }
        } catch (e: Exception) {
            holder.imgProducto.setImageResource(R.drawable.ic_launcher_background)
        }

        holder.btnDetalles.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, DetallesProducto::class.java)
            intent.putExtra("producto", producto)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = productos.size
}
