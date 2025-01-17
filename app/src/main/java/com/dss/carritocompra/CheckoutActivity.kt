package com.dss.carritocompra

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dss.carritocompra.adapters.CheckoutAdapter
import com.dss.carritocompra.api.ApiService
import com.dss.carritocompra.models.Product
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class CheckoutActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var checkoutAdapter: CheckoutAdapter
    private lateinit var textViewTotal: TextView
    private lateinit var apiService: ApiService
    private lateinit var buttonConfirmPurchase: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)

        // Recuperar los productos del carrito
        val cartProducts = intent.getSerializableExtra("cartProducts") as? ArrayList<Product> ?: arrayListOf()

        // Configurar RecyclerView
        recyclerView = findViewById(R.id.recyclerViewCartItems)
        recyclerView.layoutManager = LinearLayoutManager(this)
        checkoutAdapter = CheckoutAdapter(cartProducts)
        recyclerView.adapter = checkoutAdapter

        // Configurar el total
        textViewTotal = findViewById(R.id.textViewTotal)
        val total = cartProducts.sumOf { it.price }
        textViewTotal.text = "Total: €%.2f".format(total)

        // Botón para confirmar la compra
        buttonConfirmPurchase = findViewById(R.id.buttonConfirmPurchase)
        buttonConfirmPurchase.setOnClickListener {
            if (cartProducts.isNotEmpty()) {
                // Llamada a la API para limpiar el carrito en el servidor
                apiService.clearCart().enqueue(object : Callback<Void> {
                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        if (response.isSuccessful) {
                            Toast.makeText(this@CheckoutActivity, "Compra realizada con éxito", Toast.LENGTH_LONG).show()

                            // Vaciar la lista local de productos
                            cartProducts.clear()
                            checkoutAdapter.notifyDataSetChanged()

                            // Opcional: cerrar la actividad o mostrar una pantalla de agradecimiento
                            finish()
                        } else {
                            Toast.makeText(this@CheckoutActivity, "Error al limpiar el carrito en el servidor", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        Toast.makeText(this@CheckoutActivity, "Error de red: ${t.message}", Toast.LENGTH_SHORT).show()
                    }
                })
            } else {
                Toast.makeText(this, "El carrito está vacío", Toast.LENGTH_SHORT).show()
            }
        }

    }
}