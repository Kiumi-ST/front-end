package com.example.kiumi

data class MenuItem(
    val name: String,
    val price: String,
    val calories: String,
    val imageResourceId: Int,
    val isNew: Boolean
)

data class ProposalMenuItem(
    val name: String,
    val price: String,
    val imageResourceId: Int,
    val isNew: Boolean
)

data class OrderItem(
    val menuItem: MenuItem,
    var quantity: Int = 1
)

data class ProposalOrderItem(
    val menuItem: ProposalMenuItem,
    var quantity: Int = 1
)
