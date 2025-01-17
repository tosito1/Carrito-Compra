package com.dss.carritocompra.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dss.carritocompra.R
import com.dss.carritocompra.models.Product
import com.squareup.picasso.Picasso

class CartAdapter(private var products: MutableList<Product>, private val onRemoveClick: (Product) -> Unit) :
    RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    // Crear el ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cart_product_item, parent, false)
        return CartViewHolder(view)
    }

    // Vincular los datos con la vista
    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val product = products[position]

        holder.productName.text = product.name
        holder.productPrice.text = "€ ${product.price}"

        // Agregar el listener de eliminar producto
        holder.removeButton.setOnClickListener {
            onRemoveClick(product)
        }
    }

    // Retornar el número de productos
    override fun getItemCount(): Int {
        return products.size
    }

    fun updateProducts(newProducts: List<Product>) {
        products.clear()
        products.addAll(newProducts)
        notifyDataSetChanged() // Notificar que todos los datos han cambiado
    }

    // Método para eliminar un producto
    fun removeProduct(product: Product) {
        val position = products.indexOf(product)
        if (position >= 0) {
            products.removeAt(position)
            notifyItemRemoved(position) // Notificar al RecyclerView que se ha eliminado un item
            notifyItemRangeChanged(position, itemCount) // Actualizar el rango
        }
    }

    // ViewHolder para manejar los elementos de la lista
    class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productName: TextView = itemView.findViewById(R.id.product_name)
        val productPrice: TextView = itemView.findViewById(R.id.product_price)
        val removeButton: ImageView = itemView.findViewById(R.id.remove_from_cart_button)
    }
}
