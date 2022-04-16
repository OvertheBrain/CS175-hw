package com.bytedance.jstu.demo.lesson6.todo

import android.database.sqlite.SQLiteDatabase
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bytedance.jstu.demo.R
import com.bytedance.jstu.demo.lesson6.db.MyDBHelper

class TaskAdapter(activity: TasksActivity) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>()  {
    private var contentList = mutableListOf<Task>()
    private val mainActivity = activity

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val v = View.inflate(parent.context, R.layout.activity_tasks_adapter, null)
        val viewHolder = TaskViewHolder(v)
        viewHolder.delTask.setOnClickListener{
            val title = viewHolder.taskTitle.text

            val dbHelper = MyDBHelper(mainActivity, "Todo.db", 1)
            val db: SQLiteDatabase? = dbHelper.writableDatabase

            db?.delete("todoList", "title = ?", arrayOf(title.toString()))
            Toast.makeText(mainActivity, "任务${title}已删除", Toast.LENGTH_SHORT).show()
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val todo = contentList[position]
        holder.taskTitle.text = todo.title
        holder.taskDesc.text = todo.description

    }

    override fun getItemCount(): Int = contentList.size

    fun setContentList(list: List<Task>) {
        contentList.clear()
        contentList.addAll(list)
        notifyDataSetChanged()
    }


    class TaskViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val taskTitle = view.findViewById<TextView>(R.id.task_title)
        val taskDesc = view.findViewById<TextView>(R.id.task_description)
        val delTask = view.findViewById<TextView>(R.id.delete)
    }
}
