package com.example.marketmobile

import Producto
import ProductoAdapter
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.marketmobile.databinding.ActivityInventoryBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import android.content.Intent
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import android.view.MenuItem
import androidx.core.view.GravityCompat

class Inventory : AppCompatActivity() {
    private lateinit var binding: ActivityInventoryBinding
    private val database = Firebase.firestore
    private val productList = arrayListOf<Producto>()
    private val filteredList = arrayListOf<Producto>()
    private lateinit var adapter: ProductoAdapter
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInventoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        drawerLayout = findViewById(R.id.drawerLayout)
        navigationView = findViewById(R.id.navigationView)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.logo_app_ch_testing)

        navigationView.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_ventas -> true
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

        val menu = navigationView.menu
        menu.findItem(R.id.nav_inventario).isEnabled = false

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.btnVolverInventario.setOnClickListener {
            val intent = Intent(this, MenuPrincipal::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            startActivity(intent)
            finish()
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = ProductoAdapter(filteredList)
        binding.recyclerView.adapter = adapter

        binding.etBuscarCodigo.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) = aplicarBusquedaCodigo()
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        obtenerInventario()
    }

    private fun obtenerInventario() {
        database.collection("inventario").get().addOnSuccessListener { productos ->
            productList.clear()
            for (product in productos) {
                val producto = product.toObject(Producto::class.java)
                productList.add(producto)
            }
            aplicarBusquedaCodigo()
        }
    }

    private fun aplicarBusquedaCodigo() {
        val codigo = binding.etBuscarCodigo.text.toString().uppercase()

        val listaFiltrada = productList.filter {
            codigo.isEmpty() || it.Codigo.uppercase().contains(codigo)
        }

        filteredList.clear()
        filteredList.addAll(listaFiltrada)
        adapter.notifyDataSetChanged()
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
