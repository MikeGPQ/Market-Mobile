package com.example.marketmobile

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class UsuarioAdapter(
    private val usuarios: List<Pair<String, Usuario>>
) : RecyclerView.Adapter<UsuarioAdapter.UsuarioViewHolder>() {

    class UsuarioViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgRol: ImageView = itemView.findViewById(R.id.imgRol)
        val tvId: TextView = itemView.findViewById(R.id.tvId)
        val tvNombre: TextView = itemView.findViewById(R.id.tvNombre)
        val tvCorreo: TextView = itemView.findViewById(R.id.tvCorreo)
        val tvEstado: TextView = itemView.findViewById(R.id.tvEstado)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsuarioViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_usuario, parent, false)
        return UsuarioViewHolder(view)
    }

    override fun onBindViewHolder(holder: UsuarioViewHolder, position: Int) {
        val (id, usuario) = usuarios[position]
        holder.tvId.text = "ID: $id"
        holder.tvNombre.text = usuario.Nombre
        holder.tvCorreo.text = usuario.Correo
        holder.tvEstado.text = if (usuario.Habilitado) "Estado: Activo" else "Estado: Deshabilitado"

        val imagenRol = if (usuario.Rol.lowercase() == "administrador") {
            R.drawable.ic_administrador
        } else {
            R.drawable.ic_usuario
        }
        holder.imgRol.setImageResource(imagenRol)
    }

    override fun getItemCount(): Int = usuarios.size
}
