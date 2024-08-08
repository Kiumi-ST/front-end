package com.example.kiumi

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ProposalMenuAdapter (
    private var menuItems: List<ProposalMenuItem>,
    private val onItemClick: (ProposalMenuItem) -> Unit
) : RecyclerView.Adapter<ProposalMenuAdapter.MenuViewHolder>() {

    inner class MenuViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.image)
        val name: TextView = itemView.findViewById(R.id.name)
        val price: TextView = itemView.findViewById(R.id.price)
        val newLabel: TextView = itemView.findViewById(R.id.new_label)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClick(menuItems[position])
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.menu_item, parent, false)
        return MenuViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        val menuItem = menuItems[position]
        holder.image.setImageResource(menuItem.imageResourceId)
        holder.name.text = menuItem.name
        holder.price.text = menuItem.price
        holder.newLabel.visibility = if (menuItem.isNew) View.VISIBLE else View.GONE
    }

    override fun getItemCount() = menuItems.size

    fun updateMenuItems(newMenuItems: List<ProposalMenuItem>) {
        menuItems = newMenuItems
        notifyDataSetChanged()
    }

}