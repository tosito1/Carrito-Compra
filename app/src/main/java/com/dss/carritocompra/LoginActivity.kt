package com.dss.carritocompra

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dss.carritocompra.api.ApiClient
import com.dss.carritocompra.api.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    private lateinit var apiService: ApiService
    private lateinit var usernameInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var loginButton: Button
    private lateinit var logoutButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        apiService = ApiClient.retrofit.create(ApiService::class.java)

        // Inicializar los elementos de la interfaz
        usernameInput = findViewById(R.id.usernameInput)
        passwordInput = findViewById(R.id.passwordInput)
        loginButton = findViewById(R.id.loginButton)
        logoutButton = findViewById(R.id.logoutButton)

        // Comprobar si el usuario está logueado
        if (isLoggedIn()) {
            // Si el usuario está logueado, mostrar el botón de logout
            showLogoutButton()
        } else {
            // Si el usuario no está logueado, mostrar el formulario de login
            showLoginForm()
        }

        // Configurar el botón de login
        loginButton.setOnClickListener {
            val username = usernameInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()

            if (username.isNotEmpty() && password.isNotEmpty()) {
                performLogin(username, password)
            } else {
                Toast.makeText(this, "Por favor, llena todos los campos", Toast.LENGTH_SHORT).show()
            }
        }

        // Configurar el botón de logout
        logoutButton.setOnClickListener {
            performLogout()
        }
    }

    private fun performLogin(username: String, password: String) {
        apiService.login(username, password).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    // Guardar el token en SharedPreferences
                    val token = "some_token"  // Aquí usarías el token real si lo obtienes de la API
                    saveToken(token)

                    Toast.makeText(this@LoginActivity, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show()
                    showLogoutButton()  // Mostrar el botón de logout
                } else {
                    Toast.makeText(this@LoginActivity, "Credenciales incorrectas", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(this@LoginActivity, "Error de red: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun performLogout() {
        // Eliminar el token de SharedPreferences
        val sharedPreferences = getSharedPreferences("auth_prefs", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.remove("auth_token")
        editor.apply()

        Toast.makeText(this@LoginActivity, "Sesión cerrada", Toast.LENGTH_SHORT).show()
        showLoginForm()  // Volver a mostrar el formulario de login
    }

    private fun saveToken(token: String) {
        val sharedPreferences = getSharedPreferences("auth_prefs", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("auth_token", token)
        editor.apply()
    }

    private fun isLoggedIn(): Boolean {
        val sharedPreferences = getSharedPreferences("auth_prefs", MODE_PRIVATE)
        return sharedPreferences.contains("auth_token")  // Si el token está presente, el usuario está logueado
    }

    private fun showLoginForm() {
        usernameInput.visibility = android.view.View.VISIBLE
        passwordInput.visibility = android.view.View.VISIBLE
        loginButton.visibility = android.view.View.VISIBLE
        logoutButton.visibility = android.view.View.GONE
    }

    private fun showLogoutButton() {
        usernameInput.visibility = android.view.View.GONE
        passwordInput.visibility = android.view.View.GONE
        loginButton.visibility = android.view.View.GONE
        logoutButton.visibility = android.view.View.VISIBLE
    }
}
