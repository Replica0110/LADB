package com.draco.ladb.views

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.draco.ladb.R
import com.draco.ladb.databinding.ActivityHistoryBinding
import com.draco.ladb.viewmodels.HistoryActivityViewModel


class HistoryActivity: AppCompatActivity() {
    private val viewModel: HistoryActivityViewModel by viewModels()
    private lateinit var binding: ActivityHistoryBinding
    private lateinit var initialText: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initialText = intent.getStringExtra(Intent.EXTRA_TEXT) ?: ""

        viewModel.prepareRecycler(this, binding.historyRecycler)
        viewModel.recyclerAdapter.pickHook = {
            val intent = Intent()
                .putExtra(Intent.EXTRA_TEXT, it)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }

        viewModel.recyclerAdapter.deleteHook = {
            viewModel.areYouSure(this) { viewModel.recyclerAdapter.delete(it) }
        }


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // 清空记录
        return when (item.itemId) {
            R.id.delete_all -> {
                viewModel.clear(this)
                true
            }

            else -> super.onOptionsItemSelected(item)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.shell_history, menu)
        return true
    }
}