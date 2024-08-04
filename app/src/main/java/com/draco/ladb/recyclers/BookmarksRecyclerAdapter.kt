package com.draco.ladb.recyclers

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.TextView
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.RecyclerView
import com.draco.ladb.R

class BookmarksRecyclerAdapter(context: Context) : RecyclerView.Adapter<BookmarksRecyclerAdapter.ViewHolder>() {
    private val list = sortedSetOf<String>()
    private val prefs = PreferenceManager.getDefaultSharedPreferences(context)

    var pickHook: (String) -> Unit = {}
    var deleteHook: (String) -> Unit = {}
    var editHook: (String) -> Unit = {}

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val content: TextView = view.findViewById(R.id.content)
        val options: ImageButton = view.findViewById(R.id.overflow_menu)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(refresh: Boolean = true) {
        list.clear()
        list.addAll(prefs.getStringSet("bookmarks", setOf()) ?: emptySet())
        if (refresh)
            notifyDataSetChanged()
    }

    private fun saveList() {
        with(prefs.edit()) {
            putStringSet("bookmarks", list)
            apply()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun add(text: String) {
        list.add(text)
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
    fun edit(text: String, newText: String) {
        list.remove(text)
        list.add(newText)
        notifyDataSetChanged()
        saveList()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        updateList(false)
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val text = list.elementAt(position)
        holder.content.text = text
        holder.itemView.setOnClickListener { pickHook(text) }

        // Set up the overflow menu
        holder.options.setOnClickListener { view ->
            val popup = PopupMenu(view.context, view)
            popup.inflate(R.menu.menu_overflow) // Inflate the overflow menu layout
            popup.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.action_edit -> {
                        editHook(text)
                        true
                    }
                    R.id.action_delete -> {
                        deleteHook(text)
                        true
                    }
                    else -> false
                }
            }
            popup.show()
        }
    }

    override fun getItemCount() = list.size
}