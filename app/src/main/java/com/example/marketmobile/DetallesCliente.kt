package com.example.marketmobile

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.marketmobile.databinding.ActivityDetallesClienteBinding
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import android.content.Intent
import android.view.MenuItem
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class DetallesCliente : AppCompatActivity() {
    private lateinit var binding: ActivityDetallesClienteBinding
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetallesClienteBinding.inflate(layoutInflater)
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

        binding.tvNombreCliente.text = intent.getStringExtra("nombre")
        binding.tvIdCliente.text = "ID: ${intent.getStringExtra("id")}"
        binding.tvCorreo.text = "Correo: ${intent.getStringExtra("correo")}"
        binding.tvTelefono.text = "Teléfono: ${intent.getStringExtra("telefono")}"
        binding.tvTarjeta.text = "Tarjeta: ${intent.getStringExtra("tarjeta")}"
        binding.tvPuntos.text = "Puntos: ${intent.getStringExtra("puntos")}"
        binding.tvEstado.text = "Estado: " + if (intent.getBooleanExtra("habilitado", true)) "Activo" else "Deshabilitado"
        binding.tvFecha.text = "Fecha de alta: ${intent.getStringExtra("fecha")}"

        binding.btnVolver.setOnClickListener { finish() }
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
}
