package com.example.kiumi

import android.graphics.drawable.AnimationDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MenuTutorialAdapter(
    private var menuItems: List<MenuItem>,
    private val onItemClick: (MenuItem) -> Unit
) : RecyclerView.Adapter<MenuTutorialAdapter.MenuViewHolder>() {

    inner class MenuViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.image)
        val name: TextView = itemView.findViewById(R.id.name)
        val price: TextView = itemView.findViewById(R.id.price)
        val calories: TextView = itemView.findViewById(R.id.calories)
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
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.menu_tutorial_item, parent, false)
        return MenuViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        val menuItem = menuItems[position]
        holder.image.setImageResource(menuItem.imageResourceId)
        holder.name.text = menuItem.name
        holder.price.text = menuItem.price
        holder.calories.text = menuItem.calories
        holder.newLabel.visibility = if (menuItem.isNew) View.VISIBLE else View.GONE

        // 배경 설정
        if (menuItem.name == "창녕 갈릭 버거") {
            holder.itemView.setBackgroundResource(R.drawable.blinking_border_animation)
            val background = holder.itemView.background
            if (background is AnimationDrawable) {
                (background as AnimationDrawable).start()
            }
        } else {
            holder.itemView.setBackgroundResource(R.drawable.item_background)
        }
    }

    override fun getItemCount() = menuItems.size

    fun updateMenuItems(newMenuItems: List<MenuItem>) {
        menuItems = newMenuItems
        notifyDataSetChanged()
    }
}
