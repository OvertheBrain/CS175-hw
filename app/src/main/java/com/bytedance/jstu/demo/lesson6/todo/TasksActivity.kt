package com.bytedance.jstu.demo.lesson6.todo

import android.annotation.SuppressLint
import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EdgeEffect
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bytedance.jstu.demo.R
import com.bytedance.jstu.demo.lesson6.db.MyDBHelper

class TasksActivity : AppCompatActivity() {
    private val addTask: Button by lazy {
        findViewById(R.id.add)
    }

    private val showTasks: Button by lazy {
        findViewById(R.id.display)
    }

    private var titleText: EditText? = null
    private var DesText: EditText? = null

    private val dbHelper = MyDBHelper(this, "Todo.db", 1)
    private var db: SQLiteDatabase? = null
    private var todoList = mutableListOf<Task>()

    fun updateList() {
        todoList.clear()
        val cursor = (db?: dbHelper.writableDatabase).query("todoList", null, null, null, null, null, null, null)
        if (cursor.moveToFirst()) {
            do {
                val title = cursor.getString(cursor.getColumnIndexOrThrow("title"))
                val desc = cursor.getString(cursor.getColumnIndexOrThrow("description"))
                todoList.add(Task(title, desc))
            } while (cursor.moveToNext())
        }
        cursor.close()
    }

    @SuppressLint("Range")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tasks)
        titleText = findViewById(R.id.title)
        DesText = findViewById(R.id.description)

        db = dbHelper.writableDatabase


        addTask.setOnClickListener {
            val values = ContentValues().apply {
                if(titleText?.text == null) return@apply

                put("title", "${titleText?.text}")
                put("description", "${DesText?.text}")
            }
            db?.insert("todoList", null, values)

            updateList()
        }

        showTasks.setOnClickListener{
            val recyclerView = findViewById<RecyclerView>(R.id.tasks_list)
            recyclerView.layoutManager = LinearLayoutManager(this)

            val adapter = TaskAdapter(this)
            updateList()
            adapter.setContentList(todoList)
            recyclerView.adapter = adapter
        }
    }
}
