package com.example.marketmobile

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.marketmobile.databinding.ActivityUsuariosBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import android.content.Intent
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import android.view.MenuItem
import androidx.core.view.GravityCompat

class Usuarios : AppCompatActivity() {
    private lateinit var binding: ActivityUsuariosBinding
    private val usuariosList = mutableListOf<Pair<String, Usuario>>()
    private val filteredList = mutableListOf<Pair<String, Usuario>>()
    private lateinit var adapter: UsuarioAdapter
    private val database = Firebase.firestore
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUsuariosBinding.inflate(layoutInflater)
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
                R.id.nav_clientes -> {
                    startActivity(Intent(this, Clientes::class.java))
                    finish()
                    true
                }
                else -> false
            }
        }

        val menu = navigationView.menu
        menu.findItem(R.id.nav_usuarios).isEnabled = false

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.btnAuditoriaGeneral.setOnClickListener {
            val intent = Intent(this, Auditoria::class.java)
            startActivity(intent)
        }

        binding.btnVolverUsuarios.setOnClickListener {
            val intent = Intent(this, MenuPrincipal::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            startActivity(intent)
            finish()
        }


        adapter = UsuarioAdapter(filteredList)
        binding.recyclerUsuarios.layoutManager = LinearLayoutManager(this)
        binding.recyclerUsuarios.adapter = adapter


        binding.etBuscarCodigo.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) = aplicarBusquedaNombre()
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        obtenerUsuarios()
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

    private fun obtenerUsuarios() {
        database.collection("usuarios")
            .get()
            .addOnSuccessListener { resultado ->
                usuariosList.clear()
                for (doc in resultado) {
                    val usuario = doc.toObject(Usuario::class.java)
                    usuariosList.add(Pair(doc.id, usuario))
                }
                aplicarBusquedaNombre()
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Error al obtener usuarios", e)
            }
    }

    private fun aplicarBusquedaNombre() {
        val nombre = binding.etBuscarCodigo.text.toString().uppercase()

        val listaFiltrada = usuariosList.filter {
            nombre.isEmpty() ||
                    it.second.Nombre?.uppercase()?.contains(nombre) == true
        }

        filteredList.clear()
        filteredList.addAll(listaFiltrada)
        adapter.notifyDataSetChanged()
    }
}