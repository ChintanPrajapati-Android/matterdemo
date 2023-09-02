package com.matter.matterdemo.ui

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.lifecycle.ViewModelProvider
import com.matter.matterdemo.databinding.ActivityMainBinding
import com.matter.matterdemo.vm.GameViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {
    private lateinit var gameViewModel: GameViewModel
    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    private fun init() {
        gameViewModel = ViewModelProvider(this)[GameViewModel::class.java]
        CoroutineScope(Dispatchers.Main).launch(Dispatchers.Main) {
            gameViewModel.setData.collect {
                if (it.isEmpty.not()) {
                    it.keys.forEach { key ->
                        val data = it[key]
                        setTextView(key, data?.label.orEmpty())
                        binding.tvStatus.text = data?.status.orEmpty()
                    }
                }
            }
        }

        binding.btnReset.setOnClickListener {
            gameViewModel.reset()
        }
    }

    fun onClick(view: View) {
        if (view is TextView) {
            val clickPosition = view.tag.toString().toInt()
            gameViewModel.onTapped(clickPosition)
        }
    }

    private fun setTextView(clickPosition: Int, value: String) {
        for (i in 0 until binding.constraintGame.childCount) {
            val view: View = binding.constraintGame[i]
            if (view is TextView) {
                val tag = view.tag.toString().toInt()
                if (tag == clickPosition) {
                    view.text = value
                }
                if (clickPosition == -2) {
                    view.text = ""
                }
            }
        }
    }
}
