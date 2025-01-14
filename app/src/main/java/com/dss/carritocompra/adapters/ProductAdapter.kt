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

class ProductAdapter(private val products: List<Product>, private val onAddToCartClick: (Product) -> Unit) :
    RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    // Método para crear un nuevo ViewHolder cuando es necesario
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.product_item, parent, false)
        return ProductViewHolder(view)
    }

    // Método para vincular datos a cada ítem del RecyclerView
    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = products[position]

        // Vincular los datos al layout
        holder.productName.text = product.name
        holder.productPrice.text = "€ ${product.price}"

        // Comprobar si la URL de la imagen es válida
        val imageUrl = "android.resource://${holder.itemView.context.packageName}/drawable/ic_product_placeholder"

        // Cargar la imagen del producto o la imagen predeterminada usando Picasso
        Picasso.get().load(imageUrl).into(holder.productImage)

        // Configurar el botón para agregar al carrito
        holder.addToCartButton.setOnClickListener {
            onAddToCartClick(product)
        }
    }

    // Método para obtener el número total de elementos en la lista
    override fun getItemCount(): Int {
        return products.size
    }

    // ViewHolder para manejar la vista de cada producto en el RecyclerView
    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productImage: ImageView = itemView.findViewById(R.id.product_image)
        val productName: TextView = itemView.findViewById(R.id.product_name)
        val productPrice: TextView = itemView.findViewById(R.id.product_price)
        val addToCartButton: ImageView = itemView.findViewById(R.id.add_to_cart_button)
    }
}
