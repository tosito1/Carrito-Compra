package com.dss.carritocompra.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dss.carritocompra.R
import com.dss.carritocompra.models.Product
import com.dss.carritocompra.models.ProductsResponse
import com.dss.carritocompra.utils.CartManager
import retrofit2.Call

class ProductAdapter(
    private val productList: MutableList<Product>,
    private val onAddToCartClick: (Product) -> Unit
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewName: TextView = itemView.findViewById(R.id.textViewName)
        val textViewPrice: TextView = itemView.findViewById(R.id.textViewPrice)
        val buttonAddToCart: Button = itemView.findViewById(R.id.buttonAddToCart)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.product_item, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = productList[position]
        holder.textViewName.text = product.name
        holder.textViewPrice.text = "$${product.price}"

        // Configurar el botón de agregar al carrito
        holder.buttonAddToCart.setOnClickListener {
            onAddToCartClick(product)
        }
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    fun removeProduct(product: Product) {
        val position = productList.indexOf(product)
        if (position != -1) {
            productList.removeAt(position)
            notifyItemRemoved(position)
        }
    }
}

