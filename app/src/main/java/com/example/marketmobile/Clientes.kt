package com.example.marketmobile

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.marketmobile.databinding.ActivityClientesBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import android.content.Intent
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import android.view.MenuItem
import androidx.core.view.GravityCompat

class Clientes : AppCompatActivity() {
    private lateinit var binding: ActivityClientesBinding
    private val clientesList = mutableListOf<Pair<String, Cliente>>()
    private val filteredList = mutableListOf<Pair<String, Cliente>>()
    private lateinit var adapter: ClienteAdapter
    private val database = Firebase.firestore
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClientesBinding.inflate(layoutInflater)
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
                else -> false
            }
        }

        val menu = navigationView.menu
        menu.findItem(R.id.nav_clientes).isEnabled = false

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.btnVolverCliente.setOnClickListener {
            val intent = Intent(this, MenuPrincipal::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            startActivity(intent)
            finish()
        }

        adapter = ClienteAdapter(filteredList)
        binding.recyclerClientes.layoutManager = LinearLayoutManager(this)
        binding.recyclerClientes.adapter = adapter

        binding.etBuscarCodigo.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) = aplicarBusquedaNombre()
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        obtenerClientes()
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

    private fun obtenerClientes() {
        database.collection("clientes")
            .get()
            .addOnSuccessListener { resultado ->
                clientesList.clear()
                for (doc in resultado) {
                    val cliente = doc.toObject(Cliente::class.java)
                    clientesList.add(Pair(doc.id, cliente))
                }
                aplicarBusquedaNombre()
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Error al obtener clientes", e)
            }
    }

    private fun aplicarBusquedaNombre() {
        val nombre = binding.etBuscarCodigo.text.toString().uppercase()

        val listaFiltrada = clientesList.filter {
            nombre.isEmpty() ||
                    it.second.Nombre.uppercase().contains(nombre)
        }

        filteredList.clear()
        filteredList.addAll(listaFiltrada)
        adapter.notifyDataSetChanged()
    }
}