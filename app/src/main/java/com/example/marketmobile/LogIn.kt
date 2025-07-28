package com.example.marketmobile

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.marketmobile.databinding.ActivityLogInBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class LogIn : AppCompatActivity() {

    private lateinit var binding: ActivityLogInBinding
    private lateinit var auth: FirebaseAuth

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLogInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val textWatcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val correo = binding.email.text.toString().trim()
                val password = binding.password.text.toString().trim()
                binding.continueBtn.isEnabled = correo.isNotEmpty() && password.isNotEmpty()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        }

        binding.email.addTextChangedListener(textWatcher)
        binding.password.addTextChangedListener(textWatcher)

        binding.continueBtn.setOnClickListener {
            val email = binding.email.text.toString().trim()
            val password = binding.password.text.toString().trim()

            binding.tvError.text = ""
            binding.tvError.visibility = android.view.View.GONE

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val intent = Intent(this, MenuPrincipal::class.java)
                        recordLoginEvent(email)
                        startActivity(intent)
                        finish()
                    } else {
                        val firebaseError = task.exception?.message ?: ""

                        val errorMsg = when {
                            firebaseError.contains("badly formatted", ignoreCase = true) ->
                                "El correo electrónico no tiene un formato válido."

                            firebaseError.contains("auth credential is incorrect", ignoreCase = true) ||
                                    firebaseError.contains("malformed", ignoreCase = true) ||
                                    firebaseError.contains("has expired", ignoreCase = true) ->
                                "Correo o contraseña incorrectos."

                            else -> "Ocurrió un error al iniciar sesión."
                        }

                        binding.tvError.text = errorMsg
                        binding.tvError.visibility = android.view.View.VISIBLE
                        Log.e("LoginError", task.exception?.message ?: "Error desconocido")
                    }
                }
        }
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val intent = Intent(this, MenuPrincipal::class.java)
            startActivity(intent)
            finish()
        }
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
            "tipo" to "Entrada"
        )

        db.collection("auditoriasES")
            .add(loginData)
    }
}