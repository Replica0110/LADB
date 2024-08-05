package com.draco.ladb.viewmodels

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.AndroidViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.draco.ladb.R
import com.draco.ladb.recyclers.HistoryRecyclerAdapter

class HistoryActivityViewModel(application: Application): AndroidViewModel(application) {
    val recyclerAdapter = HistoryRecyclerAdapter(application.applicationContext)

    fun prepareRecycler(context: Context, recycler: RecyclerView) {
        recycler.apply {
            adapter = recyclerAdapter
            layoutManager = LinearLayoutManager(context)
        }
        recyclerAdapter.updateList()
    }

    fun clear(context: Context) {
        AlertDialog.Builder(context)
            .setTitle(R.string.clear)
            .setMessage(R.string.clear_confirm)
            .setPositiveButton(R.string.delete) { _, _ ->
                recyclerAdapter.clear()
                recyclerAdapter.updateList()
            }
            .setNegativeButton(R.string.cancel, null)
            .show()
    }


    fun areYouSure(context: Context, callback: () -> Unit) {
        AlertDialog.Builder(context)
            .setTitle(R.string.delete)
            .setMessage(R.string.delete_confirm)
            .setPositiveButton(R.string.delete) { _, _ -> callback() }
            .setNegativeButton(R.string.cancel, null)
            .show()
    }

}