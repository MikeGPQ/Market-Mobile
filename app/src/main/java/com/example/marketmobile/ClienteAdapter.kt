package com.example.marketmobile

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ClienteAdapter(
    private val clientes: List<Pair<String, Cliente>>
) : RecyclerView.Adapter<ClienteAdapter.ClienteViewHolder>() {

    class ClienteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgCliente: ImageView = itemView.findViewById(R.id.imgCliente)
        val tvIdCliente: TextView = itemView.findViewById(R.id.tvIdCliente)
        val tvNombreCliente: TextView = itemView.findViewById(R.id.tvNombreCliente)
        val tvPuntosCliente: TextView = itemView.findViewById(R.id.tvPuntosCliente)
        val tvEstadoCliente: TextView = itemView.findViewById(R.id.tvEstadoCliente)
        val btnVerMas: ImageView = itemView.findViewById(R.id.btnVerMas)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClienteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_cliente, parent, false)
        return ClienteViewHolder(view)
    }

    override fun onBindViewHolder(holder: ClienteViewHolder, position: Int) {
        val (id, cliente) = clientes[position]
        holder.tvIdCliente.text = "ID: $id"
        holder.tvNombreCliente.text = cliente.Nombre
        holder.tvPuntosCliente.text = "Puntos: ${cliente.Puntos}"
        holder.tvEstadoCliente.text = if (cliente.Habilitado) "Estado: Activo" else "Estado: Deshabilitado"
        holder.imgCliente.setImageResource(R.drawable.ic_cliente)

        holder.btnVerMas.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, DetallesCliente::class.java)
            intent.putExtra("id", id)
            intent.putExtra("nombre", cliente.Nombre)
            intent.putExtra("correo", cliente.Correo)
            intent.putExtra("telefono", cliente.Telefono)
            intent.putExtra("tarjeta", cliente.Tarjeta)
            intent.putExtra("puntos", cliente.Puntos)
            intent.putExtra("habilitado", cliente.Habilitado)
            intent.putExtra("fecha", cliente.Fecha)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = clientes.size
}