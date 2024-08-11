package com.example.kiumi

import android.content.Context
import android.content.SharedPreferences
import android.graphics.drawable.AnimationDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
class MenuTutorialAdapter(
    private var menuItems: List<MenuItem>,
    private val onItemClick: (MenuItem) -> Unit,
    private val context: Context  // Context를 받아서 SharedPreferences에 접근할 수 있게 합니다.
) : RecyclerView.Adapter<MenuTutorialAdapter.MenuViewHolder>() {

    private val preferences: SharedPreferences = context.getSharedPreferences("com.example.kiumi.PREFERENCES", Context.MODE_PRIVATE)

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
                    val menuItem = menuItems[position]
                    val isBurgerSetClicked = preferences.getBoolean("burger_set_clicked", false)

                    // "창녕 갈릭 버거"이고 burger_set_clicked가 false일 때만 클릭 이벤트 처리
                    if (menuItem.name == "창녕 갈릭 버거" && !isBurgerSetClicked) {
                        onItemClick(menuItem)
                    }
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

        // SharedPreferences에서 상태를 확인하여 배경 설정
        val isBurgerSetClicked = preferences.getBoolean("burger_set_clicked", false)

        if (menuItem.name == "창녕 갈릭 버거") {
            if (isBurgerSetClicked) {
                holder.itemView.setBackgroundResource(R.drawable.item_background)
            } else {
                holder.itemView.setBackgroundResource(R.drawable.blinking_border_animation)
                val background = holder.itemView.background
                if (background is AnimationDrawable) {
                    background.start()
                }
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
