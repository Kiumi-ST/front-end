package com.example.kiumi

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class OrderAdapter(
    private var orderItems:  MutableList<OrderItem>,
    private val onRemoveItem: (OrderItem) -> Unit
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
        val setSide: TextView = itemView.findViewById(R.id.set_side)
        val buttonSet: TextView = itemView.findViewById(R.id.button_set)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.order_item, parent, false)
        return OrderViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val orderItem = orderItems[position]
        val menuItem = orderItem.menuItem

        holder.itemCount.text = orderItem.quantity.toString()

        if (orderItem.isSet) {
            holder.image.setImageResource(R.drawable.ic_burger_set)
            holder.name.text = "${menuItem.name} - ${if (orderItem.isLargeSet) "라지세트" else "세트"}"
            holder.calories.text = orderItem.totalCalories
            holder.setSide.visibility = View.VISIBLE
            holder.buttonSet.visibility = View.VISIBLE
            holder.setSide.text = "${orderItem.drink}, ${orderItem.side}"
        } else {
            holder.image.setImageResource(menuItem.imageResourceId)
            holder.name.text = menuItem.name
            holder.calories.text = menuItem.calories
            holder.setSide.visibility = View.GONE
            holder.buttonSet.visibility = View.GONE
        }

        val itemPrice = if (orderItem.isSet) {
            orderItem.totalPrice.replace(",", "").replace("₩", "").toInt()
        } else {
            menuItem.price.replace(",", "").replace("₩", "").toInt()
        }
        holder.price.text = "₩${itemPrice * orderItem.quantity}"

        holder.buttonDecrease.setOnClickListener {
            if (orderItem.quantity > 1) {
                orderItem.quantity--
                holder.itemCount.text = orderItem.quantity.toString()
                holder.price.text = "₩${itemPrice * orderItem.quantity}"
                CartManager.updateItemQuantity(orderItem, orderItem.quantity)
                notifyItemChanged(position)
            }
        }

        holder.buttonIncrease.setOnClickListener {
            orderItem.quantity++
            holder.itemCount.text = orderItem.quantity.toString()
            holder.price.text = "₩${itemPrice * orderItem.quantity}"
            CartManager.updateItemQuantity(orderItem, orderItem.quantity)
            notifyItemChanged(position)
        }

        holder.cancelButton.setOnClickListener {
            onRemoveItem(orderItem)
        }
    }

    override fun getItemCount() = orderItems.size

    fun updateOrderItems(newOrderItems: List<OrderItem>) {
        orderItems = newOrderItems.toMutableList()
        notifyDataSetChanged()
    }
}
