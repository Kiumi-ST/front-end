package com.example.kiumi

object CartManager {
    private val cartItems = mutableListOf<OrderItem>()
    private val listeners = mutableListOf<() -> Unit>()

    fun addItem(item: OrderItem) {
        cartItems.add(item)
        notifyListeners()
    }

    fun removeItem(item: OrderItem) {
        cartItems.remove(item)
        notifyListeners()
    }

    fun getItems(): List<OrderItem> {
        return cartItems
    }

    fun clearCart() {
        cartItems.clear()
    }

    fun updateItemQuantity(item: OrderItem, quantity: Int) {
        val index = cartItems.indexOf(item)
        if (index != -1) {
            cartItems[index].quantity = quantity
            notifyListeners()
        }
    }

    fun getTotalPrice(): String {
        var total = 0
        for (item in cartItems) {
            val itemPrice = if (item.isSet) {
                item.totalPrice.replace("₩", "").replace(",", "").toInt()
            } else {
                item.menuItem.price.replace("₩", "").replace(",", "").toInt()
            }
            total += itemPrice * item.quantity
        }
        return "₩${total}"
    }

    fun addListener(listener: () -> Unit) {
        listeners.add(listener)
    }

    fun removeListener(listener: () -> Unit) {
        listeners.remove(listener)
    }

    fun getTotalItems(): Int {
        return cartItems.size
    }

    private fun notifyListeners() {
        for (listener in listeners) {
            listener()
        }
    }
}
