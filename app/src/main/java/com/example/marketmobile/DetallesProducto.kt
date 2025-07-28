package com.example.marketmobile

import Producto
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.marketmobile.databinding.ActivityDetallesProductoBinding
import com.google.android.material.navigation.NavigationView

class DetallesProducto : AppCompatActivity() {
    private lateinit var binding: ActivityDetallesProductoBinding
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetallesProductoBinding.inflate(layoutInflater)
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

        val producto = intent.getSerializableExtra("producto") as Producto

        binding.tvNombre.text = producto.Descripcion
        binding.tvCodigo.text = "Código: ${producto.Codigo}"
        binding.tvCategoria.text = "Categoría: ${producto.Categoria}"
        binding.tvSubcategoria.text = if (producto.Subcategoria.isNullOrEmpty()) {
            "Subcategoría: Sin subcategoría"
        } else {
            "Subcategoría: ${producto.Subcategoria}"
        }
        binding.tvCantidad.text = "Existencia: ${producto.CantidadExistencia}"
        binding.tvMinima.text = "Cantidad Mínima: ${producto.CantidadMinima}"
        binding.tvCosto.text = "Costo: $${producto.Costo}"
        binding.tvPrecioOriginal.text = "Precio Venta: $${producto.PrecioVenta}"

        val descuentoPorcentaje = producto.Descuento
        val precioFinal = producto.PrecioVenta - (producto.PrecioVenta * (descuentoPorcentaje / 100.0))

        binding.tvDescuento.text = "Descuento: ${descuentoPorcentaje}%"
        binding.tvPrecioFinal.text = "Precio Final: $${String.format("%.2f", precioFinal)}"

        try {
            if (producto.Imagen.isNotEmpty()) {
                val imageBytes = Base64.decode(producto.Imagen, Base64.DEFAULT)
                val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                binding.imgProducto.setImageBitmap(bitmap)
            }
        } catch (e: Exception) {}

        binding.btnVolver.setOnClickListener {
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
}