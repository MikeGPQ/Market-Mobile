package com.example.marketmobile

import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.marketmobile.databinding.ActivityAuditoriaBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import android.content.Intent
import android.view.MenuItem
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Auditoria : AppCompatActivity() {

    private lateinit var binding: ActivityAuditoriaBinding
    private val database = Firebase.firestore
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuditoriaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        drawerLayout = findViewById(R.id.drawerLayout)
        navigationView = findViewById(R.id.navigationView)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.logo_app_ch_testing)

        navigationView.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_inventario -> {
                    startActivity(Intent(this, Inventory::class.java))
                    finish()
                    true
                }
                R.id.nav_ventas -> {
                    true
                }
                R.id.nav_usuarios -> {
                    startActivity(Intent(this, Usuarios::class.java))
                    finish()
                    true
                }
                R.id.nav_clientes -> {
                    startActivity(Intent(this, Clientes::class.java))
                    finish()
                    true
                }
                else -> false
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        cargarTablaAuditoria()

        binding.btnVolverAuditoria.setOnClickListener {
            finish()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                drawerLayout.openDrawer(GravityCompat.START)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun cargarTablaAuditoria() {
        val tabla = binding.tableAuditoria
        val encabezado = TableRow(this)
        encabezado.setBackgroundColor(0xFFE0E0E0.toInt())

        listOf("Correo Electrónico", "Fecha", "Hora", "Operación").forEach { texto ->
            val celda = TextView(this)
            celda.text = texto
            celda.setPadding(8, 8, 8, 8)
            celda.setTypeface(null, android.graphics.Typeface.BOLD)
            encabezado.addView(celda)
        }
        tabla.addView(encabezado)

        database.collection("auditoriasES")
            .get()
            .addOnSuccessListener { resultado ->
                for (doc in resultado) {
                    val fila = TableRow(this)

                    listOf(
                        doc.getString("email") ?: "",
                        doc.getString("fecha") ?: "",
                        doc.getString("hora") ?: "",
                        doc.getString("tipo") ?: ""
                    ).forEach { dato ->
                        val celda = TextView(this)
                        celda.text = dato
                        celda.setPadding(8, 8, 8, 8)
                        celda.gravity = Gravity.CENTER
                        fila.addView(celda)
                    }
                    tabla.addView(fila)
                }
            }
    }
}
