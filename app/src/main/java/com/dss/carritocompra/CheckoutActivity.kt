package com.dss.carritocompra

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dss.carritocompra.R
import com.dss.carritocompra.utils.CartManager

class CheckoutActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)

        val textViewTotal: TextView = findViewById(R.id.textViewTotal)
        val buttonConfirmPurchase: Button = findViewById(R.id.buttonConfirmPurchase)

        // Calcular el total
        val total = CartManager.getCartItems().sumOf { it.price }
        textViewTotal.text = "Total: $%.2f".format(total)

        // Manejar la confirmación de compra
        buttonConfirmPurchase.setOnClickListener {
            if (CartManager.getCartItems().isNotEmpty()) {
                Toast.makeText(this, "Compra realizada con éxito", Toast.LENGTH_LONG).show()

                // Vaciar el carrito
                CartManager.clearCart()

                // Finalizar la actividad
                finish()
            } else {
                Toast.makeText(this, "El carrito está vacío", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
