package com.example.kiumi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ActualPracticeOrderActivity : AppCompatActivity() {

    private lateinit var orderRecyclerView: RecyclerView
    private lateinit var orderAdapter: OrderAdapter
    private lateinit var orderItems: MutableList<OrderItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actual_practice_order)

        orderRecyclerView = findViewById(R.id.order_recycler_view)
        orderRecyclerView.layoutManager = LinearLayoutManager(this)

        // 샘플 데이터 생성
//        val menuItem = MenuItem("맥스파이시® 상하이 버거 - 세트", "₩5,900", "826 Kcal",R.drawable.ic_burger_set, true)
//        val menuItem2 = MenuItem("불고기 버거 - 세트", "₩4,500", "700 Kcal", R.drawable.ic_burger_set, false)

//        orderItems = mutableListOf(
//            OrderItem(menuItem) ,
//            OrderItem(menuItem2)
//            // 더 많은 아이템 추가 가능
//        )
        orderItems = CartManager.getItems().toMutableList()

        orderAdapter = OrderAdapter(orderItems){ itemToRemove ->
            CartManager.removeItem(itemToRemove)
            orderItems.remove(itemToRemove)
            orderAdapter.notifyDataSetChanged()
            updateTotalPrice()
        }
        orderRecyclerView.adapter = orderAdapter
        CartManager.addListener { updateTotalPrice() }
        updateTotalPrice()

        findViewById<TextView>(R.id.order_more).setOnClickListener {
            val intent = Intent(this@ActualPracticeOrderActivity, ActualPracticeMainActivity::class.java)
            startActivity(intent)
        }

        findViewById<TextView>(R.id.order_finish).setOnClickListener {
            val intent = Intent(this, ActualPracticePaymentSelection::class.java)
            startActivity(intent)
        }

        findViewById<LinearLayout>(R.id.button_points).setOnClickListener {
            PointManager.setPointEarned(true)
            val intent = Intent(this@ActualPracticeOrderActivity, ActualPracticeQRSuccess::class.java)
            startActivity(intent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        CartManager.removeListener { updateTotalPrice() }
    }

    private fun updateTotalPrice() {
        val sumPriceTextView: TextView = findViewById(R.id.sumif)
        val totalPriceTextView: TextView = findViewById(R.id.sumall)
        sumPriceTextView.text = CartManager.getTotalPrice()
        totalPriceTextView.text = CartManager.getTotalPrice()
    }
}