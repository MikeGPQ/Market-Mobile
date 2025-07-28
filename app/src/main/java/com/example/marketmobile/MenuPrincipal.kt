package com.example.marketmobile

import android.app.Dialog
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.graphics.Color
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class MenuPrincipal : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_principal)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val currentUser = FirebaseAuth.getInstance().currentUser
        val correo = currentUser?.email ?: "Usuario desconocido"
        findViewById<TextView>(R.id.tvCorreoUsuario).text = correo

        findViewById<ImageView>(R.id.btnLogout).setOnClickListener {
            recordLoginEvent(currentUser?.email.toString())
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this, LogIn::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        findViewById<LinearLayout>(R.id.btnInventario).setOnClickListener {
            startActivity(Intent(this, Inventory::class.java))
        }

        findViewById<LinearLayout>(R.id.btnUsuarios).setOnClickListener {
            startActivity(Intent(this, Usuarios::class.java))
        }

        findViewById<LinearLayout>(R.id.btnClientes).setOnClickListener {
            startActivity(Intent(this, Clientes::class.java))
        }

        findViewById<TextView>(R.id.tvTermsLink).setOnClickListener {
            showTermsDialog()
        }

        // btnVentas aún sin funcionalidad
    }

    private fun showTermsDialog() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_terms)


        dialog.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        dialog.window?.setBackgroundDrawable(ColorDrawable())


        dialog.findViewById<Button>(R.id.btnClose).setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun recordLoginEvent(email: String) {
        val db = FirebaseFirestore.getInstance()
        val current = LocalDateTime.now()

        val fecha = current.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
        val hora = current.format(DateTimeFormatter.ofPattern("HH:mm:ss"))

        val loginData = hashMapOf(
            "email" to email,
            "fecha" to fecha,
            "hora" to hora,
            "tipo" to "Salida"
        )

        db.collection("auditoriasES")
            .add(loginData)
    }
}
