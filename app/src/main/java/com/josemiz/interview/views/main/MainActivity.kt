package com.josemiz.interview.views.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.josemiz.interview.R
import com.josemiz.interview.databinding.ActivityMainBinding
import com.josemiz.interview.models.ShiftModel
import com.josemiz.interview.views.create.CreateShiftActivity
import org.koin.androidx.scope.currentScope
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by currentScope.viewModel(this)
    private val adapter: MainAdapter = MainAdapter(emptyList())
    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == 123) {
                val model = it.data?.getParcelableExtra<ShiftModel>("model")
                viewModel.addShift(model)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.rvList.adapter = adapter
        binding.fabAddShift.setOnClickListener {
            val intent = Intent(this, CreateShiftActivity::class.java)
            resultLauncher.launch(intent)
        }
        viewModel.shiftList.observe(this, ::observeShiftList)
        viewModel.loadShifts(assets)
    }

    private fun observeShiftList(list: List<ShiftModel>) {
        adapter.loadList(list)
    }
}