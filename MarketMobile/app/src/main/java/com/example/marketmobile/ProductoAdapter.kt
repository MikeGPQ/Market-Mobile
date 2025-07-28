package com.example.marketmobile

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ProductoAdapter(private val productos: List<Producto>) :
    RecyclerView.Adapter<ProductoAdapter.ProductoViewHolder>() {

    class ProductoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textCodigo: TextView = itemView.findViewById(R.id.text_codigo)
        val textDescripcion: TextView = itemView.findViewById(R.id.text_descripcion)
        val textCosto: TextView = itemView.findViewById(R.id.text_costo)
        val textExistencia: TextView = itemView.findViewById(R.id.text_existencia)
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
        holder.textCosto.text = "$${producto.Costo}"
        holder.textExistencia.text = "Existencia: ${producto.CantidadExistencia}"
    }

    override fun getItemCount(): Int = productos.size
}
