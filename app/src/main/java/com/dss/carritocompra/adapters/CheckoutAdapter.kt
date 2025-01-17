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

class CheckoutAdapter(private val products: List<Product>) :
    RecyclerView.Adapter<CheckoutAdapter.CheckoutViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CheckoutViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cart_product_item, parent, false)
        return CheckoutViewHolder(view)
    }

    override fun onBindViewHolder(holder: CheckoutViewHolder, position: Int) {
        val product = products[position]

        // Vincular datos
        holder.productName.text = product.name
        holder.productPrice.text = "€ ${product.price}"
    }

    override fun getItemCount(): Int {
        return products.size
    }

    class CheckoutViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productName: TextView = itemView.findViewById(R.id.product_name)
        val productPrice: TextView = itemView.findViewById(R.id.product_price)
    }
}
