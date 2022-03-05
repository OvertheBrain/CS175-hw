package com.bytedance.jstu.demo.lesson2

import android.content.Intent
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bytedance.jstu.demo.R

/**
 * Created by shenjun on 2/21/22.
 */
class SearchItemAdapter(activity: RecyclerViewDemoActivity) : RecyclerView.Adapter<SearchItemAdapter.SearchItemViewHolder>() {

    private val contentList = mutableListOf<String>()
    private val filteredList = mutableListOf<String>()
    private val levelList = mutableListOf<String>()
    private val solutionList = mutableListOf<String>()
    private val passList = mutableListOf<String>()
    private val mainActivity = activity

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchItemViewHolder {
        val v = View.inflate(parent.context, R.layout.search_item_layout, null)
        val view = SearchItemViewHolder(v)
        view.itemView.setOnClickListener {
            val position = view.adapterPosition
            val info = mainActivity.getInfo()
            val infoItem = info?.get(position)
            val intent = Intent(mainActivity, Details::class.java)
            intent.putExtra("info", infoItem)
            mainActivity.startActivity(intent)
        }
        return view
    }

    override fun onBindViewHolder(holder: SearchItemViewHolder, position: Int) {
        holder.bind(filteredList[position], levelList[position], passList[position], solutionList[position])
    }

    override fun getItemCount(): Int = filteredList.size

    fun setContentList(list1: List<String>, list2: List<String>, list3: List<String>, list4: List<String>) {
        contentList.clear()
        contentList.addAll(list1)
        filteredList.clear()
        filteredList.addAll(list1)
        levelList.clear()
        levelList.addAll(list2)
        solutionList.clear()
        solutionList.addAll(list3)
        passList.clear()
        passList.addAll(list4)
        notifyDataSetChanged()
    }

    fun setFilter(keyword: String?) {
        filteredList.clear()
        if (keyword?.isNotEmpty() == true) {
            filteredList.addAll(contentList.filter { it.contains(keyword) })
        } else {
            filteredList.addAll(contentList)
        }
        notifyDataSetChanged()
    }

    class SearchItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val tv = view.findViewById<TextView>(R.id.search_item_tv)
        private val level = view.findViewById<TextView>(R.id.search_item_level)
        private val sol = view.findViewById<TextView>(R.id.search_item_solution)
        private val pass = view.findViewById<TextView>(R.id.search_item_pass)

        private val colorMap = mapOf<String, String>("简单" to "#2E7D32", "中等" to "#F9A825", "困难" to "#C62828")

        fun bind(content: String, diff: String, p: String, s: String) {
            tv.text = content
            level.text = diff
            level.setTextColor(Color.parseColor(colorMap[diff]))
            sol.text = "题解: $s"
            pass.text = "通过率： $p"
        }
    }

}
