package com.example.kiumi

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class OrderAdapter(
    private var orderItems:  MutableList<OrderItem>
) : RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {

    inner class OrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.image)
        val name: TextView = itemView.findViewById(R.id.name)
        val price: TextView = itemView.findViewById(R.id.price)
        val calories: TextView = itemView.findViewById(R.id.calories)
        val buttonDecrease: Button = itemView.findViewById(R.id.button_decrease)
        val buttonIncrease: Button = itemView.findViewById(R.id.button_increase)
        val itemCount: TextView = itemView.findViewById(R.id.item_count)
        val cancelButton: Button = itemView.findViewById(R.id.cancel_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.order_item, parent, false)
        return OrderViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val orderItem = orderItems[position]
        val menuItem = orderItem.menuItem
        holder.image.setImageResource(menuItem.imageResourceId)
        holder.name.text = menuItem.name
        holder.calories.text = menuItem.calories
        holder.itemCount.text = orderItem.quantity.toString()

        val itemPrice = menuItem.price.replace(",", "").replace("₩", "").toInt()
        holder.price.text = "₩${itemPrice * orderItem.quantity}"

        holder.buttonDecrease.setOnClickListener {
            if (orderItem.quantity > 1) {
                orderItem.quantity--
                holder.itemCount.text = orderItem.quantity.toString()
                holder.price.text = "₩${itemPrice * orderItem.quantity}"
                notifyItemChanged(position)
            }
        }

        holder.buttonIncrease.setOnClickListener {
            orderItem.quantity++
            holder.itemCount.text = orderItem.quantity.toString()
            holder.price.text = "₩${itemPrice * orderItem.quantity}"
            notifyItemChanged(position)
        }

        holder.cancelButton.setOnClickListener {
            orderItems.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, orderItems.size)
        }
    }

    override fun getItemCount() = orderItems.size

    fun updateOrderItems(newOrderItems: List<OrderItem>) {
        orderItems = newOrderItems.toMutableList()
        notifyDataSetChanged()
    }
}
