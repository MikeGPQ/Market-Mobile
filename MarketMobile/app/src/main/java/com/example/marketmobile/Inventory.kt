package com.example.marketmobile

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.marketmobile.databinding.ActivityInventoryBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Inventory : AppCompatActivity() {
    private lateinit var binding: ActivityInventoryBinding
    private val database = Firebase.firestore
    private val productList = arrayListOf<Producto>()
    private lateinit var adapter: ProductoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInventoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = ProductoAdapter(productList)
        binding.recyclerView.adapter = adapter

        getInventoryData()
    }

    private fun getInventoryData() {
        val prod = database.collection("inventario")
        prod.get()
            .addOnSuccessListener { productos ->
                productList.clear()

                for (product in productos) {
                    val producto = product.toObject(Producto::class.java)
                    productList.add(producto)
                }

                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                binding.textTest.text = "Error: ${exception.message}"
            }
    }
}
