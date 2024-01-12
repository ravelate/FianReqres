package com.felina.fianreqres.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.felina.fianreqres.adapter.LoadingStateAdapter
import com.felina.fianreqres.adapter.UserListAdapter
import com.felina.fianreqres.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val mainViewModel: MainViewModel by viewModels {
        ViewModelFactory(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvUsers.layoutManager = LinearLayoutManager(this)

        getData()
    }

    private fun getData() {
        val adapter = UserListAdapter()
        binding.rvUsers.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                adapter.retry()
            }
        )
        mainViewModel.user.observe(this, {
            adapter.submitData(lifecycle, it)
        })
    }
}