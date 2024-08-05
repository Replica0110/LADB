package com.draco.ladb.recyclers

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.RecyclerView
import com.draco.ladb.R
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class HistoryRecyclerAdapter(context: Context) : RecyclerView.Adapter<HistoryRecyclerAdapter.ViewHolder>() {
    private val list = mutableListOf<String>()
    private val gson = Gson()
    private val prefs = PreferenceManager.getDefaultSharedPreferences(context)
    private val historyKey = "history"

    var pickHook: (String) -> Unit = {}
    var deleteHook: (String) -> Unit = {}

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val content: TextView = view.findViewById(R.id.history_content)
        val delete: ImageButton = view.findViewById(R.id.history_delete)
    }

    init {
        updateList()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(refresh: Boolean = true) {
        list.clear()
        val historyJson = prefs.getString(historyKey, null)
        if (historyJson != null) {
            val type = object : TypeToken<List<String>>() {}.type
            val items: List<String> = gson.fromJson(historyJson, type)
            list.addAll(items)
        }
        if (refresh) {
            notifyDataSetChanged()
        }
    }

    private fun saveList() {
        val historyJson = gson.toJson(list)
        prefs.edit().putString(historyKey, historyJson).apply()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun add(text: String) {
        if (list.contains(text)){
            list.remove(text)
        }
        list.add(text)
        while (list.size > 50) {
            list.removeAt(0)
        }
        notifyDataSetChanged()
        saveList()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun delete(text: String) {
        list.remove(text)
        notifyDataSetChanged()
        saveList()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun clear() {
        if (list.isNotEmpty()) {
            list.clear()
            notifyDataSetChanged()
            saveList()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.history_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.content.text = item
        holder.content.setOnClickListener { pickHook(item) }
        holder.delete.setOnClickListener { deleteHook(item) }
    }

    override fun getItemCount(): Int = list.size
}
